package cn.qbs.wa.teach.cert.pojo.cert;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PersonCertDetailRequest {

    @ApiModelProperty(value = "证书id")
    private Long certId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "来源id")
    private Long sourceId;
}
