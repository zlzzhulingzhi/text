package cn.qbs.wa.train.allowance.controller.manage;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.train.allowance.entity.ApplyAttach;
import cn.qbs.wa.train.allowance.enums.ApplyTypeEnum;
import cn.qbs.wa.train.allowance.enums.AttachSectionEnum;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyAttachAddRequest;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyAttachResponse;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyUpdateBySettleRequest;
import cn.qbs.wa.train.allowance.service.ApplyAttachService;
import cn.qbs.wa.train.allowance.service.ApplyService;
import cn.qbs.wa.train.allowance.service.ApplySettleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "培训申请表单管理")
@RestController
@RequestMapping("applySettle")
public class ApplySettleController {

    @Resource
    private ApplyService applyService;

    @Resource
    private ApplyAttachService applyAttachService;

    @Resource
    private ApplySettleService applySettleService;

    @ApiOperation("培训申请-删除")
    @PostMapping("/remove")
    @RequiresPermissions("Logistics:Apply")
    public R<Boolean> removeSettleApply(@RequestBody @Validated IdRequest request) {
        // 校验表单状态 未提交、驳回状态的申请单才允许删除
        applyService.checkApplyStatus(request.getId(), true);
        // 使用 逻辑删除 申请单表头
        return R.ok(applyService.removeById(request.getId()));
    }

    @ApiOperation("培训申请-复制")
    @PostMapping("/copy")
    @RequiresPermissions("Logistics:Apply")
    public R<Boolean> copySettleApply(@RequestBody @Validated IdRequest request) {
        return R.ok(this.applySettleService.copySettleApply(request.getId()));
    }

    @ApiOperation("培训申请-修改")
    @PostMapping("update")
    @RequiresPermissions("Logistics:Apply")
    public R<Boolean> update(@RequestBody @Validated ApplyUpdateBySettleRequest params) {
        return R.ok(this.applySettleService.update(params));
    }
    
    @ApiOperation("培训申请附件-列表")
    @PostMapping("/attach/list")
    @RequiresPermissions("Logistics:Apply")
    public R<List<ApplyAttachResponse>> listQualificationAttach(@RequestBody @Validated IdRequest request) {
        return R.ok(applyAttachService.list(ApplyTypeEnum.SETTLE, AttachSectionEnum.APPLY, request.getId()));
    }

    @ApiOperation("培训申请附件-保存")
    @PostMapping("/attach/save")
    @RequiresPermissions("Logistics:Apply")
    public R<List<ApplyAttachResponse>> saveQualificationAttach(@RequestBody @Validated ApplyAttachAddRequest attachRequest) {
        // 校验表单状态
        applyService.checkApplyStatus(attachRequest.getApplyId(), false);
        return R.ok(applyAttachService.saveBatch(ApplyTypeEnum.SETTLE, AttachSectionEnum.APPLY, attachRequest));
    }

    @ApiOperation("培训申请附件-删除")
    @PostMapping("/attach/remove")
    @RequiresPermissions("Logistics:Apply")
    public R<Boolean> removeQualificationAttach(@RequestBody @Validated IdRequest request) {
        ApplyAttach attach = applyAttachService.getById(request.getId());
        if (attach != null) {
            // 校验表单状态
            applyService.checkApplyStatus(attach.getApplyId(), false);
            applyAttachService.removeById(attach.getId());
        }
        return R.ok(Boolean.TRUE);
    }

}

