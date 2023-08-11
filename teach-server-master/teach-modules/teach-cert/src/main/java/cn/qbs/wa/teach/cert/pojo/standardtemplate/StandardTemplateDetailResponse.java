package cn.qbs.wa.teach.cert.pojo.standardtemplate;

import cn.qbs.wa.teach.cert.pojo.common.CommonConfigDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 证书模板(StandardTemplate)证书模板详情
 *
 * @author makejava
 * @since 2022-01-19 11:38:22
 */
@Data
public class StandardTemplateDetailResponse {

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "证书模板名称")
    private String name;

    @ApiModelProperty(value = "背景图片地址")
    private String backgroundUrl;

    @ApiModelProperty(value = "模式 1 2")
    private Integer modelType;

    @ApiModelProperty(value = "证书搜索配置")
    private CommonConfigDTO certSearchConfig;

    @ApiModelProperty(value = "证书信息配置")
    private CommonConfigDTO certTemplateConfig;


}

