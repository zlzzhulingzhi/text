package cn.qbs.wa.union.admin.controller;


import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.union.admin.pojo.systemmenu.*;
import cn.qbs.wa.union.admin.service.SystemMenuService;
import cn.qbs.wa.teach.common.core.domain.BatchFlagRequest;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
 * 【系统菜单】(SystemMenu)表控制层
 *
 * @author makejava
 * @since 2022-07-08 09:03:04
 */
@RestController
@RequestMapping("systemMenu")
@Api(tags = "菜单管理")
public class SystemMenuController {

    /**
     * 服务对象
     */
    @Resource
    private SystemMenuService systemMenuService;


    /**
     * 新增【系统菜单】
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    @RequiresPermissions("System:Admin:Menu")
    @ApiOperation("新增")
    public R<Boolean> add(@RequestBody @Validated SystemMenuAddRequest params) {
        return R.ok(this.systemMenuService.add(params));
    }

    /**
     * 分页查询【系统菜单】
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    @RequiresPermissions("System:Admin:Menu")
    @ApiOperation("分页查询")
    public R<IPage<SystemMenuPageResponse>> page(@RequestBody SystemMenuPageRequest params) {
        return R.ok(this.systemMenuService.page(params));
    }

    /**
     * 查询【系统菜单】详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    @RequiresPermissions("System:Admin:Menu")
    @ApiOperation("详情")
    public R<SystemMenuDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.systemMenuService.detail(request.getId()));
    }

    /**
     * 查询【系统菜单】详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("list")
    @RequiresPermissions("System:Admin:Menu")
    @ApiOperation("菜单列表")
    public R<List<SystemMenuDetailResponse>> menuList() {
        return R.ok(this.systemMenuService.menuList());
    }

    @PostMapping("tree")
    @RequiresPermissions("System:Admin:Menu")
    @ApiOperation("菜单树")
    public R<List<SystemMenuTreeResponse>> getTreeList(@RequestBody SystemMenuTreeRequest request) {
        return R.ok(this.systemMenuService.getTreeList(request));
    }

    /**
     * 修改【系统菜单】
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    @RequiresPermissions("System:Admin:Menu")
    @ApiOperation("更新")
    public R<Boolean> update(@RequestBody @Validated SystemMenuUpdateRequest params) {
        return R.ok(this.systemMenuService.update(params));
    }

    /**
     * 删除【系统菜单】
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    @RequiresPermissions("System:Admin:Menu")
    @ApiOperation("删除")
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.systemMenuService.deleteByIds(request.getIdList()));
    }

    /**
     * 批量启用/禁用
     *
     *
     */
    @PostMapping("batch-enabled")
    @RequiresPermissions("System:Admin:Menu")
    @ApiOperation("批量启用/禁用")
    public R batchEnabled(@RequestBody BatchFlagRequest request) {
        this.systemMenuService.batchEnabled(request.getFlag(),request.getIdList());
        return R.ok();
    }

}

