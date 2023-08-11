package cn.qbs.wa.train.logistics.pojo.index;

import cn.qbs.wa.train.logistics.pojo.employee.EmployeeLoginPermissionRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @author yjx
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EmployeeLoginRequest extends EmployeeLoginPermissionRequest {

    @NotNull(message = "请选择机构")
    private Long orgId;
}
