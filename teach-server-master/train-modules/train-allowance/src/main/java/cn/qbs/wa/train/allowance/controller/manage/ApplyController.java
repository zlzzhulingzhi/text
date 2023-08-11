package cn.qbs.wa.train.allowance.controller.manage;

import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.train.allowance.entity.ApplyAttach;
import cn.qbs.wa.train.allowance.enums.ApplyTypeEnum;
import cn.qbs.wa.train.allowance.enums.AttachSectionEnum;
import cn.qbs.wa.train.allowance.pojo.apply.*;
import cn.qbs.wa.train.allowance.pojo.applyactivity.ApplyActivityPageRequest;
import cn.qbs.wa.train.allowance.pojo.applyactivity.ApplyActivityPageResponse;
import cn.qbs.wa.train.allowance.pojo.applyallowance.ApplyAllowancePageRequest;
import cn.qbs.wa.train.allowance.pojo.applyallowance.ApplyAllowancePageResponse;
import cn.qbs.wa.train.allowance.pojo.approve.ApplySettleDetailResponse;
import cn.qbs.wa.train.allowance.pojo.approve.ApplySettlePageRequest;
import cn.qbs.wa.train.allowance.service.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 申请表(Apply)表控制层
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-14 15:07:22
 */
@Api(tags = "申请业务")
@RestController
@RequestMapping("/apply")
public class ApplyController {

    @Resource
    private ApplyService applyService;

    @Resource
    private ApproveService approveService;

    @Resource
    private ApplyQualificationService applyQualificationService;

    @Resource
    private ApplyActivityService applyActivityService;

    @Resource
    private ApplyAllowanceService applyAllowanceService;

    @Resource
    private ApplyAttachService applyAttachService;

    @Resource
    private ApplySettleService applySettleService;

    @ApiOperation("培训申请-新增")
    @PostMapping("/settle-add")
    @RequiresPermissions("Logistics:Apply")
    public R<Long> settleAdd(@RequestBody ApplyAddRequest request) {
        return R.ok(applyService.settleAdd(request));
    }

    @ApiOperation("培训申请-提交")
    @PostMapping("/settle/submit")
    @RequiresPermissions("Logistics:Apply")
    public R<Boolean> submitSettleApply(@RequestBody @Validated IdRequest request) {
        // 检查教室宿舍相关数据
        applySettleService.checkCompleteness(request.getId());
        // 检查教室是否被占用
        applySettleService.checkSettleOccupy(request.getId());
        checkApplyAttach(request.getId());
        return R.ok(applyService.submit(request.getId()));
    }

    @ApiOperation("培训申请-分页")
    @PostMapping("/settle-page")
    @RequiresPermissions("Logistics:Apply")
    public R<IPage<ApplyPageResponse>> pageSettleApply(@RequestBody ApplySettlePageRequest request) {
        return R.ok(applyService.pageSettleApply(request));
    }

    @ApiOperation("培训申请-详情")
    @PostMapping("/settle-detail")
    @RequiresPermissions("Logistics:Apply")
    public R<ApplySettleDetailResponse> detailSettleApply(@RequestBody @Validated IdRequest request) {
        return R.ok(approveService.detailSettleApply(request.getId()));
    }

    @ApiOperation("资质申请-分页")
    @PostMapping("/qualification/page")
    @RequiresPermissions("Allowance:Apply:Qualifications")
    public R<IPage<QualificationPageResponse>> pageQualificationApply(@RequestBody QualificationPageRequest request) {
        ApplyRequest applyRequest = new ApplyRequest();
        applyRequest.setApplyType(ApplyTypeEnum.QUALIFICATION.getCode());
        applyRequest.setOrgId(SecurityContextHolder.getOrgId());
        IPage<ApplyPageResponse> page = applyService.page(request.createMpPage(), applyRequest);
        return R.ok(page.convert(e -> BeanUtil.copyProperties(e, QualificationPageResponse.class)));
    }

    @ApiOperation("学术活动申请-分页")
    @PostMapping("/activity/page")
    @RequiresPermissions("Allowance:Apply:Cost")
    public R<IPage<ApplyActivityPageResponse>> pageActivityApply(@RequestBody ApplyActivityPageRequest request) {
        ApplyRequest applyRequest = new ApplyRequest();
        applyRequest.setApplyType(ApplyTypeEnum.ACTIVITY.getCode());
        applyRequest.setOrgId(SecurityContextHolder.getOrgId());
        IPage<ApplyPageResponse> page = applyActivityService.page(request, applyRequest);
        page = applyService.selectCreateBy(page);
        return R.ok(page.convert(e -> BeanUtil.copyProperties(e, ApplyActivityPageResponse.class)));
    }

    @ApiOperation("课程申请-分页")
    @PostMapping("/allowance/page")
    @RequiresPermissions("Allowance:Apply:Cost")
    public R<IPage<ApplyAllowancePageResponse>> pageAllowanceApply(@RequestBody ApplyAllowancePageRequest request) {
        ApplyRequest applyRequest = new ApplyRequest();
        applyRequest.setApplyType(ApplyTypeEnum.ALLOWANCE.getCode());
        applyRequest.setOrgId(SecurityContextHolder.getOrgId());
        IPage<ApplyPageResponse> page = applyAllowanceService.page(request, applyRequest);
        page = applyService.selectCreateBy(page);
        return R.ok(page.convert(e -> BeanUtil.copyProperties(e, ApplyAllowancePageResponse.class)));
    }

    @ApiOperation("资质申请-提交")
    @PostMapping("/qualification/submit")
    @RequiresPermissions("Allowance:Apply:Qualifications")
    public R<Boolean> submitQualificationApply(@RequestBody @Validated IdRequest request) {
        // 检查项目信息 以及 项目下的课程信息是否完整
        applyQualificationService.checkCompleteness(request.getId());
        checkApplyAttach(request.getId());
        return R.ok(applyService.submit(request.getId()));
    }

    @ApiOperation("学术活动申请-提交")
    @PostMapping("/activity/submit")
    @RequiresPermissions("Allowance:Apply:Cost")
    public R<Boolean> submitActivityApply(@RequestBody @Validated IdRequest request) {
        // 检查学术活动数据
        applyActivityService.checkCompleteness(request.getId());
        checkApplyAttach(request.getId());
        return R.ok(applyService.submit(request.getId()));
    }

    @ApiOperation("课程申请-提交")
    @PostMapping("/allowance/submit")
    @RequiresPermissions("Allowance:Apply:Cost")
    public R<Boolean> submitAllowanceApply(@RequestBody @Validated IdRequest request) {
        // 检查课程相关数据
        applyAllowanceService.checkCompleteness(request.getId());
        checkApplyAttach(request.getId());
        return R.ok(applyService.submit(request.getId()));
    }

    @ApiOperation("查询表单所有节点状态")
    @PostMapping("/applyWorkflowNodes")
    public R<ApplyByWorkflowNodeDTO> applyWorkflowNodes(@RequestBody @Validated IdRequest request) {
        return R.ok(applyService.selectApplyByWorkflowNode(request));
    }

    private void checkApplyAttach(Long applyId) {
        Long count = applyAttachService.lambdaQuery()
                .eq(ApplyAttach::getApplyId, applyId)
                .eq(ApplyAttach::getSection, AttachSectionEnum.APPLY.getCode())
                .count();
        if (count == null || count == 0) {
            throw new ServiceException("请上传申请相关的资料附件信息");
        }
    }

}
