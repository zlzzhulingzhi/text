package cn.qbs.wa.train.logistics.pojo.orgbackcoupon;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 机构部门表(EmployeeDept)响应参数
 *
 * @author WX
 * @since 2022-03-23
 */
@Data
public class OrgDeptOrRoleResponseDTO {

    @ApiModelProperty(value = "部门ID")
    private Long id;

    @ApiModelProperty(value = "部门名称")
    private String name;

}

