package cn.qbs.wa.teach.organization.controller;


import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.BatchFlagRequest;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import cn.qbs.wa.teach.common.security.utils.SecurityUtils;
import cn.qbs.wa.teach.organization.entity.OrganizationRole;
import cn.qbs.wa.teach.organization.pojo.organizationrole.*;
import cn.qbs.wa.teach.organization.pojo.orgbackcoupon.OrgDeptOrRoleResponseDTO;
import cn.qbs.wa.teach.organization.service.EmployeeRoleService;
import cn.qbs.wa.teach.organization.service.OrganizationRoleService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Comparator;
import java.util.List;


/**
 * 【机构角色】(OrganizationRole)表控制层
 *
 * @author makejava
 * @since 2021-11-10 08:42:58
 */
@RestController
@RequestMapping("organizationRole")
@Api(tags = "机构角色管理")
public class OrganizationRoleController {

    /**
     * 服务对象
     */
    @Resource
    private OrganizationRoleService organizationRoleService;

    @Resource
    private EmployeeRoleService employeeRoleService;

    /**
     * 新增【机构角色】
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    //@RequiresPermissions("organizationRole:add")
    //@Log(title = "新增【机构角色】", businessType = BusinessType.INSERT)
    @ApiOperation("新增")
    public R<Boolean> add(@RequestBody @Validated OrganizationRoleAddRequest params) {
        return R.ok(this.organizationRoleService.add(params));
    }

    /**
     * 分页查询【机构角色】
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    //@RequiresPermissions("organizationRole:page")
    //@Log(title = "分页查询【机构角色】", businessType = BusinessType.OTHER)
    @ApiOperation("分页查询")
    public R<IPage<OrganizationRolePageResponse>> page(@RequestBody OrganizationRolePageRequest params) {
        return R.ok(this.organizationRoleService.page(params));
    }



    /**
     * 查询【机构角色】详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    //@RequiresPermissions("organizationRole:details")
    //@Log(title = "【机构角色】详情", businessType = BusinessType.OTHER)
    @ApiOperation("详情")
    public R<OrganizationRoleDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.organizationRoleService.detail(request.getId()));
    }

    /**
     * 修改【机构角色】
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    //@RequiresPermissions("organizationRole:update")
    //@Log(title = "更新【机构角色】", businessType = BusinessType.UPDATE)
    @ApiOperation("更新")
    public R<Boolean> update(@RequestBody @Validated OrganizationRoleUpdateRequest params) {
        return R.ok(this.organizationRoleService.update(params));
    }

    /**
     * 删除【机构角色】
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    //@RequiresPermissions("organizationRole:delete")
    //@Log(title = "删除【机构角色】", businessType = BusinessType.DELETE)
    @ApiOperation("删除")
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.organizationRoleService.deleteByIds(request.getIdList()));
    }


    @PostMapping("admin/list")
    //@RequiresPermissions("employee:page")
    @Log(title = "管理员角色列表", businessType = BusinessType.OTHER)
    @ApiOperation("管理员角色列表")
    public R<List<OrganizationRolePageResponse>> adminList(@RequestBody OrganizationRoleListRequest params) {
        return R.ok(this.organizationRoleService.listRole(params));
    }

    @PostMapping("list")
    //@RequiresPermissions("employee:page")
    @Log(title = "机构管理员角色列表", businessType = BusinessType.OTHER)
    @ApiOperation("机构管理员角色列表")
    public R<List<OrganizationRolePageResponse>> list(@RequestBody OrganizationRoleListRequest params) {
        params.setOrgId(SecurityContextHolder.getOrgId());
        List<OrganizationRolePageResponse> list = this.organizationRoleService.listRole(params);
        if (CollUtil.isNotEmpty(list)) {
            // 不能选择比当前操作人员角色高的角色
            List<OrganizationRole> roles = employeeRoleService.getRole(SecurityContextHolder.getEmployeeId());
            if (CollUtil.isNotEmpty(roles)) {
                Integer priority = roles.stream().map(OrganizationRole::getPriority).distinct().max(Comparator.naturalOrder()).get();
                list.removeIf(e -> e.getPriority().compareTo(priority) > 0);
            }

        }
        return R.ok(list);
    }

    /**
     * 批量启用/禁用
     */
    @PostMapping("batch-enabled")
    //@RequiresPermissions("role:delete")
    @Log(title = "批量启用/禁用】", businessType = BusinessType.OTHER)
    @ApiOperation("批量启用/禁用")
    public R batchEnabled(@RequestBody BatchFlagRequest request) {
        this.organizationRoleService.batchEnabled(request.getFlag(), request.getIdList());
        return R.ok();
    }

    /**
     * 机构后台获取角色
     *
     * @param id 机构id
     * @return
     */
    @PostMapping("getOrgRole")
    @ApiOperation("获取角色")
    public R<List<OrgDeptOrRoleResponseDTO>> getOrgRole(@RequestBody Serializable id) {
        return R.ok(this.organizationRoleService.getOrgRole(id));
    }
}

