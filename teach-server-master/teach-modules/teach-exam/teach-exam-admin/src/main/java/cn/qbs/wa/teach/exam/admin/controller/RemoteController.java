package cn.qbs.wa.teach.exam.admin.controller;


import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.security.annotation.AutoSelectOrg;
import cn.qbs.wa.teach.exam.admin.service.RemoteService;
import cn.qbs.wa.teach.organization.api.RemoteEmployeeDeptService;
import cn.qbs.wa.teach.organization.api.RemoteGroupsService;
import cn.qbs.wa.teach.organization.api.RemoteOrgBackDeptService;
import cn.qbs.wa.teach.organization.api.RemoteStudentService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.dept.DeptListRequestDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.dept.EmployeeDeptListSearchDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.dept.EmployeeDeptTreeListDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.dept.TreeDeptTotalResponseDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.groups.GroupsDetailResponseDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.lecturer.LecturerDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.lecturer.LecturerSearchDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentSearchDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * 课程模块远程服务统一请求接口（用户接口、资源库接口、题库试卷接口）
 *
 * @author yjx
 * @version 1.0
 * @date 2021-11-16 10:33:01
 */
@Api(tags = "课程模块远程服务统一请求接口")
@RestController
@RequestMapping("/remote")
public class RemoteController {

    /**
     * 部门员工服务
     */
    @Resource
    private RemoteEmployeeDeptService remoteEmployeeDeptService;


    /**
     * 部门员工服务
     */
    @Resource
    private RemoteStudentService remoteStudentService;

    /**
     * 讲师服务
     */
    @Resource
    private RemoteService remoteService;

    /**
     * 机构部门
     */
    @Resource
    private RemoteOrgBackDeptService remoteOrgBackDeptService;

    /**
     * 标签服务
     */
    @Resource
    private RemoteGroupsService remoteGroupsService;

    /**
     * 获取机构职工部门
     * @param params 查询参数
     * @return 部门下职工
     */
    @ApiOperation(value = "获取机构职工部门")
    @PostMapping("/dept/employee/tree")
    public R<List<EmployeeDeptTreeListDTO>> listDeptTree(@RequestBody EmployeeDeptListSearchDTO params) {
        if (params.getOrgId() == null) {
            params.setOrgId(SecurityContextHolder.getOrgId());
        }
        return remoteEmployeeDeptService.listTree(params);
    }

    /**
     * 获取机构讲师列表
     * @param params 查询参数
     * @return 讲师列表
     */
    @ApiOperation(value = "获取机构讲师列表")
    @PostMapping("/lecturer/list")
    public R<List<LecturerDTO>> listLecturers(@RequestBody LecturerSearchDTO params) {
        if (params.getOrgId() == null) {
            params.setOrgId(SecurityContextHolder.getOrgId());
        }
        return R.ok(remoteService.listLecturers(params));
    }

    /**
     * 获取机构学员列表
     * @param params 查询参数
     * @return 学员列表
     */
    @AutoSelectOrg
    @ApiOperation(value = "获取机构学员列表")
    @PostMapping("/student/page")
    public R<PageResultComDTO<StudentDTO>> pageStudent(@RequestBody StudentSearchDTO params) {
        return remoteStudentService.pageWithStaff(params);
    }

    @PostMapping("dept/tree-list")
    @ApiOperation("查询所有部门")
    public R<TreeDeptTotalResponseDTO> treeList(@RequestBody DeptListRequestDTO params){
        params.setOrgId(SecurityContextHolder.getOrgId());
        params.setEnabled(1);
        return remoteOrgBackDeptService.treeList(params);
    }

    @ApiOperation("查询所有启用标签")
    @PostMapping("group/list")
    public R<List<GroupsDetailResponseDTO>> list(){
        return remoteGroupsService.list();
    }

}

