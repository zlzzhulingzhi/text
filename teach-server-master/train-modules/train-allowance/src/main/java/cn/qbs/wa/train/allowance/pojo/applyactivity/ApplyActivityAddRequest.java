package cn.qbs.wa.train.allowance.pojo.applyactivity;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 资助资金申请-学术会议和竞赛活动(ApplyActivity)创建资助资金申请-学术会议和竞赛活动参数
 *
 * @author makejava
 * @since 2022-11-03 19:27:15
 */
@Data
public class ApplyActivityAddRequest {

    @ApiModelProperty(value = "申请ID")
    private Long applyId;

    @ApiModelProperty(value = "活动名称")
    private String activityName;

    @ApiModelProperty(value = "活动主题")
    private String activityTheme;

    @ApiModelProperty(value = "预算金额(元)")
    private BigDecimal budgetAmount;

    @ApiModelProperty(value = "举办地点")
    private String activityAddress;

    @ApiModelProperty(value = "举办时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate activityDate;

    @ApiModelProperty(value = "举办规模")
    private String activityScale;

    @ApiModelProperty(value = "主办单位")
    private String activityUnits;

    @ApiModelProperty(value = "指导单位")
    private String guideUnits;

    @ApiModelProperty(value = "承办单位")
    private String undertakerUnits;

    @ApiModelProperty(value = "协办单位")
    private String partnerUnits;

    @ApiModelProperty(value = "总要嘉宾")
    private String guests;
    
}

