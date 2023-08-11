package cn.qbs.wa.teach.organization.pojo.employeedept;

import cn.qbs.wa.teach.common.core.utils.TreeNode;
import cn.qbs.wa.teach.organization.entity.Dept;
import cn.qbs.wa.teach.organization.entity.Employee;
import cn.qbs.wa.teach.organization.entity.EmployeeDept;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 职工部门表(EmployeeDept)查询职工部门表响应
 *
 * @author makejava
 * @since 2021-11-09 20:11:36
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EmployeeDeptTreeListResponse extends TreeNode<EmployeeDeptTreeListResponse> {

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "部门名")
    private String deptName;

    @ApiModelProperty(value = "部门全称")
    private String fullName;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "员工列表")
    private List<Employee> employees;

    @Override
    public int compareTo(EmployeeDeptTreeListResponse o) {
        if (o.getSort() == null) {
            return super.compareTo(o);
        }
       return this.getSort().compareTo(o.getSort());

    }
}

