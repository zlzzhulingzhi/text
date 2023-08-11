package cn.qbs.wa.teach.organization.controller;


import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import cn.qbs.wa.teach.common.security.utils.SecurityUtils;
import cn.qbs.wa.teach.organization.pojo.organizationmenu.*;
import cn.qbs.wa.teach.organization.service.OrganizationMenuService;
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
 * 【角色菜单关联关系】(OrganizationMenu)表控制层
 *
 * @author makejava
 * @since 2021-11-09 20:26:20
 */
@RestController
@RequestMapping("organizationMenu")
@Api(tags = "机构菜单管理")
public class OrganizationMenuController {

    /**
     * 服务对象
     */
    @Resource
    private OrganizationMenuService organizationMenuService;


    /**
     * 新增【角色菜单关联关系】
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    //@RequiresPermissions("organizationMenu:add")
    //@Log(title = "新增【角色菜单关联关系】", businessType = BusinessType.INSERT)
    @ApiOperation("新增角色菜单")
    public R<Boolean> add(@RequestBody @Validated OrganizationMenuAddRequest params) {
        return R.ok(this.organizationMenuService.add(params));
    }

    /**
     * 分页查询【角色菜单关联关系】
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    //@RequiresPermissions("organizationMenu:page")
    //@Log(title = "分页查询【角色菜单关联关系】", businessType = BusinessType.OTHER)
    @ApiOperation("分页查询")
    public R<IPage<OrganizationMenuPageResponse>> page(@RequestBody OrganizationMenuPageRequest params) {
        return R.ok(this.organizationMenuService.page(params));
    }

    /**
     * 查询【角色菜单关联关系】详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    //@RequiresPermissions("organizationMenu:details")
    //@Log(title = "【角色菜单关联关系】详情", businessType = BusinessType.OTHER)
    @ApiOperation("详情")
    public R<OrganizationMenuDetailResponse> detail(Long id) {
        return R.ok(this.organizationMenuService.detail(id));
    }

    /**
     * 修改【角色菜单关联关系】
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    //@RequiresPermissions("organizationMenu:update")
    //@Log(title = "更新【角色菜单关联关系】", businessType = BusinessType.UPDATE)
    @ApiOperation("更新")
    public R<Boolean> update(@RequestBody @Validated OrganizationMenuUpdateRequest params) {
        return R.ok(this.organizationMenuService.update(params));
    }

    /**
     * 删除【角色菜单关联关系】
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    //@RequiresPermissions("organizationMenu:delete")
    //@Log(title = "删除【角色菜单关联关系】", businessType = BusinessType.DELETE)
    @ApiOperation("删除")
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.organizationMenuService.deleteByIds(request.getIdList()));
    }

    @PostMapping("admin/list")
    //@RequiresPermissions("employee:page")
    @Log(title = "管理员菜单列表", businessType = BusinessType.OTHER)
    @ApiOperation("管理员菜单列表")
    public R<List<OrganizationMenuListResponse>> adminList(@RequestBody OrganizationMenuListRequest params) {
        return R.ok(this.organizationMenuService.listOrganizationMenu(params));
    }

    @PostMapping("list")
    //@RequiresPermissions("employee:page")
    @Log(title = "机构管理员菜单列表", businessType = BusinessType.OTHER)
    @ApiOperation("机构管理员菜单列表")
    public R<List<OrganizationMenuListResponse>> list(@RequestBody OrganizationMenuListRequest params) {
        params.setOrgId(SecurityUtils.getLoginUser().getOrgId());
        return R.ok(this.organizationMenuService.listOrganizationMenu(params));
    }

}

