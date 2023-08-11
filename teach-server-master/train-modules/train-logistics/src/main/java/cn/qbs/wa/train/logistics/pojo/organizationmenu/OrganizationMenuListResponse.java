package cn.qbs.wa.train.logistics.pojo.organizationmenu;

import cn.qbs.wa.teach.common.core.utils.TreeNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/11 19:39
 */
@Data
public class OrganizationMenuListResponse extends TreeNode<OrganizationMenuListResponse> {

    @ApiModelProperty(value = "【应用模块ID】")
    private Long appId;

    @ApiModelProperty(value = "【菜单类型 0-应用 1-菜单 2-板块 3-按钮】")
    private Integer type;

    @ApiModelProperty(value = "【菜单URI】")
    private String uri;

    @ApiModelProperty(value = "【菜单权限 例：student:score:more】")
    private String permission;

    @ApiModelProperty(value = "【菜单名称】")
    private String name;
}
