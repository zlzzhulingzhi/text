package cn.qbs.wa.teach.organization.api.pojo.DTO.dept;

import cn.qbs.wa.teach.organization.api.pojo.DTO.employee.EmployeeDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 职工部门表(EmployeeDept)查询职工部门表响应
 *
 * @author makejava
 * @since 2021-11-09 20:11:36
 */
@Data
public class EmployeeDepListResultDTO  {

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "部门名")
    private String deptName;

    @ApiModelProperty(value = "部门全称")
    private String fullName;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "员工列表")
    private List<EmployeeDTO> employees;

}

