package cn.qbs.wa.teach.organization.api.pojo.DTO.wlecturer;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 机构插件表(WOrg)更新机构插件表参数
 *
 * @author makejava
 * @since 2022-03-01 13:46:16
 */
@Data
public class WOrgUpdateDTO {

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "排序")
    private Integer sort;

}

