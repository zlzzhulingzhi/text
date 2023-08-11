package cn.qbs.wa.teach.cert.pojo.cert;

import cn.qbs.wa.teach.cert.pojo.common.CommonConfigDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 证书(Cert)证书详情
 *
 * @author makejava
 * @since 2022-01-19 19:19:23
 */
@Data
public class CertDetailResponse {

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "模板id")
    private Long templateId;

    @ApiModelProperty(value = "证书名称")
    private String name;

    @ApiModelProperty(value = "证书模板名称")
    private String templateName;

    @ApiModelProperty(value = "证书搜索配置")
    private CommonConfigDTO certSearchConfig;

    @ApiModelProperty(value = "证书信息配置")
    private CommonConfigDTO certTemplateConfig;

}

