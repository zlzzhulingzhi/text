package cn.qbs.wa.train.basic.api.pojo.DTO.menu;

import cn.qbs.wa.teach.common.core.utils.TreeNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/3 15:14
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TreeMenuDTO extends TreeNode<TreeMenuDTO> {

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

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @Override
    public int compareTo(TreeMenuDTO o) {
        if (o.getSort() == null || sort == null) {
            return super.compareTo(o);
        }
        int i = this.sort.compareTo(o.getSort());
        return i == 0 ? this.id.compareTo(o.getId()) : i;
    }
}
