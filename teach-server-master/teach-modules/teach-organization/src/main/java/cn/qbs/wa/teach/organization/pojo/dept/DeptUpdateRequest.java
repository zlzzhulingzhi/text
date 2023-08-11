package cn.qbs.wa.teach.organization.pojo.dept;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 部门表(Dept)更新部门表参数
 *
 * @author makejava
 * @since 2021-11-17 09:29:20
 */
@Data
public class DeptUpdateRequest {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "父主键")
    private Long parentId;

    @ApiModelProperty(value = "部门名")
    private String deptName;

    @ApiModelProperty(value = "部门全称")
    private String fullName;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "0 禁用 1启用")
    private Integer enabled;

    @ApiModelProperty(value = "人数")
    private Integer peopleCount;

}

