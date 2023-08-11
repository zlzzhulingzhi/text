package cn.qbs.wa.train.allowance.controller.manage;


import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.train.allowance.entity.ApplyAttach;
import cn.qbs.wa.train.allowance.enums.ApplyTypeEnum;
import cn.qbs.wa.train.allowance.enums.AttachSectionEnum;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyAttachAddRequest;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyAttachResponse;
import cn.qbs.wa.train.allowance.pojo.applyactivity.ApplyActivityDetailResponse;
import cn.qbs.wa.train.allowance.pojo.applyactivity.ApplyActivitySaveRequest;
import cn.qbs.wa.train.allowance.pojo.applyactivity.ApplyActivityUpdateRequest;
import cn.qbs.wa.train.allowance.service.ApplyActivityService;
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
 * 资助资金申请-学术会议和竞赛活动(ApplyActivity)表控制层
 *
 * @author makejava
 * @since 2022-11-03 19:27:15
 */
@Api(tags = "学术活动申请表单管理")
@RestController
@RequestMapping("applyActivity")
public class ApplyActivityController {

    @Resource
    private ApplyAttachService applyAttachService;

    @Resource
    private ApplyService applyService;

    /**
     * 服务对象
     */
    @Resource
    private ApplyActivityService applyActivityService;


    /**
     * 新增资助资金申请-学术会议和竞赛活动
     *
     * @param params
     * @return
     */
    @ApiOperation("学术活动申请-保存")
    @PostMapping("save")
    @RequiresPermissions("Allowance:Apply:Cost")
    //@RequiresPermissions("applyActivity:add")
    //@Log(title = "新增资助资金申请-学术会议和竞赛活动", businessType = BusinessType.INSERT)
    public R<Long> saveActivityApply(@RequestBody @Validated ApplyActivitySaveRequest params) {
        return R.ok(this.applyActivityService.saveActivityApply(params));
    }

    @ApiOperation("学术活动申请-复制")
    @PostMapping("/copy")
    @RequiresPermissions("Allowance:Apply:Cost")
    public R<Boolean> copyActivityApply(@RequestBody @Validated IdRequest request) {
        return R.ok(this.applyActivityService.copyActivityApply(request.getId()));
    }

    @ApiOperation("学术活动申请附件-列表")
    @PostMapping("/attach/list")
    @RequiresPermissions("Allowance:Apply:Cost")
    public R<List<ApplyAttachResponse>> listActivityAttach(@RequestBody @Validated IdRequest request) {
        return R.ok(applyAttachService.list(ApplyTypeEnum.ACTIVITY, AttachSectionEnum.APPLY, request.getId()));
    }

    @ApiOperation("学术活动申请附件-保存")
    @PostMapping("/attach/save")
    @RequiresPermissions("Allowance:Apply:Cost")
    public R<List<ApplyAttachResponse>> saveActivityAttach(@RequestBody @Validated ApplyAttachAddRequest attachRequest) {
        // 校验表单状态
        applyService.checkApplyStatus(attachRequest.getApplyId(), false);
        return R.ok(applyAttachService.saveBatch(ApplyTypeEnum.ACTIVITY, AttachSectionEnum.APPLY, attachRequest));
    }

    @ApiOperation("学术活动申请附件-删除")
    @PostMapping("/attach/remove")
    @RequiresPermissions("Allowance:Apply:Cost")
    public R<Boolean> removeActivityAttach(@RequestBody @Validated IdRequest request) {
        ApplyAttach attach = applyAttachService.getById(request.getId());
        if (attach != null) {
            // 校验表单状态
            applyService.checkApplyStatus(attach.getApplyId(), false);
            applyAttachService.removeById(attach.getId());
        }
        return R.ok(Boolean.TRUE);
    }


//    /**
//     * 分页查询资助资金申请-学术会议和竞赛活动
//     *
//     * @param params
//     * @return
//     */
//    @PostMapping("page")
//    //@RequiresPermissions("applyActivity:page")
//    //@Log(title = "分页查询资助资金申请-学术会议和竞赛活动", businessType = BusinessType.OTHER)
//    public R<IPage<ApplyActivityPageResponse>> page(@RequestBody ApplyActivityPageRequest params) {
//        return R.ok(this.applyActivityService.page(params));
//    }

    /**
     * 查询资助资金申请-学术会议和竞赛活动详情
     *
     * @param id 主键
     * @return
     */
    @ApiOperation("学术活动申请-详情")
    @PostMapping("detail")
    @RequiresPermissions("Allowance:Apply:Cost")
    //@RequiresPermissions("applyActivity:details")
    //@Log(title = "资助资金申请-学术会议和竞赛活动详情", businessType = BusinessType.OTHER)
    public R<ApplyActivityDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.applyActivityService.detail(request.getId()));
    }

    /**
     * 修改资助资金申请-学术会议和竞赛活动
     *
     * @param params
     * @return
     */
    @ApiOperation("学术活动申请-修改")
    @PostMapping("update")
    @RequiresPermissions("Allowance:Apply:Cost")
    //@RequiresPermissions("applyActivity:update")
    //@Log(title = "更新资助资金申请-学术会议和竞赛活动", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated ApplyActivityUpdateRequest params) {
        return R.ok(this.applyActivityService.update(params));
    }

    @ApiOperation("学术会议和竞赛活动申请-删除")
    @PostMapping("/remove")
    @RequiresPermissions("Allowance:Apply:Cost")
    public R<Boolean> removeActivityApply(@RequestBody @Validated IdRequest request) {
        // 校验表单状态 未提交、驳回状态的申请单才允许删除
        applyService.checkApplyStatus(request.getId(), true);
        // 使用 逻辑删除 申请单表头
        return R.ok(applyService.removeApply(request.getId()));
    }


}

