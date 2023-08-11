package cn.qbs.wa.teach.organization.controller;


import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.organization.pojo.organizationrolemenu.*;
import cn.qbs.wa.teach.organization.service.OrganizationRoleMenuService;
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
 * 【角色菜单关联关系】(OrganizationRoleMenu)表控制层
 *
 * @author makejava
 * @since 2021-11-12 08:58:32
 */
@RestController
@RequestMapping("organizationRoleMenu")
@Api(tags = "机构角色菜单管理")
public class OrganizationRoleMenuController {

    /**
     * 服务对象
     */
    @Resource
    private OrganizationRoleMenuService organizationRoleMenuService;


    /**
     * 新增【角色菜单关联关系】
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    //@RequiresPermissions("organizationRoleMenu:add")
    //@Log(title = "新增【角色菜单关联关系】", businessType = BusinessType.INSERT)
    @ApiOperation("新增")
    public R<Boolean> add(@RequestBody @Validated OrganizationRoleMenuAddRequest params) {
        return R.ok(this.organizationRoleMenuService.add(params));
    }

    /**
     * 分页查询【角色菜单关联关系】
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    //@RequiresPermissions("organizationRoleMenu:page")
    //@Log(title = "分页查询【角色菜单关联关系】", businessType = BusinessType.OTHER)
    @ApiOperation("分页查询")
    public R<IPage<OrganizationRoleMenuPageResponse>> page(@RequestBody OrganizationRoleMenuPageRequest params) {
        return R.ok(this.organizationRoleMenuService.page(params));
    }

    /**
     * 查询【角色菜单关联关系】详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    //@RequiresPermissions("organizationRoleMenu:details")
    //@Log(title = "【角色菜单关联关系】详情", businessType = BusinessType.OTHER)
    @ApiOperation("详情")
    public R<OrganizationRoleMenuDetailResponse> detail(Long id) {
        return R.ok(this.organizationRoleMenuService.detail(id));
    }

    /**
     * 修改【角色菜单关联关系】
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    //@RequiresPermissions("organizationRoleMenu:update")
    //@Log(title = "更新【角色菜单关联关系】", businessType = BusinessType.UPDATE)
    @ApiOperation("更新")
    public R<Boolean> update(@RequestBody @Validated OrganizationRoleMenuUpdateRequest params) {
        return R.ok(this.organizationRoleMenuService.update(params));
    }

    /**
     * 删除【角色菜单关联关系】
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    //@RequiresPermissions("organizationRoleMenu:delete")
    //@Log(title = "删除【角色菜单关联关系】", businessType = BusinessType.DELETE)
    @ApiOperation("删除")
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.organizationRoleMenuService.deleteByIds(request.getIdList()));
    }

}

