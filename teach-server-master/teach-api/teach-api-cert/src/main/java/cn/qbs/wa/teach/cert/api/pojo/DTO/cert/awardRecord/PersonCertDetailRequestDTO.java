package cn.qbs.wa.teach.cert.api.pojo.DTO.cert.awardRecord;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PersonCertDetailRequestDTO {
    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "证书id")
    private Long certId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "来源id")
    private Long sourceId;
}
