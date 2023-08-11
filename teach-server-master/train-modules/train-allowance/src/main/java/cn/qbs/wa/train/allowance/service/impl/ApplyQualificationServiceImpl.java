package cn.qbs.wa.train.allowance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.train.allowance.entity.*;
import cn.qbs.wa.train.allowance.enums.*;
import cn.qbs.wa.train.allowance.mapper.ApplyAttachMapper;
import cn.qbs.wa.train.allowance.mapper.ApplyMapper;
import cn.qbs.wa.train.allowance.mapper.ApplyQualificationMapper;
import cn.qbs.wa.train.allowance.pojo.apply.QualificationApplyRequest;
import cn.qbs.wa.train.allowance.pojo.apply.QualificationApplyResponse;
import cn.qbs.wa.train.allowance.pojo.apply.QualificationProjectResponse;
import cn.qbs.wa.train.allowance.pojo.apply.QualificationUpdateRequest;
import cn.qbs.wa.train.allowance.service.ApplyQualificationCourseService;
import cn.qbs.wa.train.allowance.service.ApplyQualificationProjectService;
import cn.qbs.wa.train.allowance.service.ApplyQualificationService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 资助资格申请(ApplyQualification)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2022-11-02 11:20:28
 */
@Slf4j
@Service("applyQualificationService")
public class ApplyQualificationServiceImpl extends ServiceImpl<ApplyQualificationMapper, ApplyQualification> implements ApplyQualificationService {

    @Resource
    private ApplyMapper applyMapper;

    @Resource
    private ApplyQualificationProjectService qualificationProjectService;

    @Resource
    private ApplyQualificationCourseService qualificationCourseService;

    @Resource
    private ApplyAttachMapper applyAttachMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ApplyQualification save(QualificationApplyRequest applyRequest) {
        Long orgId = applyRequest.getOrgId();
        Apply apply = BeanUtil.copyProperties(applyRequest, Apply.class);
        // 资质申请标识
        apply.setApplyType(ApplyTypeEnum.QUALIFICATION.getCode());
        apply.setFlowCode(FlowCodeEnum.ORG_QUALIFICATION.getCode());
        // 保存表头信息
        applyMapper.insert(apply);

        // 保存资质申请表单信息
        ApplyQualification applyQualification = new ApplyQualification();
        applyQualification.setOrgId(orgId);
        applyQualification.setApplyId(apply.getId());
        this.save(applyQualification);

        return applyQualification;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ApplyQualification copy(Long applyId) {
        Apply apply = applyMapper.selectById(applyId);
        if (apply == null) {
            throw new ServiceException("申请不存在，请刷新后操作");
        }
        Long orgId = SecurityContextHolder.getOrgId();
        if (!orgId.equals(apply.getOrgId())) {
            throw new ServiceException("非法操作");
        }
        // 组装成新增对象
        QualificationApplyResponse detail = this.detail(applyId);
        if(StringUtils.isNotBlank(detail.getContactNumber())){
            detail.setContactNumber(AesUtil.encode(detail.getContactNumber()));
        }
        if(StringUtils.isNotBlank(detail.getLegalNumber())){
            detail.setLegalNumber(AesUtil.encode(detail.getLegalNumber()));
        }
        QualificationApplyRequest applyRequest = BeanUtil.copyProperties(detail, QualificationApplyRequest.class);
        ApplyQualification qualification = this.save(applyRequest);

        // 保存项目计划以及课程
        qualificationProjectService.batchAdd(applyRequest.getProjectList(), qualification.getId());

        // 拷贝附件
        applyAttachMapper.copyByApplyId(applyId, AttachSectionEnum.APPLY.getCode(), qualification.getApplyId());

        return qualification;
    }

    @Override
    public QualificationApplyResponse detail(Long applyId) {
        Apply apply = applyMapper.selectOne(Wrappers.<Apply>lambdaQuery().eq(Apply::getId, applyId));
        if (apply == null) {
            return new QualificationApplyResponse();
        }
        QualificationApplyResponse response = BeanUtil.copyProperties(apply, QualificationApplyResponse.class);
        // 手机号解密
        response.setContactNumber(AesUtil.decode(response.getContactNumber()));
        response.setLegalNumber(AesUtil.decode(response.getLegalNumber()));
        ApplyQualification one = this.lambdaQuery().select(ApplyQualification::getId).eq(ApplyQualification::getApplyId, applyId).one();
        if (one != null) {
            response.setQualificationId(one.getId());
            // 获取项目计划及其课程列表
            List<QualificationProjectResponse> projectList = qualificationProjectService.listProjectInfoByQualId(one.getId());
            response.setProjectList(projectList);
        }
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean update(QualificationUpdateRequest updateRequest) {
        Apply apply = BeanUtil.copyProperties(updateRequest, Apply.class);
        apply.setId(updateRequest.getApplyId());
        // 更新信息
        applyMapper.updateById(apply);
        ApplyQualification one = this.lambdaQuery().eq(ApplyQualification::getApplyId, apply.getId()).one();
        Long orgId = updateRequest.getOrgId();
        if (one == null) {
            // 保存资质申请表单信息
            one = new ApplyQualification();
            one.setOrgId(orgId);
            one.setApplyId(apply.getId());
            this.save(one);
        }
        return Boolean.TRUE;
    }

    @Override
    public void checkCompleteness(Long applyId) {
        ApplyQualification one = this.lambdaQuery().eq(ApplyQualification::getApplyId, applyId).one();
        if (one == null) {
            throw new ServiceException("该资质申请信息不完整");
        }
        if (!one.getOrgId().equals(SecurityContextHolder.getOrgId())) {
            throw new ServiceException("非法操作");
        }
        Long qualificationId = one.getId();
        List<ApplyQualificationProject> projectList = qualificationProjectService.lambdaQuery().eq(ApplyQualificationProject::getQualificationId, qualificationId).list();
        if (projectList.isEmpty()) {
            throw new ServiceException("该资质申请未填写项目计划");
        }
        for (ApplyQualificationProject project : projectList) {
            Long count = qualificationCourseService.lambdaQuery().eq(ApplyQualificationCourse::getProjectId, project.getId()).count();
            if (count == null || count == 0) {
                throw new ServiceException(String.format("该资质申请中项目计划：%s, 缺少课程，请补充完善", project.getProjectName()));
            }
        }

        // 校验附件完整性
        /*List<ApplyAttach> attachCodeList = applyAttachMapper.selectList(
                Wrappers.<ApplyAttach>lambdaQuery().select(ApplyAttach::getAttachCode)
                        .eq(ApplyAttach::getApplyId, applyId)
                        .eq(ApplyAttach::getSection, AttachSectionEnum.APPLY.getCode())
        );

        if (CollUtil.isEmpty(attachCodeList)) {
            throw new ServiceException("请上传申请相关的资料附件信息");
        }

        if (CollUtil.isNotEmpty(attachCodeList)) {
            // 目前限制 除`其他证明材料`外所有附件必须上传
            long count = attachCodeList.stream().map(ApplyAttach::getAttachCode).distinct().count();
            if (count < 12) {
                throw new ServiceException("请补全申请相关的资料附件");
            }
        }*/
    }

    @Override
    public void checkUniqueness(Long orgId) {
        Long count = applyMapper.selectCount(Wrappers.<Apply>lambdaQuery().eq(Apply::getOrgId, orgId)
                .eq(Apply::getApplyType, ApplyTypeEnum.QUALIFICATION.getCode())
                .eq(Apply::getApplyStatus, ApplyStatusEnum.NOT_SUBMIT.getCode()));
        if (count != null && count > 0) {
            throw new ServiceException("当前存在未提交资质申请,无法再次申请");
        }

        count = applyMapper.selectCount(Wrappers.<Apply>lambdaQuery().eq(Apply::getOrgId, orgId)
                .eq(Apply::getApplyType, ApplyTypeEnum.QUALIFICATION.getCode())
                .eq(Apply::getApplyStatus, ApplyStatusEnum.SUBMIT.getCode())
                .in(Apply::getFlowStatus, FlowStatusEnum.ING.getCode(), FlowStatusEnum.STOP.getCode()));
        if (count != null && count > 0) {
            throw new ServiceException("您的资质申请正在审核中，无需再次申请");
        }

        count = applyMapper.selectCount(Wrappers.<Apply>lambdaQuery().eq(Apply::getOrgId, orgId)
                .eq(Apply::getApplyType, ApplyTypeEnum.QUALIFICATION.getCode())
                .eq(Apply::getApplyStatus, ApplyStatusEnum.SUBMIT.getCode())
                .eq(Apply::getApplyResult, ApplyResultEnum.PASS.getCode()));
        if (count != null && count > 0) {
            throw new ServiceException("您的资质申请已通过，无需再次申请");
        }
    }

    @Override
    public boolean checkPassStatus() {
        Long count = applyMapper.selectCount(Wrappers.<Apply>lambdaQuery().eq(Apply::getOrgId, SecurityContextHolder.getOrgId())
                .eq(Apply::getApplyType, ApplyTypeEnum.QUALIFICATION.getCode())
                .eq(Apply::getApplyStatus, ApplyStatusEnum.SUBMIT.getCode())
                .eq(Apply::getApplyResult, ApplyResultEnum.PASS.getCode()));
        return count > 0;
    }
}

