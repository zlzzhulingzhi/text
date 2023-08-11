package cn.qbs.wa.train.screen.pojo.permission;

import cn.qbs.wa.teach.common.core.utils.TreeNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 菜单树
 *
 * @author makejava
 * @since 2022-10-08 09:03:05
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SystemMenuTree extends TreeNode<SystemMenuTree> {

    @ApiModelProperty(value = "【菜单类型 0-应用 1-菜单 2-板块 3-按钮】")
    private Integer type;

    private String permission;

    @ApiModelProperty(value = "【菜单名称】")
    private String menuName;

    @ApiModelProperty(value = "【菜单图标URL】")
    private String iconUrl;

    @ApiModelProperty(value = "【菜单排序】")
    private Integer sort;

    @Override
    public int compareTo(SystemMenuTree o) {
        if (o.getSort() == null || sort == null) {
            return super.compareTo(o);
        }
        int i = this.sort.compareTo(o.getSort());
        return i == 0 ? this.id.compareTo(o.getId()) : i;
    }
}

