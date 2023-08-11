package cn.qbs.wa.teach.basic.controller;


import cn.qbs.wa.teach.basic.entity.Menu;
import cn.qbs.wa.teach.basic.pojo.menu.*;
import cn.qbs.wa.teach.basic.service.MenuService;
import cn.qbs.wa.teach.common.core.domain.BatchFlagRequest;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
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
 * 【系统菜单】(Menu)表控制层
 *
 * @author makejava
 * @since 2021-11-04 16:28:08
 */
@RestController
@RequestMapping("menu")
@Api(tags = "菜单管理")
public class MenuController {

    /**
     * 服务对象
     */
    @Resource
    private MenuService menuService;


    /**
     * 新增【系统菜单】
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    //@RequiresPermissions("menu:add")
    //@Log(title = "新增【系统菜单】", businessType = BusinessType.INSERT)
    @ApiOperation("新增")
    public R<Boolean> add(@RequestBody @Validated MenuAddRequest params) {
        return R.ok(this.menuService.add(params));
    }

    /**
     * 分页查询【系统菜单】
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    //@RequiresPermissions("menu:page")
    //@Log(title = "分页查询【系统菜单】", businessType = BusinessType.OTHER)
    @ApiOperation("分页查询")
    public R<IPage<MenuPageResponse>> page(@RequestBody MenuPageRequest params) {
        return R.ok(this.menuService.page(params));
    }


    @PostMapping("tree-total")
    //@RequiresPermissions("menu:page")
    //@Log(title = "分页查询【系统菜单】", businessType = BusinessType.OTHER)
    @ApiOperation("菜单树汇总")
    public R<TreeMenuTotalResponse> getTreeMenuTotal(@RequestBody TreeMenuTotalRequest request) {
        return R.ok(this.menuService.getTreeMenuTotal(request));
    }

    /**
     * 查询【系统菜单】详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    //@RequiresPermissions("menu:details")
    //@Log(title = "【系统菜单】详情", businessType = BusinessType.OTHER)
    @ApiOperation("菜单详情")
    public R<MenuDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.menuService.detail(request.getId()));
    }

    /**
     * 修改【系统菜单】
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    //@RequiresPermissions("menu:update")
    //@Log(title = "更新【系统菜单】", businessType = BusinessType.UPDATE)
    @ApiOperation("更新")
    public R<Boolean> update(@RequestBody @Validated MenuUpdateRequest params) {
        return R.ok(this.menuService.update(params));
    }

    /**
     * 删除【系统菜单】
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    //@RequiresPermissions("menu:delete")
    //@Log(title = "删除【系统菜单】", businessType = BusinessType.DELETE)
    @ApiOperation("删除")
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.menuService.deleteByIds(request.getIdList()));
    }


    /**
     *  列表查询所有
     *
     * @param params
     * @return
     */
    @PostMapping("list")
    //@RequiresPermissions("menu:page")
    //@Log(title = "分页查询【系统菜单】", businessType = BusinessType.OTHER)
    @ApiOperation("列表")
    public R<List<Menu>> listMenu(@RequestBody MenuListRequest params) {
        return R.ok(this.menuService.listMenu(params));
    }

    /**
     * 批量启用/禁用
     *
     *
     */
    @PostMapping("batch-enabled")
    //@RequiresPermissions("role:delete")
    @Log(title = "批量启用/禁用】", businessType = BusinessType.OTHER)
    @ApiOperation("批量启用/禁用")
    public R batchEnabled(@RequestBody BatchFlagRequest request) {
        this.menuService.batchEnabled(request.getFlag(),request.getIdList());
        return R.ok();
    }


}

