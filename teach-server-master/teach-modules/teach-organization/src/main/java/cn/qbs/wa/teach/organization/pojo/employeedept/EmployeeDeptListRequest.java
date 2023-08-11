package cn.qbs.wa.teach.organization.pojo.employeedept;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 职工部门表(EmployeeDept)查询参数
 *
 * @author makejava
 * @since 2021-11-09 20:11:35
 */
@Data
public class EmployeeDeptListRequest {

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "部门id")
    private Long deptId;

}

