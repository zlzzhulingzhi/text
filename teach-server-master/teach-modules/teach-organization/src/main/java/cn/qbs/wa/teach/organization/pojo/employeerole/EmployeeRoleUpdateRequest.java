package cn.qbs.wa.teach.organization.pojo.employeerole;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 【职工角色关联关系】(EmployeeRole)更新【职工角色关联关系】参数
 *
 * @author makejava
 * @since 2021-11-09 20:12:52
 */
@Data
public class EmployeeRoleUpdateRequest {

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "【系统职工ID】")
    private Long employeeId;

    @ApiModelProperty(value = "【角色ID】")
    private Long roleId;

}

