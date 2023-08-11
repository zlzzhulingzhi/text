package cn.qbs.wa.teach.cert.pojo.cert;

import cn.qbs.wa.teach.cert.pojo.common.CommonConfigDTO;
import cn.qbs.wa.teach.cert.pojo.common.CommonConfigDetailDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PersonCertDetailResponse {
    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "证书图片路径")
    private String certImageUrl;

    @ApiModelProperty(value = "模板id")
    private Long templateId;

    @ApiModelProperty(value = "证书名称")
    private String name;

    @ApiModelProperty(value = "证书模板名称")
    private String templateName;

    @ApiModelProperty(value = "禁用false 启用true")
    private Boolean enabled;

    @ApiModelProperty(value = "证书搜索配置")
    private CommonConfigDTO certSearchConfig;

    @ApiModelProperty(value = "证书信息配置")
    private CommonConfigDTO certTemplateConfig;
}
