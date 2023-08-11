package cn.qbs.wa.teach.cert.pojo.standardtemplateconfig;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 证书模板配置(StandardTemplateConfig)分页查询参数
 *
 * @author makejava
 * @since 2022-01-19 11:38:22
 */
@Data
public class StandardTemplateConfigPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "模板id")
    private Long templateId;

    @ApiModelProperty(value = "配置类型 1.证书参数配置 2.证书查询配置")
    private Integer type;

    @ApiModelProperty(value = "配置码")
    private String code;

    @ApiModelProperty(value = "配置名称")
    private String keyName;

    @ApiModelProperty(value = "配置值")
    private String keyValue;

}

