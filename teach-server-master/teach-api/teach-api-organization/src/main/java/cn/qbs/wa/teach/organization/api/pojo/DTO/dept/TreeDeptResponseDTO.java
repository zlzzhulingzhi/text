package cn.qbs.wa.teach.organization.api.pojo.DTO.dept;

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
public class TreeDeptResponseDTO extends TreeNode<TreeDeptResponseDTO> {

    @ApiModelProperty("菜单名称别名")
    String deptName;

    @ApiModelProperty(value = "部门全称")
    private String fullName;

    @ApiModelProperty(value = "人数")
    Integer peopleCount;

    @ApiModelProperty("递归人数")
    Integer peopleCountTotal;

    @ApiModelProperty("菜单名称")
    String name;

    @ApiModelProperty("机构id")
    Long orgId;

    @ApiModelProperty(value = "主键")
    private Long deptId;

    public String getName() {
        return deptName;
    }
}
