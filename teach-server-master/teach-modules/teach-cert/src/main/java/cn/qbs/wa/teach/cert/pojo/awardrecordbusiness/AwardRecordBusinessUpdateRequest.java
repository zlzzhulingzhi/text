package cn.qbs.wa.teach.cert.pojo.awardrecordbusiness;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 基础直播业务关联表(AwardRecordBusiness)更新基础直播业务关联表参数
 *
 * @author makejava
 * @since 2022-01-19 11:38:19
 */
@Data
public class AwardRecordBusinessUpdateRequest {

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "颁发记录id")
    private Long awardRecordId;

    @ApiModelProperty(value = "业务类型 1考试")
    private Integer businessType;

    @ApiModelProperty(value = "业务id")
    private Long businessId;

    @ApiModelProperty(value = "业务名称")
    private String businessName;

    @ApiModelProperty(value = "业务内容")
    private String businessContent;

}

