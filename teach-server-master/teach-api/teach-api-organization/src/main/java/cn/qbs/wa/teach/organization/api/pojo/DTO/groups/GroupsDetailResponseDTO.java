package cn.qbs.wa.teach.organization.api.pojo.DTO.groups;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author makejava
 * @since 2022-03-28 09:27:33
 */
@Data
public class GroupsDetailResponseDTO  {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "父id")
    private Long parentId;

    @ApiModelProperty(value = "业务编码(编码一般为表名)")
    private String businessCode;

    @ApiModelProperty(value = "组名称")
    private String groupName;

    @ApiModelProperty(value = "组编码")
    private String groupCode;

    @ApiModelProperty(value = "0 禁用 1启用")
    private Integer enabled;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "标签id")
    private Long groupId;

}
