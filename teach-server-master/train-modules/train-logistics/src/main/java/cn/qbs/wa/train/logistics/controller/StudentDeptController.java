package cn.qbs.wa.train.logistics.controller;/*
package cn.qbs.wa.train.organization.controller;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.organization.pojo.studentdept.*;
import cn.qbs.wa.train.organization.service.DeptService;
import cn.qbs.wa.train.organization.service.StudentDeptService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

*/
/**
 * 学员部门表(StudentDept)表控制层
 *
 * @author makejava
 * @since 2022-05-09 15:15:30
 *//*

@RestController
@RequestMapping("studentDept")
@Api(tags = "学员部门管理")
public class StudentDeptController {

    */
/**
     * 服务对象
     *//*

    @Resource
    private StudentDeptService studentDeptService;

    @Resource
    private DeptService deptService;


    */
/**
     * 批量新增学员部门表
     *
     * @param params
     * @return
     *//*

    @PostMapping("add")
    //@RequiresPermissions("studentDept:add")
    //@Log(title = "批量新增学员部门表", businessType = BusinessType.INSERT)
    @ApiOperation("批量新增学员部门表")
    public R<Boolean> add(@RequestBody @Validated StudentDeptBatchAddRequest params) {
        return R.ok(this.studentDeptService.batchAdd(params));
    }

    */
/**
     * 分页查询学员部门表
     *
     * @param params
     * @return
     *//*

    @PostMapping("page")
    //@RequiresPermissions("studentDept:page")
    //@Log(title = "分页查询学员部门表", businessType = BusinessType.OTHER)
    public R<IPage<StudentDeptPageResponse>> page(@RequestBody StudentDeptPageRequest params) {
        return R.ok(this.studentDeptService.page(params));
    }

    */
/**
     * 查询学员部门表详情
     *
     * @param id 主键
     * @return
     *//*

    @PostMapping("detail")
    //@RequiresPermissions("studentDept:details")
    //@Log(title = "学员部门表详情", businessType = BusinessType.OTHER)
    public R<StudentDeptDetailResponse> detail(@RequestBody IdRequest request) {
        return R.ok(this.studentDeptService.detail(request.getId()));
    }

    @PostMapping("placeDept")
    public R<StudentDeptDetailResponse> placeDept(@RequestBody IdRequest request) {
        StudentDeptDetailResponse detailResponse = this.studentDeptService.detail(request.getId());
        detailResponse.setDeptName(deptService.getById(detailResponse.getDeptId()).getDeptName());
        return R.ok(detailResponse);
    }

    */
/**
     * 修改学员部门表
     *
     * @param params
     * @return
     *//*

    @PostMapping("update")
    //@RequiresPermissions("studentDept:update")
    //@Log(title = "更新学员部门表", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated StudentDeptUpdateRequest params) {
        return R.ok(this.studentDeptService.update(params));
    }

    */
/**
     * 删除学员部门表
     *
     * @param idList 主键集合
     * @return
     *//*

    @PostMapping("delete")
    //@RequiresPermissions("studentDept:delete")
    //@Log(title = "删除学员部门表", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.studentDeptService.deleteByIds(request.getIdList()));
    }

}

*/
