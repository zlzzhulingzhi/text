package cn.qbs.wa.teach.exam.api.pojo.DTO;


import cn.hutool.core.util.StrUtil;
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
