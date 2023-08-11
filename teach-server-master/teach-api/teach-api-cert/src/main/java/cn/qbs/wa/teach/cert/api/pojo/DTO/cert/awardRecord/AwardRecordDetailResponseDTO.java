package cn.qbs.wa.teach.cert.api.pojo.DTO.cert.awardRecord;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: NQY
 * @Date: 2022/4/27 18:30
 * @Description:
 */
@Data
public class AwardRecordDetailResponseDTO {
    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "机构Id")
    private Long orgId;

    @ApiModelProperty(value = "证书Id")
    private Long certId;

    @ApiModelProperty(value = "用户Id")
    private Long userId;

    @ApiModelProperty(value = "学员Id")
    private Long studentId;

    @ApiModelProperty(value = "证书前缀规则")
    private String certPrefix;

    @ApiModelProperty(value = "证书编号")
    private String certNum;

    @ApiModelProperty(value = "禁用0 启用1")
    private Integer enabled;

    @ApiModelProperty(value = "证书图片路径")
    private String certImageUrl;
}
