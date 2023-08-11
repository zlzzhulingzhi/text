package cn.qbs.wa.train.allowance.controller.manage;

import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.train.allowance.config.RedisCacheKey;
import cn.qbs.wa.train.allowance.entity.ApplyAttach;
import cn.qbs.wa.train.allowance.entity.ApplyQualification;
import cn.qbs.wa.train.allowance.entity.ApplyQualificationProject;
import cn.qbs.wa.train.allowance.enums.ApplyTypeEnum;
import cn.qbs.wa.train.allowance.enums.AttachSectionEnum;
import cn.qbs.wa.train.allowance.pojo.apply.*;
import cn.qbs.wa.train.allowance.service.ApplyAttachService;
import cn.qbs.wa.train.allowance.service.ApplyQualificationProjectService;
import cn.qbs.wa.train.allowance.service.ApplyQualificationService;
import cn.qbs.wa.train.allowance.service.ApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yjx
 */
@Api(tags = "资质申请表单管理")
@RestController
@RequestMapping("/qualification")
public class ApplyQualificationController {

    @Resource
    private ApplyQualificationService applyQualificationService;

    @Resource
    private ApplyQualificationProjectService qualificationProjectService;

    @Resource
    private ApplyAttachService applyAttachService;

    @Resource
    private ApplyService applyService;

    @Resource
    private RedissonClient redissonClient;

    @ApiOperation("资质申请-申请通过确认")
    @PostMapping("/check")
    @RequiresPermissions("Allowance:Apply:Qualifications")
    public R<Boolean> check() {
        return R.ok(applyQualificationService.checkPassStatus());
    }

    @ApiOperation("资质申请-保存")
    @PostMapping("/save")
    @RequiresPermissions("Allowance:Apply:Qualifications")
    public R<ApplyQualification> saveQualificationApply(@RequestBody QualificationApplyRequest saveRequest) {
        // 手机号加密
        saveRequest.setContactNumber(AesUtil.encode(saveRequest.getContactNumber()));
        saveRequest.setLegalNumber(AesUtil.encode(saveRequest.getLegalNumber()));
        Long orgId = SecurityContextHolder.getOrgId();
        RLock lock = redissonClient.getLock(RedisCacheKey.getQualificationLockKey(orgId));
        if (lock.tryLock()) {
            try {
                // 资质申请 只允许提交一个进行审核的单，未完成 或者 通过后，则不允许申请了
                applyQualificationService.checkUniqueness(orgId);
                saveRequest.setOrgId(orgId);
                ApplyQualification qualification = applyQualificationService.save(saveRequest);
                return R.ok(qualification);
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }
        return R.ok();
    }

    @ApiOperation("资质申请-复制")
    @PostMapping("/copy")
    @RequiresPermissions("Allowance:Apply:Qualifications")
    public R<ApplyQualification> copyQualificationApply(@RequestBody @Validated IdRequest request) {
        Long orgId = SecurityContextHolder.getOrgId();
        // 资质申请 只允许提交一个进行审核的单，未完成 或者 通过后，则不允许申请了
        applyQualificationService.checkUniqueness(orgId);
        return R.ok(applyQualificationService.copy(request.getId()));
    }

    @ApiOperation("资质申请-详情")
    @PostMapping("/detail")
    @RequiresPermissions("Allowance:Apply:Qualifications")
    public R<QualificationApplyResponse> showQualificationApply(@RequestBody @Validated IdRequest request) {
        return R.ok(applyQualificationService.detail(request.getId()));
    }

    @ApiOperation("资质申请-编辑")
    @PostMapping("/update")
    @RequiresPermissions("Allowance:Apply:Qualifications")
    public R<Boolean> updateQualificationApply(@RequestBody QualificationUpdateRequest updateRequest) {
        // 手机号加密
        updateRequest.setContactNumber(AesUtil.encode(updateRequest.getContactNumber()));
        updateRequest.setLegalNumber(AesUtil.encode(updateRequest.getLegalNumber()));
        // 校验表单状态
        applyService.checkApplyStatus(updateRequest.getApplyId(), false);
        updateRequest.setOrgId(SecurityContextHolder.getOrgId());
        return R.ok(applyQualificationService.update(updateRequest));
    }

    @ApiOperation("资质申请-删除")
    @PostMapping("/remove")
    @RequiresPermissions("Allowance:Apply:Qualifications")
    public R<Boolean> removeQualificationApply(@RequestBody @Validated IdRequest request) {
        // 校验表单状态 未提交、驳回状态的申请单才允许删除
        applyService.checkApplyStatus(request.getId(), true);
        // 使用 逻辑删除 申请单表头
        return R.ok(applyService.removeById(request.getId()));
    }

    @ApiOperation("资质申请附件-列表")
    @PostMapping("/attach/list")
    @RequiresPermissions("Allowance:Apply:Qualifications")
    public R<List<ApplyAttachResponse>> listQualificationAttach(@RequestBody @Validated IdRequest request) {
        return R.ok(applyAttachService.list(ApplyTypeEnum.QUALIFICATION, AttachSectionEnum.APPLY, request.getId()));
    }

    @ApiOperation("资质申请附件-保存")
    @PostMapping("/attach/save")
    @RequiresPermissions("Allowance:Apply:Qualifications")
    public R<List<ApplyAttachResponse>> saveQualificationAttach(@RequestBody @Validated ApplyAttachAddRequest attachRequest) {
        // 校验表单状态
        applyService.checkApplyStatus(attachRequest.getApplyId(), false);
        return R.ok(applyAttachService.saveBatch(ApplyTypeEnum.QUALIFICATION, AttachSectionEnum.APPLY, attachRequest));
    }

    @ApiOperation("资质申请附件-删除")
    @PostMapping("/attach/remove")
    @RequiresPermissions("Allowance:Apply:Qualifications")
    public R<Boolean> removeQualificationAttach(@RequestBody @Validated IdRequest request) {
        ApplyAttach attach = applyAttachService.getById(request.getId());
        if (attach != null) {
            // 校验表单状态
            applyService.checkApplyStatus(attach.getApplyId(), false);
            applyAttachService.removeById(attach.getId());
        }
        return R.ok(Boolean.TRUE);
    }

    @ApiOperation("资质申请-项目计划-新增")
    @PostMapping("/project/add")
    @RequiresPermissions("Allowance:Apply:Qualifications")
    public R<ApplyQualificationProject> addQualificationProject(@RequestBody QualificationProjectAddRequest request) {
        // 校验表单状态
        applyService.checkApplyStatus(request.getApplyId(), false);
        ApplyQualification qualification = applyQualificationService.getById(request.getQualificationId());
        if (qualification == null) {
            throw new ServiceException("资质申请不存在");
        }
        Long orgId = SecurityContextHolder.getOrgId();
        if (!qualification.getApplyId().equals(request.getApplyId()) || !orgId.equals(qualification.getOrgId())) {
            throw new ServiceException("非法操作");
        }
        return R.ok(qualificationProjectService.add(request));
    }

    @ApiOperation("资质申请-项目计划-编辑")
    @PostMapping("/project/update")
    @RequiresPermissions("Allowance:Apply:Qualifications")
    public R<Boolean> updateQualificationProject(@RequestBody QualificationProjectUpdateRequest request) {
        // 校验表单状态
        applyService.checkApplyStatus(request.getApplyId(), false);
        return R.ok(qualificationProjectService.update(request));
    }

    @ApiOperation("资质申请-项目计划-移除")
    @PostMapping("/project/remove")
    @RequiresPermissions("Allowance:Apply:Qualifications")
    public R<Boolean> removeQualificationProject(@RequestBody @Validated QualificationProjectRemoveRequest request) {
        // 校验表单状态
        applyService.checkApplyStatus(request.getApplyId(), false);
        return R.ok(qualificationProjectService.remove(request.getProjectId()));
    }

}
