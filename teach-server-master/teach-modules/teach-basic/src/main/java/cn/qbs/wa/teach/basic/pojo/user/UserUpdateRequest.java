package cn.qbs.wa.teach.basic.pojo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/4 14:09
 */
@Data
public class UserUpdateRequest extends UserAdminUpdateRequest{

    @ApiModelProperty("部门Id")
    @NotNull(message = "请选择部门")
    Long deptId;
}
