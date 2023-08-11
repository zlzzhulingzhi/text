package cn.qbs.wa.train.allowance.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author makejava
 * @since 2022-11-03 19:27:14
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ApplyActivity extends Model {

    private static final long serialVersionUID = 607636916543982969L;


    @ApiModelProperty(value = "主键")
    private Long id;

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

    @ApiModelProperty(value = "创建人ID")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改人ID")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateBy;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
