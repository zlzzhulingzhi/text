package cn.qbs.wa.train.logistics.pojo.employeedept;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 职工部门表(EmployeeDept)更新职工部门表参数
 *
 * @author makejava
 * @since 2021-11-09 20:11:36
 */
@Data
public class EmployeeDeptUpdateRequest {

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "职工id")
    private Long employeeId;

    @ApiModelProperty(value = "部门id")
    private Long deptId;

}

