package cn.qbs.wa.train.logistics.controller;/*
package cn.qbs.wa.train.organization.controller;


import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.organization.pojo.dept.EmployeeDeptListResponse;
import cn.qbs.wa.train.organization.pojo.employeedept.*;
import cn.qbs.wa.train.organization.service.EmployeeDeptService;
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


*/
/**
 * 职工部门表(EmployeeDept)表控制层
 *
 * @author makejava
 * @since 2021-11-09 20:11:35
 *//*

@RestController
@RequestMapping("employeeDept")
@Api(tags = "职工部门管理")
public class EmployeeDeptController {

    */
/**
     * 服务对象
     *//*

    @Resource
    private EmployeeDeptService employeeDeptService;


    */
/**
     * 新增职工部门表
     *
     * @param params
     * @return
     *//*

    @PostMapping("add")
    //@RequiresPermissions("employeeDept:add")
    //@Log(title = "新增职工部门表", businessType = BusinessType.INSERT)
    @ApiOperation("新增职工部门")
    public R<Boolean> add(@RequestBody @Validated EmployeeDeptAddRequest params) {
        return R.ok(this.employeeDeptService.add(params));
    }

    */
/**
     * 分页查询职工部门表
     *
     * @param params
     * @return
     *//*

    @PostMapping("page")
    //@RequiresPermissions("employeeDept:page")
    //@Log(title = "分页查询职工部门表", businessType = BusinessType.OTHER)
    @ApiOperation("分页查询职工部门")
    public R<IPage<EmployeeDeptPageResponse>> page(@RequestBody EmployeeDeptPageRequest params) {
        return R.ok(this.employeeDeptService.page(params));
    }

    */
/**
     * 查询职工部门表详情
     *
     * @param id 主键
     * @return
     *//*

    @PostMapping("detail")
    //@RequiresPermissions("employeeDept:details")
    //@Log(title = "职工部门表详情", businessType = BusinessType.OTHER)
    @ApiOperation("职工部门表详情")
    public R<EmployeeDeptDetailResponse> detail(Long id) {
        return R.ok(this.employeeDeptService.detail(id));
    }

    */
/**
     * 修改职工部门表
     *
     * @param params
     * @return
     *//*

    @PostMapping("update")
    //@RequiresPermissions("employeeDept:update")
    //@Log(title = "更新职工部门表", businessType = BusinessType.UPDATE)
    @ApiOperation("更新职工部门表")
    public R<Boolean> update(@RequestBody @Validated EmployeeDeptUpdateRequest params) {
        return R.ok(this.employeeDeptService.update(params));
    }

    */
/**
     * 删除职工部门表
     *
     * @param idList 主键集合
     * @return
     *//*

    @PostMapping("delete")
    //@RequiresPermissions("employeeDept:delete")
    //@Log(title = "删除职工部门表", businessType = BusinessType.DELETE)
    @ApiOperation("删除职工部门")
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.employeeDeptService.deleteByIds( request.getIdList()));
    }

    */
/**
     * 查询职工部门表
     *
     *//*

    @PostMapping("/list/tree")
    //@RequiresPermissions("employeeDept:list")
    //@Log(title = "查询职工部门", businessType = BusinessType.OTHER)
    @ApiOperation("查询职工部门树形列表")
    public R<List<EmployeeDeptTreeListResponse>> listTree(@RequestBody EmployeeDeptListRequest params) {
        if (params.getOrgId() == null) {
            params.setOrgId(SecurityContextHolder.getOrgId());
        }
        return R.ok(this.employeeDeptService.listTree(params));
    }

    */
/**
     * 查询职工部门表
     *
     *//*

    @PostMapping("/list")
    //@RequiresPermissions("employeeDept:list")
    //@Log(title = "查询职工部门", businessType = BusinessType.OTHER)
    @ApiOperation("查询职工部门列表")
    public R<List<EmployeeDeptListResponse>> list(@RequestBody EmployeeDeptListRequest params) {
        if (params.getOrgId() == null) {
            params.setOrgId(SecurityContextHolder.getOrgId());
        }
        return R.ok(this.employeeDeptService.list(params));
    }

}

*/
