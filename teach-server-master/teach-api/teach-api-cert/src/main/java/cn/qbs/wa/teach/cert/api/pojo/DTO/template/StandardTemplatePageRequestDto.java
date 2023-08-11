package cn.qbs.wa.teach.cert.api.pojo.DTO.template;


import cn.qbs.wa.teach.common.core.domain.BasePageSearchComDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 证书模板(StandardTemplate)分页查询参数
 *
 * @author makejava
 * @since 2022-01-19 11:38:21
 */
@Data
public class StandardTemplatePageRequestDto  extends BasePageSearchComDTO {


    @ApiModelProperty(value = "证书模板名称")
    private String name;

    @ApiModelProperty(value = "0 禁用 1正常")
    private Integer enabled;

    @ApiModelProperty(value = "排序")
    private Integer sort;

}

