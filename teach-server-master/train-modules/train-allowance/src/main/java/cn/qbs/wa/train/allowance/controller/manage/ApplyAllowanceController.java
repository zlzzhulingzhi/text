package cn.qbs.wa.train.allowance.controller.manage;


import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.train.allowance.entity.ApplyAttach;
import cn.qbs.wa.train.allowance.enums.ApplyTypeEnum;
import cn.qbs.wa.train.allowance.enums.AttachSectionEnum;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyAttachAddRequest;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyAttachResponse;
import cn.qbs.wa.train.allowance.pojo.applyallowance.ApplyAllowanceSaveRequest;
import cn.qbs.wa.train.allowance.pojo.applyallowance.ApplyDetailByAllowanceResponse;
import cn.qbs.wa.train.allowance.pojo.applyallowance.ApplyUpdateByAllowanceRequest;
import cn.qbs.wa.train.allowance.service.ApplyAllowanceService;
import cn.qbs.wa.train.allowance.service.ApplyAttachService;
import cn.qbs.wa.train.allowance.service.ApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * 资助资金申请-网络安全培训课程(ApplyAllowance)表控制层
 *
 * @author makejava
 * @since 2022-11-03 19:28:57
 */
@Api(tags = "课程申请表单管理")
@RestController
@RequestMapping("applyAllowance")
public class ApplyAllowanceController {


    @Resource
    private ApplyAttachService applyAttachService;

    @Resource
    private ApplyService applyService;

    /**
     * 服务对象
     */
    @Resource
    private ApplyAllowanceService applyAllowanceService;


    /**
     * 新增资助资金申请-网络安全培训课程
     *
     * @param params
     * @return
     */
    @ApiOperation("课程申请-保存")
    @PostMapping("save")
    @RequiresPermissions("Allowance:Apply:Cost")
    //@RequiresPermissions("applyAllowance:add")
    //@Log(title = "新增资助资金申请-网络安全培训课程", businessType = BusinessType.INSERT)
    public R<Long> saveAllowanceApply(@RequestBody @Validated ApplyAllowanceSaveRequest params) {
        return R.ok(this.applyAllowanceService.saveAllowanceApply(params));
    }

    @ApiOperation("课程申请-复制")
    @PostMapping("/copy")
    @RequiresPermissions("Allowance:Apply:Cost")
    public R<Boolean> copyAllowanceApply(@RequestBody @Validated IdRequest request) {
        return R.ok(this.applyAllowanceService.copyAllowanceApply(request.getId()));
    }

    /**
     * 查询资助资金申请-网络安全培训课程详情
     *
     * @param id 主键
     * @return
     */
    @ApiOperation("课程申请-详情")
    @PostMapping("detail")
    @RequiresPermissions("Allowance:Apply:Cost")
    //@RequiresPermissions("applyAllowance:details")
    //@Log(title = "资助资金申请-网络安全培训课程详情", businessType = BusinessType.OTHER)
    public R<ApplyDetailByAllowanceResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.applyAllowanceService.detail(request.getId()));
    }

    /**
     * 修改资助资金申请-网络安全培训课程
     *
     * @param params
     * @return
     */
    @ApiOperation("课程申请-修改")
    @PostMapping("update")
    @RequiresPermissions("Allowance:Apply:Cost")
    //@RequiresPermissions("applyAllowance:update")
    //@Log(title = "更新资助资金申请-网络安全培训课程", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated ApplyUpdateByAllowanceRequest params) {
        return R.ok(this.applyAllowanceService.update(params));
    }

    @ApiOperation("课程申请-删除")
    @PostMapping("/remove")
    @RequiresPermissions("Allowance:Apply:Cost")
    public R<Boolean> removeAllowanceApply(@RequestBody @Validated IdRequest request) {
        // 校验表单状态 未提交、驳回状态的申请单才允许删除
        applyService.checkApplyStatus(request.getId(), true);
        // 使用 逻辑删除 申请单表头
        return R.ok(applyService.removeApply(request.getId()));
    }


    @ApiOperation("课程申请附件-列表")
    @PostMapping("/attach/list")
    @RequiresPermissions("Allowance:Apply:Cost")
    public R<List<ApplyAttachResponse>> listAllowanceAttach(@RequestBody @Validated IdRequest request) {
        return R.ok(applyAttachService.list(ApplyTypeEnum.ALLOWANCE, AttachSectionEnum.APPLY, request.getId()));
    }

    @ApiOperation("课程申请附件-保存")
    @PostMapping("/attach/save")
    @RequiresPermissions("Allowance:Apply:Cost")
    public R<List<ApplyAttachResponse>> saveAllowanceAttach(@RequestBody @Validated ApplyAttachAddRequest attachRequest) {
        // 校验表单状态
        applyService.checkApplyStatus(attachRequest.getApplyId(), false);
        return R.ok(applyAttachService.saveBatch(ApplyTypeEnum.ALLOWANCE, AttachSectionEnum.APPLY, attachRequest));
    }

    @ApiOperation("课程申请附件-删除")
    @PostMapping("/attach/remove")
    @RequiresPermissions("Allowance:Apply:Cost")
    public R<Boolean> removeAllowanceAttach(@RequestBody @Validated IdRequest request) {
        ApplyAttach attach = applyAttachService.getById(request.getId());
        if (attach != null) {
            // 校验表单状态
            applyService.checkApplyStatus(attach.getApplyId(), false);
            applyAttachService.removeById(attach.getId());
        }
        return R.ok(Boolean.TRUE);
    }

}

