package cn.qbs.wa.teach.organization.pojo.dept;

import cn.qbs.wa.teach.organization.entity.Dept;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 部门表(Dept)部门表详情
 *
 * @author makejava
 * @since 2021-11-17 09:29:21
 */
@Data
public class DeptListResponse extends Dept {

    @ApiModelProperty(value = "部门id")
    private Long deptId;

    @ApiModelProperty("递归人数")
    Integer peopleCountTotal;

    @ApiModelProperty(value = "是否有子 0 否 1是")
    Integer hasChildren;

}

