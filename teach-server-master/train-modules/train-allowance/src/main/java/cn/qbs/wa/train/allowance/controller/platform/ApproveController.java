package cn.qbs.wa.train.allowance.controller.platform;

import cn.hutool.core.util.IdUtil;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.redis.service.RedisService;
import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.teach.common.security.annotation.RequiresRoles;
import cn.qbs.wa.teach.out.union.admin.api.RemoteUnionOrgService;
import cn.qbs.wa.train.allowance.entity.Apply;
import cn.qbs.wa.train.allowance.entity.ApplyAttach;
import cn.qbs.wa.train.allowance.enums.ApplyResultEnum;
import cn.qbs.wa.train.allowance.enums.ApplyTypeEnum;
import cn.qbs.wa.train.allowance.enums.AttachSectionEnum;
import cn.qbs.wa.train.allowance.pojo.apply.*;
import cn.qbs.wa.train.allowance.pojo.applyactivity.ApplyActivityDetailResponse;
import cn.qbs.wa.train.allowance.pojo.applyallowance.ApplyDetailByAllowanceResponse;
import cn.qbs.wa.train.allowance.pojo.applyauditexpert.ApplyAuditExpertAddRequest;
import cn.qbs.wa.train.allowance.pojo.applyauditexpert.ApplyAuditExpertDetailResponse;
import cn.qbs.wa.train.allowance.pojo.applyauditexpert.ApplyAuditExpertListRequest;
import cn.qbs.wa.train.allowance.pojo.applyauditexpert.ApplyAuditExpertUpdateRequest;
import cn.qbs.wa.train.allowance.pojo.approve.*;
import cn.qbs.wa.train.allowance.service.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.protobuf.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 审批业务
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-14 15:07:22
 */
@Slf4j
@Api(tags = "审批业务")
@RestController
@RequestMapping("/approve")
public class ApproveController {

    @Resource
    private ApproveService approveService;

    @Resource
    private ApplyQualificationService applyQualificationService;

    @Resource
    private ApplyAttachService applyAttachService;

    @Resource
    private ApplyActivityService applyActivityService;

    @Resource
    private ApplyAllowanceService applyAllowanceService;

    @Resource
    private RedisService redisService;

    @Resource
    private RemoteUnionOrgService remoteUnionOrgService;

    @Resource
    private ApplyAuditExpertService applyAuditExpertService;

    @ApiOperation("机构入驻申请分页")
    @PostMapping("/settle/page")
    public R<IPage<ApplyPageResponse>> pageSettleApply(@RequestBody ApplySettlePageRequest request) {
        request.setApplyType(ApplyTypeEnum.SETTLE.getCode());
        return R.ok(approveService.pageSettleApply(request));
    }

    @ApiOperation("机构入驻申请分页")
    @PostMapping("/settle/pageV2")
    @RequiresPermissions("Management:Classroom:Approve")
    public R<IPage<ApplyPageResponse>> pageV2(@RequestBody ApplyPageRequest request) {
        request.setApplyType(ApplyTypeEnum.SETTLE.getCode());
        return R.ok(approveService.pageV2(request));
    }

    @ApiOperation("申请记录分页")
    @PostMapping("/pageRecord")
    @RequiresPermissions("Management:Apply:Record")
    public R<IPage<ApplyPageResponse>> pageRecord(@RequestBody ApplyPageRequest request) {
        return R.ok(approveService.pageRecord(request));
    }

    @ApiOperation("机构入驻申请作废")
    @PostMapping("/settle/invalidated")
    @RequiresRoles({"plat_manager"})
    public R<Boolean> approveSettleInvalidatedApply(@RequestBody ApproveRequest request)
            throws ServiceException {
        approveService.approveInvalidatedApply(request);
        return R.ok(Boolean.TRUE);
    }

    @ApiOperation("机构入驻申请详情")
    @PostMapping("/settle/detail")
    @RequiresPermissions("Management:Classroom:Approve")
    public R<ApplySettleDetailResponse> detailSettleApply(@RequestBody @Validated IdRequest request) {
        return R.ok(approveService.detailSettleApply(request.getId()));
    }

    @ApiOperation("机构入驻申请审批")
    @PostMapping("/settle")
    @RequiresPermissions("Management:Classroom:Approve")
    public R<Boolean> approveSettleApply(@RequestBody ApproveRequest request)
            throws ServiceException {
        approveService.approveApply(request);
        return R.ok(Boolean.TRUE);
    }

    @ApiOperation("教室预定结果分页")
    @PostMapping("/settle-classroom/page")
    public R<IPage<ApplySettleClassroomPageResponse>> pageSettleClassroomApply(@RequestBody ApplyPageRequest request) {
        return R.ok(approveService.pageSettleClassroomApply(request));
    }

    @ApiOperation("教室预定结果详情")
    @PostMapping("/settle-classroom/detail")
    public R<ApplySettleClassroomDetailResponse> detailSettleClassroomApply(@RequestBody IdRequest request) {
        return R.ok(approveService.detailSettleClassroomApply(request.getId()));
    }

    /**
     * 资质申请-分页
     */
    @ApiOperation("资质申请-分页")
    @PostMapping("/qualification/page")
    @RequiresPermissions("Management:Qualification:Approve")
    public R<IPage<ApplyPageResponse>> pageQualificationApply(@RequestBody ApprovePageRequest request) {
        request.setApplyType(ApplyTypeEnum.QUALIFICATION.getCode());
        return R.ok(approveService.pageApproveApply(request));
    }

    /**
     * 学术活动申请-分页
     */
    @ApiOperation("学术活动申请-分页")
    @PostMapping("/activity/page")
    @RequiresPermissions("Management:Allowance:Apply:Check")
    public R<IPage<ApplyPageResponse>> pageActivityApply(@RequestBody ApprovePageRequest request) {
        request.setApplyType(ApplyTypeEnum.ACTIVITY.getCode());
        return R.ok(approveService.pageApproveApply(request));
    }

    /**
     * 课程申请-分页
     */
    @ApiOperation("课程申请-分页")
    @PostMapping("/allowance/page")
    @RequiresPermissions("Management:Allowance:Apply:Check")
    public R<IPage<ApplyPageResponse>> pageAllowanceApply(@RequestBody ApprovePageRequest request) {
        request.setApplyType(ApplyTypeEnum.ALLOWANCE.getCode());
        return R.ok(approveService.pageApproveApply(request));
    }

    @ApiOperation("资质申请-详情")
    @PostMapping("/qualification/detail")
    @RequiresPermissions("Management:Qualification:Approve")
    public R<QualificationApplyResponse> showQualificationApply(@RequestBody @Validated IdRequest request) {
        return R.ok(applyQualificationService.detail(request.getId()));
    }

    @ApiOperation("学术活动申请-详情")
    @PostMapping("/activity/detail")
    @RequiresPermissions("Management:Allowance:Apply:Check")
    public R<ApplyActivityDetailResponse> showActivityApply(@RequestBody @Validated IdRequest request) {
        return R.ok(applyActivityService.detail(request.getId()));
    }

    @ApiOperation("课程申请-详情")
    @PostMapping("/allowance/detail")
    @RequiresPermissions("Management:Allowance:Apply:Check")
    public R<ApplyDetailByAllowanceResponse> showAllowanceApply(@RequestBody @Validated IdRequest request) {
        return R.ok(applyAllowanceService.detail(request.getId()));
    }

    @ApiOperation("机构入驻申请详情")
    @PostMapping("/settle/attach/list")
    @RequiresPermissions("Management:Classroom:Approve")
    public R<List<ApplyAttachResponse>> listTrainPlaceAttach(@RequestBody @Validated IdRequest request) {
        return R.ok(applyAttachService.list(ApplyTypeEnum.SETTLE, AttachSectionEnum.APPLY, request.getId()));
    }

    @ApiOperation("资质申请附件-列表")
    @PostMapping("/qualification/attach/list")
    @RequiresPermissions("Management:Qualification:Approve")
    public R<List<ApplyAttachResponse>> listQualificationAttach(@RequestBody @Validated IdRequest request) {
        return R.ok(applyAttachService.list(ApplyTypeEnum.QUALIFICATION, AttachSectionEnum.APPLY, request.getId()));
    }

    @ApiOperation("学术活动申请附件-列表")
    @PostMapping("/activity/attach/list")
    @RequiresPermissions("Management:Allowance:Apply:Check")
    public R<List<ApplyAttachResponse>> listActivityAttach(@RequestBody @Validated IdRequest request) {
        return R.ok(applyAttachService.list(ApplyTypeEnum.ACTIVITY, AttachSectionEnum.APPLY, request.getId()));
    }

    @ApiOperation("课程申请附件-列表")
    @PostMapping("/allowance/attach/list")
    @RequiresPermissions("Management:Allowance:Apply:Check")
    public R<List<ApplyAttachResponse>> listAllowanceAttach(@RequestBody @Validated IdRequest request) {
        return R.ok(applyAttachService.list(ApplyTypeEnum.ALLOWANCE, AttachSectionEnum.APPLY, request.getId()));
    }

    /**
     * 资质申请审批功能
     * 表示该机构已入围万人计划机构，后续才拥有的资金申请的权限(train_union -> uni_organization -> plan = 1)
     */
    @ApiOperation("资质申请审批")
    @PostMapping("/qualification")
    @RequiresPermissions("Management:Qualification:Approve")
    public R<ApproveConfirmResponse> approveQualificationApply(@RequestBody ApproveRequest request) throws ServiceException {
        Apply apply = approveService.approveApply(request);
        ApproveConfirmResponse response = new ApproveConfirmResponse();
        response.setIsConfirm(false);
        if (ApplyResultEnum.PASS.getCode().equals(apply.getApplyResult())) {
            String uuid = IdUtil.fastSimpleUUID();
            redisService.setCacheObject("plan:org:" + uuid, apply.getOrgId(), 30L, TimeUnit.MINUTES);
            response.setIsConfirm(true);
            response.setKey(uuid);
        }
        return R.ok(response);
    }

    @ApiOperation("资质申请审批通过-升级万人计划")
    @PostMapping("/qualification/trigger")
    @RequiresPermissions("Management:Qualification:Approve")
    public R<Boolean> approveQualificationTrigger(@RequestBody @Validated ApproveTriggerRequest request) {
        String key = request.getKey();
        Object cacheObject = redisService.getCacheObject("plan:org:" + key);
        if (cacheObject != null) {
            Long orgId = (Long)cacheObject;
            R<Boolean> r = remoteUnionOrgService.becomeToPlan(new IdRequest(orgId));
            if (!r.isOk()) {
                log.error("机构万人计划设置失败: {}", r.getMsg());
            }
            redisService.deleteObject("plan:org:" + key);
        } else {
            return R.fail("key[" + key + "] 不合法");
        }
        return R.ok(true);
    }

    @ApiOperation("学术活动申请审批")
    @PostMapping("/activity")
    @RequiresPermissions("Management:Allowance:Apply:Check")
    public R<Boolean> approveActivityApply(@RequestBody ApproveRequest request) throws ServiceException {
        approveService.approveApply(request);
        return R.ok();
    }

    @ApiOperation("课程申请审批")
    @PostMapping("/allowance")
    @RequiresPermissions("Management:Allowance:Apply:Check")
    public R<Boolean> approveAllowanceApply(@RequestBody ApproveRequest request) throws ServiceException {
        approveService.approveApply(request);
        return R.ok();
    }

    @ApiOperation("查询表单所有节点状态")
    @PostMapping("/applyWorkflowNodes")
    public R<ApproveByWorkflowNodeDTO> applyWorkflowNodes(@RequestBody @Validated IdRequest request) {
        return R.ok(approveService.selectApplyByWorkflowNode(request));
    }

    @ApiOperation("查询资质待审核数")
    @PostMapping("/qualificationCount")
    public R<Long> selectQualificationCount() {
        return R.ok(approveService.selectQualificationCount());
    }

    @ApiOperation("查询课程活动待审核数")
    @PostMapping("/allowanceAndActivityCount")
    public R<AllowanceAndActivityCount> selectAllowanceAndActivityCount() {
        return R.ok(approveService.selectAllowanceAndActivityCount());
    }

    @ApiOperation("查询教室待审核数")
    @PostMapping("/classroomCount")
    public R<Long> selectClassroomCount() {
        return R.ok(approveService.selectClassroomCount());
    }

    @ApiOperation("专家评审-新增")
    @PostMapping("/applyAuditExpert/add")
    public R<Boolean> addApplyAuditExpert(@RequestBody ApplyAuditExpertAddRequest request) {
        return R.ok(applyAuditExpertService.add(request));
    }

    @ApiOperation("专家评审-编辑")
    @PostMapping("/applyAuditExpert/update")
    public R<Boolean> updateApplyAuditExpert(@RequestBody ApplyAuditExpertUpdateRequest request) {
        return R.ok(applyAuditExpertService.update(request));
    }

    @ApiOperation("专家评审-删除·")
    @PostMapping("/applyAuditExpert/delete")
    public R<Boolean> deleteApplyAuditExpert(@RequestBody IdListRequest request) {
        return R.ok(applyAuditExpertService.deleteByIds(request.getIdList()));
    }

    @ApiOperation("专家评审-列表")
    @PostMapping("/applyAuditExpert/list")
    public R<List<ApplyAuditExpertDetailResponse>> listApplyAuditExpert(@RequestBody ApplyAuditExpertListRequest request) {
        return R.ok(applyAuditExpertService.listApplyAuditExpert(request));
    }

    @ApiOperation("专家评审附件-列表")
    @PostMapping("/applyAuditExpert/attach/list")
    public R<List<ApplyAttachResponse>> listApplyAuditExpertAttach(@RequestBody @Validated ApplyExpertAttachListRequest request) {
        return R.ok(applyAttachService.listExpertAttach(AttachSectionEnum.AUDIT, request));
    }

    @ApiOperation("专家评审附件-保存")
    @PostMapping("/applyAuditExpert/attach/save")
    public R<List<ApplyAttachResponse>> saveApplyAuditExpertAttach(@RequestBody @Validated ApplyExpertAttachAddRequest attachRequest) {
        return R.ok(applyAttachService.saveBatchExpertAttach(AttachSectionEnum.AUDIT, attachRequest));
    }

    @ApiOperation("专家评审附件-删除")
    @PostMapping("/applyAuditExpert/attach/remove")
    public R<Boolean> removeApplyAuditExpertAttach(@RequestBody @Validated IdListRequest request) {
        List<ApplyAttach> attachs = applyAttachService.lambdaQuery().in(ApplyAttach::getId,request.getIdList()).list();
        if (CollectionUtils.isNotEmpty(attachs)) {
            applyAttachService.removeByIds(request.getIdList());
        }
        return R.ok(Boolean.TRUE);
    }



}
