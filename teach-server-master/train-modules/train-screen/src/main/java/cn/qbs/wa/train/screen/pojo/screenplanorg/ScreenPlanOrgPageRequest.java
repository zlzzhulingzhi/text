package cn.qbs.wa.train.screen.pojo.screenplanorg;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 大屏-万人计划机构(ScreenPlanOrg)分页查询参数
 *
 * @author makejava
 * @since 2022-10-14 15:22:15
 */
@Data
public class ScreenPlanOrgPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "机构ID(用于跳转)")
    private Long orgId;

    @ApiModelProperty(value = "机构名称")
    private String orgName;

    @ApiModelProperty(value = "机构简称")
    private String briefName;

    @ApiModelProperty(value = "排序号")
    private Integer sort;

}

