package cn.qbs.wa.train.logistics.pojo.employee;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/3 14:26
 */
@Data
public class EmployeeLoginPermissionRequest {

    @ApiModelProperty(value = "应用类别Id",example = "2 平台管理后台 3 机构管理后台")
    Long applicationTypeId;
}
