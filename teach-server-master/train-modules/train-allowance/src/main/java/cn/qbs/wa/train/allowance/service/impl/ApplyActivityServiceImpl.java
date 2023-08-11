package cn.qbs.wa.train.allowance.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.train.allowance.entity.Apply;
import cn.qbs.wa.train.allowance.entity.ApplyActivity;
import cn.qbs.wa.train.allowance.entity.ApplyAllowance;
import cn.qbs.wa.train.allowance.enums.*;
import cn.qbs.wa.train.allowance.mapper.ApplyActivityMapper;
import cn.qbs.wa.train.allowance.mapper.ApplyAttachMapper;
import cn.qbs.wa.train.allowance.mapper.ApplyMapper;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyPageResponse;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyRequest;
import cn.qbs.wa.train.allowance.pojo.applyactivity.*;
import cn.qbs.wa.train.allowance.service.ApplyActivityService;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 资助资金申请-学术会议和竞赛活动(ApplyActivity)表服务实现类
 *
 * @author makejava
 * @since 2022-11-03 19:27:14
 */
@Slf4j
@Service("applyActivityService")
public class ApplyActivityServiceImpl extends ServiceImpl<ApplyActivityMapper, ApplyActivity> implements ApplyActivityService {

    @Resource
    private ApplyMapper applyMapper;

    @Resource
    private ApplyAttachMapper applyAttachMapper;

    @Override
    public boolean add(ApplyActivityAddRequest params) {
        ApplyActivity applyActivity = new ApplyActivity();
        BeanUtils.copyProperties(params, applyActivity);
        return this.save(applyActivity);
    }
    

    @Override
    public ApplyActivityDetailResponse detail(Serializable id) {
        ApplyActivityDetailResponse applyActivityDetailResponse = new ApplyActivityDetailResponse();
        Apply apply = applyMapper.selectById(id);
        if (StringUtils.isNotBlank(apply.getContactNumber())) {
            apply.setContactNumber(AesUtil.decode(apply.getContactNumber()));
        }
        if (StringUtils.isNotBlank(apply.getLegalNumber())) {
            apply.setLegalNumber(AesUtil.decode(apply.getLegalNumber()));
        }
        BeanUtils.copyProperties(apply, applyActivityDetailResponse);
        ApplyActivity applyActivity = this.lambdaQuery().eq(ApplyActivity::getApplyId, apply.getId()).one();
        applyActivityDetailResponse.setApplyActivity(applyActivity);
        return applyActivityDetailResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(ApplyActivityUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        Apply selectById = applyMapper.selectById(params.getId());
        if (!selectById.getApplyStatus().equals(ApplyStatusEnum.NOT_SUBMIT.getCode())) {
            throw new SecurityException("当前表单已提交,无法编辑");
        }
        Apply apply = BeanUtil.copyProperties(params, Apply.class);
        if (StringUtils.isNotBlank(apply.getContactNumber())) {
            apply.setContactNumber(AesUtil.encode(apply.getContactNumber()));
        }
        if (StringUtils.isNotBlank(apply.getLegalNumber())) {
            apply.setLegalNumber(AesUtil.encode(apply.getLegalNumber()));
        }
        // 更新信息
        applyMapper.updateById(apply);

        ApplyActivity applyActivity = new ApplyActivity();
        BeanUtils.copyProperties(params.getApplyActivity(), applyActivity);
        return this.updateById(applyActivity);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public Long saveActivityApply(ApplyActivitySaveRequest params) {

        //保存申请单
        Apply apply = new Apply();
        BeanUtils.copyProperties(params, apply);
        apply.setOrgId(SecurityContextHolder.getOrgId());
        apply.setApplyType(ApplyTypeEnum.ACTIVITY.getCode());
        apply.setFlowCode(FlowCodeEnum.ORG_ACTIVITY.getCode());
        if (StringUtils.isNotBlank(apply.getContactNumber())) {
            apply.setContactNumber(AesUtil.encode(apply.getContactNumber()));
        }
        if (StringUtils.isNotBlank(apply.getLegalNumber())) {
            apply.setLegalNumber(AesUtil.encode(apply.getLegalNumber()));
        }
        applyMapper.insert(apply);

        //保存学术活动数据
        ApplyActivity applyActivity = new ApplyActivity();
        BeanUtils.copyProperties(params.getApplyActivityAddRequest(), applyActivity);
        applyActivity.setApplyId(apply.getId());
        this.save(applyActivity);
        return apply.getId();
    }

    @Override
    public void checkCompleteness(Long id) {

        ApplyActivity activity = this.lambdaQuery().eq(ApplyActivity::getApplyId, id).one();
        if (activity == null) {
            throw new ServiceException("该申请信息不完整");
        }
    }

    @Override
    public IPage<ApplyPageResponse> page(ApplyActivityPageRequest request, ApplyRequest applyRequest) {
        return this.baseMapper.page(request.createMpPage(), applyRequest);
    }

    @Override
    public boolean copyActivityApply(Long applyId) {
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
        ApplyActivity activity = this.lambdaQuery().eq(ApplyActivity::getApplyId, applyId).one();
        if (activity == null) {
            throw new ServiceException("申请不存在，请刷新后操作");
        }
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
        activity.setApplyId(targetApplyId);
        activity.setId(null);
        boolean save = this.save(activity);

        // 拷贝附件
        applyAttachMapper.copyByApplyId(applyId, AttachSectionEnum.APPLY.getCode(), targetApplyId);

        return save;
    }
}

