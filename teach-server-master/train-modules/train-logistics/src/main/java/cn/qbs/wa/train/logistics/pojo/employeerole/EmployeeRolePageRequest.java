package cn.qbs.wa.train.logistics.pojo.employeerole;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 【职工角色关联关系】(EmployeeRole)分页查询参数
 *
 * @author makejava
 * @since 2021-11-09 20:12:51
 */
@Data
public class EmployeeRolePageRequest extends BasePageRequest {

    @ApiModelProperty(value = "【系统职工ID】")
    private Long employeeId;

    @ApiModelProperty(value = "【角色ID】")
    private Long roleId;

}

