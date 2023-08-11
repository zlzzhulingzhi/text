package cn.qbs.wa.teach.organization.pojo.groups;

import cn.qbs.wa.teach.common.core.utils.TreeNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 【标签】分页查询【树菜单】响应
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:36
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GroupsTreeResponse extends TreeNode<GroupsTreeResponse> {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "【标签代号】")
    private String groupCode;

    @ApiModelProperty(value = "【标签名称】")
    private String groupName;

    @ApiModelProperty(value = "【排序】")
    private Integer sort;

    @ApiModelProperty(value = "包含课程")
    private Integer count;

    @ApiModelProperty(value = "启/禁用状态")
    private Integer enabled;

    @Override
    public int compareTo(GroupsTreeResponse o) {
        if (o.getSort() == null || sort == null) {
            return super.compareTo(o);
        }
        int i = this.sort.compareTo(o.getSort());
        return i == 0 ? this.id.compareTo(o.getId()) : i;
    }
}

