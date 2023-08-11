package cn.qbs.wa.teach.cert.api.pojo.DTO.cert.awardRecord;


import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.cert.api.pojo.DTO.template.CommonConfigDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PersonCertDetailResponseDTO {
    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "证书图片路径")
    private String certImageUrl;

    @ApiModelProperty(value = "证书pdf路径")
    private String certImagePdfUrl;

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

    public String getCertImagePdfUrl() {
        if (StrUtil.isNotBlank(certImageUrl)) {
            if (certImageUrl.contains("png")) {
                return certImageUrl.replace("png", "pdf");
            }
            if (certImageUrl.contains("jpg")) {
                return certImageUrl.replace("jpg", "pdf");
            }

        }
        return certImagePdfUrl;
    }
}
