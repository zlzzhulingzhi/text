package cn.qbs.wa.union.admin.controller;


import cn.qbs.wa.union.admin.pojo.uniapplicationuser.UniApplicationUserAddRequest;
import cn.qbs.wa.union.admin.pojo.uniapplicationuser.UniApplicationUserDetailResponse;
import cn.qbs.wa.union.admin.pojo.uniapplicationuser.UniApplicationUserMineRequest;
import cn.qbs.wa.union.admin.service.UniApplicationUserService;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 【系统应用-用户】(UniApplicationUser)表控制层
 *
 * @author makejava
 * @since 2022-07-08 09:03:11
 */
@RestController
@RequestMapping("uniApplicationUser")
@Api(tags = "应用分配")
public class UniApplicationUserController {

    /**
     * 服务对象
     */
    @Resource
    private UniApplicationUserService uniApplicationUserService;


    @PostMapping("list/mine")
    @ApiOperation("我的应用列表")
    public R getMineApplication(@RequestBody UniApplicationUserMineRequest request) {
        return R.ok(uniApplicationUserService.getMineApplication(request));
    }


    /**
     * 新增【系统应用-用户】
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    //@RequiresPermissions("uniApplicationUser:add")
    //@Log(title = "新增【系统应用-用户】", businessType = BusinessType.INSERT)
    @ApiOperation("新增")
    public R<Boolean> add(@RequestBody @Validated UniApplicationUserAddRequest params) {
        return R.ok(this.uniApplicationUserService.add(params));
    }

    @PostMapping("list")
    @ApiOperation("列表")
    public R getApplicationList(@RequestBody IdRequest idRequest) {
        return R.ok(uniApplicationUserService.getApplicationList(idRequest));
    }


    /**
     * 查询【系统应用-用户】详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    //@RequiresPermissions("uniApplicationUser:details")
    //@Log(title = "【系统应用-用户】详情", businessType = BusinessType.OTHER)
    public R<UniApplicationUserDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.uniApplicationUserService.detail(request.getId()));
    }


    /**
     * 删除【系统应用-用户】
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    //@RequiresPermissions("uniApplicationUser:delete")
    //@Log(title = "删除【系统应用-用户】", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.uniApplicationUserService.deleteByIds(request.getIdList()));
    }

}

