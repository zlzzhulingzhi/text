package cn.qbs.wa.train.allowance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.teach.common.core.utils.TreeUtil;
import cn.qbs.wa.train.allowance.entity.Apply;
import cn.qbs.wa.train.allowance.entity.ApplyAllowance;
import cn.qbs.wa.train.allowance.entity.ApplyAllowanceFee;
import cn.qbs.wa.train.allowance.entity.ApplyAllowanceStudent;
import cn.qbs.wa.train.allowance.enums.*;
import cn.qbs.wa.train.allowance.mapper.ApplyAllowanceMapper;
import cn.qbs.wa.train.allowance.mapper.ApplyAttachMapper;
import cn.qbs.wa.train.allowance.mapper.ApplyMapper;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyPageResponse;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyRequest;
import cn.qbs.wa.train.allowance.pojo.applyallowance.*;
import cn.qbs.wa.train.allowance.pojo.applyallowancefee.ApplyAllowanceFeeAddRequest;
import cn.qbs.wa.train.allowance.pojo.applyallowancefee.ApplyAllowanceFeeDetailResponse;
import cn.qbs.wa.train.allowance.pojo.applyallowancefee.ApplyAllowanceFeeUpdateRequest;
import cn.qbs.wa.train.allowance.pojo.applyallowancestudent.ApplyAllowanceStudentAddRequest;
import cn.qbs.wa.train.allowance.pojo.applyallowancestudent.ApplyAllowanceStudentDetailResponse;
import cn.qbs.wa.train.allowance.pojo.applyallowancestudent.ApplyAllowanceStudentUpdateRequest;
import cn.qbs.wa.train.allowance.service.ApplyAllowanceFeeService;
import cn.qbs.wa.train.allowance.service.ApplyAllowanceService;
import cn.qbs.wa.train.allowance.service.ApplyAllowanceStudentService;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 资助资金申请-网络安全培训课程(ApplyAllowance)表服务实现类
 *
 * @author makejava
 * @since 2022-11-03 19:28:56
 */
@Slf4j
@Service("applyAllowanceService")
public class ApplyAllowanceServiceImpl extends ServiceImpl<ApplyAllowanceMapper, ApplyAllowance> implements ApplyAllowanceService {

    @Resource
    private ApplyMapper applyMapper;

    @Resource
    private ApplyAllowanceFeeService applyAllowanceFeeService;

    @Resource
    private ApplyAllowanceStudentService applyAllowanceStudentService;

    @Resource
    private ApplyAttachMapper applyAttachMapper;

    @Override
    public Long saveAllowanceApply(ApplyAllowanceSaveRequest params) {
        Long orgId = SecurityContextHolder.getOrgId();
        if (orgId == null) {
            throw new ServiceException("当前登录信息有误");
        }
        Apply apply = BeanUtil.copyProperties(params, Apply.class);
        // 课程申请标识
        apply.setApplyType(ApplyTypeEnum.ALLOWANCE.getCode());
        apply.setFlowCode(FlowCodeEnum.ORG_ALLOWANCE.getCode());
        if (StringUtils.isNotBlank(apply.getContactNumber())) {
            apply.setContactNumber(AesUtil.encode(apply.getContactNumber()));
        }
        if (StringUtils.isNotBlank(apply.getLegalNumber())) {
            apply.setLegalNumber(AesUtil.encode(apply.getLegalNumber()));
        }
        apply.setOrgId(orgId);

        // 保存表头信息
        applyMapper.insert(apply);

        List<ApplyAllowanceFeeAddRequest> applyAllowanceFeeAddRequestList =
                params.getApplyAllowanceAddRequest().getApplyAllowanceFeeAddRequestList();
        ApplyAllowance applyAllowance = BeanUtil.copyProperties(params.getApplyAllowanceAddRequest(),
                ApplyAllowance.class);
        applyAllowance.setApplyId(apply.getId());
        if (CollectionUtils.isNotEmpty(applyAllowanceFeeAddRequestList)) {
            // 算出申请单下所有费用总和
            BigDecimal reduce =
                    applyAllowanceFeeAddRequestList.stream().map(ApplyAllowanceFeeAddRequest::getAllowanceFee).reduce(BigDecimal.ZERO,
                            BigDecimal::add);
            applyAllowance.setTotalAllowanceFee(reduce);
        }
        applyAllowance.setOrgId(orgId);
        this.save(applyAllowance);

        //保存费用集合
        if (CollectionUtils.isNotEmpty(applyAllowanceFeeAddRequestList)) {
            List<ApplyAllowanceFee> applyAllowanceFees = TreeUtil.copyBeanList(applyAllowanceFeeAddRequestList,
                    ApplyAllowanceFee.class);
            applyAllowanceFees.forEach(l -> l.setApplyAllowanceId(applyAllowance.getId()));
            applyAllowanceFeeService.saveBatch(applyAllowanceFees);
        }

        //保存学员集合
        List<ApplyAllowanceStudentAddRequest> applyAllowanceStudentAddRequests =
                params.getApplyAllowanceAddRequest().getApplyAllowanceStudentAddRequests();
        if (CollectionUtils.isNotEmpty(applyAllowanceStudentAddRequests)) {
            List<ApplyAllowanceStudent> applyAllowanceStudents =
                    TreeUtil.copyBeanList(applyAllowanceStudentAddRequests, ApplyAllowanceStudent.class);
            applyAllowanceStudents.forEach(l -> l.setApplyAllowanceId(applyAllowance.getId()));
            applyAllowanceStudentService.saveBatch(applyAllowanceStudents);
        }

        return apply.getId();
    }
    

    @Override
    public ApplyDetailByAllowanceResponse detail(Serializable id) {

        //查询表单数据
        Apply apply = applyMapper.selectById(id);
        if (StringUtils.isNotBlank(apply.getContactNumber())) {
            apply.setContactNumber(AesUtil.decode(apply.getContactNumber()));
        }
        if (StringUtils.isNotBlank(apply.getLegalNumber())) {
            apply.setLegalNumber(AesUtil.decode(apply.getLegalNumber()));
        }
        ApplyDetailByAllowanceResponse applyDetailByAllowanceResponse = BeanUtil.copyProperties(apply,
                ApplyDetailByAllowanceResponse.class);

        //查询表单课程数据
        ApplyAllowance applyAllowance = this.lambdaQuery().eq(ApplyAllowance::getApplyId, apply.getId()).one();
        ApplyAllowanceDetailResponse applyAllowanceDetailResponse = BeanUtil.copyProperties(applyAllowance,
                ApplyAllowanceDetailResponse.class);

        //查询课程费用数据
        List<ApplyAllowanceFee> allowanceFees =
                applyAllowanceFeeService.lambdaQuery().eq(ApplyAllowanceFee::getApplyAllowanceId,
                        applyAllowance.getId()).list();
        if (CollectionUtils.isNotEmpty(allowanceFees)) {
            applyAllowanceDetailResponse.setApplyAllowanceFeeDetailResponses(TreeUtil.copyBeanList(allowanceFees,
                    ApplyAllowanceFeeDetailResponse.class));
        }

        //查询课程学员数据
        List<ApplyAllowanceStudent> allowanceStudents =
                applyAllowanceStudentService.lambdaQuery().eq(ApplyAllowanceStudent::getApplyAllowanceId,
                        applyAllowance.getId()).list();
        if (CollectionUtils.isNotEmpty(allowanceStudents)) {
            applyAllowanceDetailResponse.setApplyAllowanceStudentDetailResponseList(TreeUtil.copyBeanList(allowanceStudents, ApplyAllowanceStudentDetailResponse.class));
        }
        applyDetailByAllowanceResponse.setApplyAllowanceDetailResponse(applyAllowanceDetailResponse);
        return applyDetailByAllowanceResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(ApplyUpdateByAllowanceRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        //更改表单数据
        Apply apply = BeanUtil.copyProperties(params, Apply.class);
        if (StringUtils.isNotBlank(apply.getContactNumber())) {
            apply.setContactNumber(AesUtil.encode(apply.getContactNumber()));
        }
        if (StringUtils.isNotBlank(apply.getLegalNumber())) {
            apply.setLegalNumber(AesUtil.encode(apply.getLegalNumber()));
        }
        applyMapper.updateById(apply);

        //更改课程数据
        ApplyAllowance applyAllowance = BeanUtil.copyProperties(params.getApplyAllowanceUpdateRequest(),
                ApplyAllowance.class);

        //将费用集合先删除再插入
        List<ApplyAllowanceFee> allowanceFees =
                applyAllowanceFeeService.lambdaQuery().eq(ApplyAllowanceFee::getApplyAllowanceId,
                        applyAllowance.getId()).list();
        if (CollectionUtils.isNotEmpty(allowanceFees)) {
            applyAllowanceFeeService.removeByIds(allowanceFees);
        }
        List<ApplyAllowanceFeeUpdateRequest> applyAllowanceFeeUpdateRequests =
                params.getApplyAllowanceUpdateRequest().getApplyAllowanceFeeUpdateRequests();
        if (CollectionUtils.isNotEmpty(applyAllowanceFeeUpdateRequests)) {
            List<ApplyAllowanceFee> applyAllowanceFees = TreeUtil.copyBeanList(applyAllowanceFeeUpdateRequests,
                    ApplyAllowanceFee.class);
            applyAllowanceFeeService.saveBatch(applyAllowanceFees);
        }

        //将课程学员集合先删除再插入
        List<ApplyAllowanceStudent> allowanceStudents =
                applyAllowanceStudentService.lambdaQuery().eq(ApplyAllowanceStudent::getApplyAllowanceId,
                        applyAllowance.getId()).list();
        if (CollectionUtils.isNotEmpty(allowanceStudents)) {
            applyAllowanceStudentService.removeByIds(allowanceStudents);
        }
        List<ApplyAllowanceStudentUpdateRequest> applyAllowanceStudentUpdateRequests =
                params.getApplyAllowanceUpdateRequest().getApplyAllowanceStudentUpdateRequests();
        if (CollectionUtils.isNotEmpty(applyAllowanceStudentUpdateRequests)) {
            List<ApplyAllowanceStudent> students =
                    TreeUtil.copyBeanList(applyAllowanceStudentUpdateRequests, ApplyAllowanceStudent.class);
            applyAllowanceStudentService.saveBatch(students);
        }

        return this.updateById(applyAllowance);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }


    @Override
    public void checkCompleteness(Long id) {

        ApplyAllowance allowance = this.lambdaQuery().eq(ApplyAllowance::getApplyId, id).one();
        if (allowance == null) {
            throw new SecurityException("课程申请信息不完整");
        }
    }

    @Override
    public IPage<ApplyPageResponse> page(ApplyAllowancePageRequest request, ApplyRequest applyRequest) {
        return this.baseMapper.page(request.createMpPage(), applyRequest);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean copyAllowanceApply(Long applyId) {
        Apply apply = applyMapper.selectById(applyId);
        if (apply == null) {
            throw new ServiceException("申请不存在，请刷新后操作");
        }
        Long orgId = SecurityContextHolder.getOrgId();
        if (!orgId.equals(apply.getOrgId())) {
            throw new ServiceException("非法操作");
        }
        if (!ApplyResultEnum.NO_PASS.getCode().equals(apply.getApplyResult())) {
            throw new ServiceException("复制功能，仅只支持驳回的申请");
        }
        ApplyAllowance allowance = this.lambdaQuery().eq(ApplyAllowance::getApplyId, applyId).one();
        if (allowance == null) {
            throw new ServiceException("申请不存在，请刷新后操作");
        }
        Long sourceAllowanceId = allowance.getId();
        // 保存表头信息
        // 清空旧ID值
        apply.setId(null);
        apply.setFlowStatus(null);
        apply.setApplyResult(null);
        apply.setProcessNo(null);
        apply.setApplyStatus(ApplyStatusEnum.NOT_SUBMIT.getCode());
        applyMapper.insert(apply);
        // 保存资助申请表单信息
        Long targetApplyId = apply.getId();
        allowance.setApplyId(targetApplyId);
        allowance.setId(null);
        boolean save = this.save(allowance);

        // 拷贝成新增对象
        Long targetAllowanceId = allowance.getId();

        applyAllowanceStudentService.copyByApplyId(sourceAllowanceId, targetAllowanceId);

        applyAllowanceFeeService.copyByApplyId(sourceAllowanceId, targetAllowanceId);

        // 拷贝附件
        applyAttachMapper.copyByApplyId(applyId, AttachSectionEnum.APPLY.getCode(), targetApplyId);
        return save;
    }

}

