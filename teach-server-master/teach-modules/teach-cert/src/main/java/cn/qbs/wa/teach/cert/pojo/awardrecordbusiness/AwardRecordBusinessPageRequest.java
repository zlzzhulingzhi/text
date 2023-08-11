package cn.qbs.wa.teach.cert.pojo.awardrecordbusiness;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 基础直播业务关联表(AwardRecordBusiness)分页查询参数
 *
 * @author makejava
 * @since 2022-01-19 11:38:18
 */
@Data
public class AwardRecordBusinessPageRequest extends BasePageRequest {

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

