package cn.qbs.wa.teach.cert.pojo.awardrecord;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 颁发记录(AwardRecord)颁发统计
 *
 * @author makejava
 * @since 2022-01-19 11:38:18
 */
@Data
public class AwardRecordCountResponse {

    @ApiModelProperty(value = "证书id")
    private Long certId;

    @ApiModelProperty(value = "发放证书人数")
    private Integer awardCount;

    @ApiModelProperty(value = "发放证书无效人数")
    private Integer awardInvalidCount;

    @ApiModelProperty(value = "发放证书有效人数")
    private Integer awardEffectiveCount;

}

