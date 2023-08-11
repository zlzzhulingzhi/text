package cn.qbs.wa.teach.cert.pojo.standardtemplate;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 证书模板(StandardTemplate)分页查询参数
 *
 * @author makejava
 * @since 2022-01-19 11:38:21
 */
@Data
public class StandardTemplatePageRequest extends BasePageRequest {


    @ApiModelProperty(value = "证书模板名称")
    private String name;

    @ApiModelProperty(value = "0 禁用 1正常")
    private Integer enabled;

    @ApiModelProperty(value = "排序")
    private Integer sort;

}

