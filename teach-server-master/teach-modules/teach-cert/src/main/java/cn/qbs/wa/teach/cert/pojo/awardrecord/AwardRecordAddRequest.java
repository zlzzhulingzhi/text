package cn.qbs.wa.teach.cert.pojo.awardrecord;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 颁发记录(AwardRecord)创建颁发记录参数
 *
 * @author makejava
 * @since 2022-01-19 11:38:18
 */
@Data
public class AwardRecordAddRequest {

    @ApiModelProperty(value = "机构Id")
    private Long orgId;

    @ApiModelProperty(value = "证书Id")
    private Long certId;

    @ApiModelProperty(value = "用户Id")
    private Long userId;

    @ApiModelProperty(value = "职工Id")
    private Long studentId;

}

