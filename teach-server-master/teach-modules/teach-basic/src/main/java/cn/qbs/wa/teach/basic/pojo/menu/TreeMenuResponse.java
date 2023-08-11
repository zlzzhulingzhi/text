package cn.qbs.wa.teach.basic.pojo.menu;

import cn.qbs.wa.teach.common.core.utils.TreeNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/3 15:14
 */
@Data
public class TreeMenuResponse extends TreeNode<TreeMenuResponse> {

    @ApiModelProperty("菜单名称")
    String menuName;

    @ApiModelProperty("菜单分类")
    String category;

    @ApiModelProperty("菜单路由地址")
    String menuUri;

    @ApiModelProperty("菜单权限")
    String permission;

    @ApiModelProperty("应用Id")
    Long appId;
}
