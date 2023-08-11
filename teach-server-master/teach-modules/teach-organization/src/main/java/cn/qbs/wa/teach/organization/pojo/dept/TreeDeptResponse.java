package cn.qbs.wa.teach.organization.pojo.dept;

import cn.qbs.wa.teach.common.core.utils.TreeNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/3 15:14
 */
@Data
public class TreeDeptResponse extends TreeNode<TreeDeptResponse> {

    @ApiModelProperty("菜单名称别名")
    String deptName;

    @ApiModelProperty(value = "人数")
    Integer peopleCount;

    @ApiModelProperty("递归人数")
    Integer peopleCountTotal;

    @ApiModelProperty("菜单名称")
    String name;

    @ApiModelProperty(value = "部门全称")
    private String fullName;

    @ApiModelProperty("机构id")
    Long orgId;

    @ApiModelProperty("部门id")
    Long deptId;

    @ApiModelProperty(value = "0 禁用 1启用")
    private Integer enabled;

    @ApiModelProperty(value = "是否有子 0 否 1是")
    Integer hasChildren;

    @ApiModelProperty(value = "学员人数")
    private Integer studentCount;

    @Getter(AccessLevel.NONE)
    @ApiModelProperty("递归人数")
    Integer studentCountTotal;

    public String getName() {
        return deptName;
    }

    public Integer getStudentCountTotal() {
        if (studentCountTotal == null) {
            return studentCount;
        }
        return studentCountTotal;
    }
}
