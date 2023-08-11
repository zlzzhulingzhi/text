package cn.qbs.wa.teach.organization.pojo.employeedept;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 职工部门表(EmployeeDept)创建职工部门表参数
 *
 * @author makejava
 * @since 2021-11-09 20:11:36
 */
@Data
public class EmployeeDeptAddRequest {

    @ApiModelProperty(value = "职工id")
    private Long employeeId;

    @ApiModelProperty(value = "部门id")
    private Long deptId;

}

