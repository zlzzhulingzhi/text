package cn.qbs.wa.teach.organization.pojo.worg;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 机构插件表(WOrg)更新机构插件表参数
 *
 * @author makejava
 * @since 2022-03-01 13:46:16
 */
@Data
public class WOrgUpdateRequest {

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "排序")
    private Integer sort;

}

