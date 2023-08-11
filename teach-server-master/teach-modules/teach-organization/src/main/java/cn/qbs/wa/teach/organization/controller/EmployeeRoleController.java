package cn.qbs.wa.teach.organization.controller;


import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.organization.pojo.employeerole.*;
import cn.qbs.wa.teach.organization.service.EmployeeRoleService;
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
 * 【职工角色关联关系】(EmployeeRole)表控制层
 *
 * @author makejava
 * @since 2021-11-09 20:12:51
 */
@RestController
@RequestMapping("employeeRole")
@Api(tags = "职工角色管理")
public class EmployeeRoleController {

    /**
     * 服务对象
     */
    @Resource
    private EmployeeRoleService employeeRoleService;


    /**
     * 新增【职工角色关联关系】
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    //@RequiresPermissions("employeeRole:add")
    //@Log(title = "新增【职工角色关联关系】", businessType = BusinessType.INSERT)
    @ApiOperation("新增")
    public R<Boolean> add(@RequestBody @Validated EmployeeRoleAddRequest params) {
        return R.ok(this.employeeRoleService.add(params));
    }

    /**
     * 分页查询【职工角色关联关系】
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    //@RequiresPermissions("employeeRole:page")
    //@Log(title = "分页查询【职工角色关联关系】", businessType = BusinessType.OTHER)
    @ApiOperation("分页查询")
    public R<IPage<EmployeeRolePageResponse>> page(@RequestBody EmployeeRolePageRequest params) {
        return R.ok(this.employeeRoleService.page(params));
    }



    /**
     * 查询【职工角色关联关系】详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    //@RequiresPermissions("employeeRole:details")
    //@Log(title = "【职工角色关联关系】详情", businessType = BusinessType.OTHER)
    @ApiOperation("详情")
    public R<EmployeeRoleDetailResponse> detail(Long id) {
        return R.ok(this.employeeRoleService.detail(id));
    }

    /**
     * 修改【职工角色关联关系】
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    //@RequiresPermissions("employeeRole:update")
    //@Log(title = "更新【职工角色关联关系】", businessType = BusinessType.UPDATE)
    @ApiOperation("更新")
    public R<Boolean> update(@RequestBody @Validated EmployeeRoleUpdateRequest params) {
        return R.ok(this.employeeRoleService.update(params));
    }

    /**
     * 删除【职工角色关联关系】
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    //@RequiresPermissions("employeeRole:delete")
    //@Log(title = "删除【职工角色关联关系】", businessType = BusinessType.DELETE)
    @ApiOperation("删除")
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.employeeRoleService.deleteByIds(request.getIdList()));
    }

}

