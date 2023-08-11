package cn.qbs.wa.train.logistics.pojo.employeedept;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 职工部门表(EmployeeDept)分页查询参数
 *
 * @author makejava
 * @since 2021-11-09 20:11:35
 */
@Data
public class EmployeeDeptPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "职工id")
    private Long employeeId;

    @ApiModelProperty(value = "部门id")
    private Long deptId;

}

