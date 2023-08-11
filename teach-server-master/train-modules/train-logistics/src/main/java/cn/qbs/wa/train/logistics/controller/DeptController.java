package cn.qbs.wa.train.logistics.controller;/*
package cn.qbs.wa.train.organization.controller;


import cn.qbs.wa.teach.common.core.domain.*;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import cn.qbs.wa.teach.common.security.utils.SecurityUtils;
import cn.qbs.wa.train.organization.pojo.dept.*;
import cn.qbs.wa.train.organization.pojo.orgbackcoupon.OrgDeptOrRoleResponseDTO;
import cn.qbs.wa.train.organization.service.DeptService;
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
 * 部门表(Dept)表控制层
 *
 * @author makejava
 * @since 2021-11-10 09:54:50
 *//*

@RestController
@RequestMapping("dept")
@Api(tags = "部门管理")
public class DeptController {

    */
/**
     * 服务对象
     *//*

    @Resource
    private DeptService deptService;


    */
/**
     * 新增部门表
     *
     * @param params
     * @return
     *//*

    @PostMapping("add")
    //@RequiresPermissions("dept:add")
    //@Log(title = "新增部门表", businessType = BusinessType.INSERT)
    @ApiOperation("新增部门")
    public R<Boolean> add(@RequestBody @Validated DeptAddRequest params) {
        return R.ok(this.deptService.add(params));
    }

    */
/**
     * 分页查询部门表
     *
     * @param params
     * @return
     *//*

    @PostMapping("page")
    //@RequiresPermissions("dept:page")
    //@Log(title = "分页查询部门表", businessType = BusinessType.OTHER)
    @ApiOperation("分页查询部门")
    public R<IPage<DeptPageResponse>> page(@RequestBody DeptPageRequest params) {
        return R.ok(this.deptService.page(params));
    }

    */
/**
     * 查询部门表详情
     *
     * @param
     * @return
     *//*

    @PostMapping("detail")
    //@RequiresPermissions("dept:details")
    //@Log(title = "部门表详情", businessType = BusinessType.OTHER)
    @ApiOperation("部门详情")
    public R<DeptDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.deptService.detail(request.getId()));
    }

    */
/**
     * 查询部门表详情
     *
     * @param
     * @return
     *//*

    @PostMapping("detailList")
    //@RequiresPermissions("dept:details")
    //@Log(title = "部门表详情", businessType = BusinessType.OTHER)
    @ApiOperation("部门详情集合")
    public R<List<DeptDetailResponse>> detailList(@RequestBody IdListRequest request) {
        return R.ok(this.deptService.detailList(request.getIdList()));
    }

    */
/**
     * 修改部门表
     *
     * @param params
     * @return
     *//*

    @PostMapping("update")
    //@RequiresPermissions("dept:update")
    //@Log(title = "更新部门表", businessType = BusinessType.UPDATE)
    @ApiOperation("部门更新")
    public R<Boolean> update(@RequestBody @Validated DeptUpdateRequest params) {
        return R.ok(this.deptService.update(params));
    }

    */
/**
     * 删除部门表
     *
     * @param
     * @return
     *//*

    @PostMapping("delete")
    //@RequiresPermissions("dept:delete")
    //@Log(title = "删除部门表", businessType = BusinessType.DELETE)
    @ApiOperation("部门删除")
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.deptService.deleteByIds(request.getIdList()));
    }


    */
/**
     * 查询所有数据
     *
     * @return 所有数据
     *//*

    @PostMapping("tree-list")
    //@RequiresPermissions("application:list")
    @Log(title = "机构管理员部门树形列表显示", businessType = BusinessType.OTHER)
    @ApiOperation("机构管理员部门树形列表显示")
    public R<TreeDeptTotalResponse> treeList(@RequestBody DeptListRequest params) {
        return R.ok(this.deptService.treeListByOrgId(SecurityUtils.getLoginUser().getOrgId(), params));
    }


    */
/**
     * 查询所有数据
     *
     * @return 所有数据
     *//*

    @PostMapping("admin/tree-list")
    //@RequiresPermissions("application:list")
    @Log(title = "管理员树形列表显示", businessType = BusinessType.OTHER)
    @ApiOperation("管理员树形列表显示")
    public R adminTreeList(@RequestBody DeptListRequest params) {
        return R.ok(this.deptService.treeListByOrgId(params.getOrgId(), params));

    }

    */
/**
     * 批量启用/禁用
     *//*

    @PostMapping("batch-enabled")
    //@RequiresPermissions("role:delete")
    @Log(title = "批量启用/禁用】", businessType = BusinessType.OTHER)
    @ApiOperation("批量启用/禁用")
    public R batchEnabled(@RequestBody BatchFlagRequest request) {
        this.deptService.batchEnabled(request.getFlag(), request.getIdList());
        return R.ok();
    }

    */
/**
     *  查询部门子列表
     *
     * @param
     * @return
     *//*

    @PostMapping("children/list")
    //@RequiresPermissions("dict:page")
    @Log(title = "查询部门子列表", businessType = BusinessType.OTHER)
    @ApiOperation("查询部门子列表")
    public R<List<DeptListResponse>> childList(@RequestBody DeptChildRequest params) {
        return R.ok(this.deptService.childList(params));
    }

    */
/**
     * 查询职工部门表
     *
     *//*

    @PostMapping("/getOrgDept")
    @ApiOperation("查询机构部门")
    public R<List<OrgDeptOrRoleResponseDTO>> getOrgDept(@RequestBody IdRequest request) {
        return R.ok(this.deptService.getOrgDept(request.getId()));
    }

    */
/**
     * 查询部门下的职工数量
     *
     * @return
     *//*

    @PostMapping("/employee/count")
    @ApiOperation("查询部门下的职工数量")
    //@RequiresPermissions("dept:employee:count")
    @Log(title = "查询部门下的职工数量", businessType = BusinessType.QUERY)
    public R<Long> deptEmployeeCount(@RequestBody @Validated PlatformIdListParam param) {
        return R.ok(this.deptService.getDeptEmployeeCount(param.getOrgId(), param.getIdList()));
    }

    */
/**
     * 查询部门下的学员数量
     *
     * @return
     *//*

    @PostMapping("/student/count")
    @ApiOperation("查询部门下的学员数量")
    //@RequiresPermissions("dept:student:count")
    @Log(title = "查询部门下的学员数量", businessType = BusinessType.QUERY)
    public R<Long> deptStudentCount(@RequestBody @Validated PlatformIdListParam param) {
        return R.ok(this.deptService.getDeptStudentCount(param.getOrgId(), param.getIdList()));
    }

}

*/
