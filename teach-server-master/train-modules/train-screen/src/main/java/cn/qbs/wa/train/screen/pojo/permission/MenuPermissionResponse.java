package cn.qbs.wa.train.screen.pojo.permission;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class MenuPermissionResponse {

    @ApiModelProperty("菜单列表")
    private List<SystemMenuTree> menuList;

}
