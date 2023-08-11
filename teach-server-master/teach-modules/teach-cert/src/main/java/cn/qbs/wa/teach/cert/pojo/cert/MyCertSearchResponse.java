package cn.qbs.wa.teach.cert.pojo.cert;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/1/22 15:51
 */
@Data
public class MyCertSearchResponse {


    @ApiModelProperty(value = "证书图片路径")
    private String certImageUrl;

    @ApiModelProperty(value = "证书pdf路径")
    private String certImagePdfUrl;

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
