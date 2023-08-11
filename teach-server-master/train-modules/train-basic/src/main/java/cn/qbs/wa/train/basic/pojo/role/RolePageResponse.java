package cn.qbs.wa.train.basic.pojo.role;

import cn.qbs.wa.train.basic.entity.Role;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/4 18:52
 */
@Data
public class RolePageResponse  extends Role {

    @ApiModelProperty(value = "权限菜单名称")
    private String menuNames;

}
