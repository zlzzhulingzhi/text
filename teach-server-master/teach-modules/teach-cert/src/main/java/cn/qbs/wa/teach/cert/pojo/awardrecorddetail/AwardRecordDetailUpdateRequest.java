package cn.qbs.wa.teach.cert.pojo.awardrecorddetail;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 证书配置(AwardRecordDetail)更新证书配置参数
 *
 * @author makejava
 * @since 2022-01-19 11:38:20
 */
@Data
public class AwardRecordDetailUpdateRequest {

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "颁发记录id")
    private Long awardRecordId;

    @ApiModelProperty(value = "配置类型 1.证书参数配置 2.证书查询配置")
    private Integer type;

    @ApiModelProperty(value = "配置码")
    private String code;

    @ApiModelProperty(value = "配置名称")
    private String keyName;

    @ApiModelProperty(value = "配置值")
    private String keyValue;

}

