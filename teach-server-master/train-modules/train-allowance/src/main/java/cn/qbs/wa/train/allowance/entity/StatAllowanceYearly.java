package cn.qbs.wa.train.allowance.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author makejava
 * @since 2022-11-14 15:35:53
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StatAllowanceYearly extends Model {

    private static final long serialVersionUID = -24074782648544365L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "年份")
    private Integer year;

    @ApiModelProperty(value = "总学时")
    private Integer totalLessonNum;

    @ApiModelProperty(value = "总人数")
    private Integer totalStudentNum;

    @ApiModelProperty(value = "活动资助费用合计(元)")
    private BigDecimal totalActivityFee;

    @ApiModelProperty(value = "课程资助费用合计(元)")
    private BigDecimal totalCourseFee;

    @ApiModelProperty(value = "培训场所资助费用(元)")
    private BigDecimal siteFundFee;

    @ApiModelProperty(value = "学员就餐资助费用(元)")
    private BigDecimal mealFundFee;

    @ApiModelProperty(value = "学员住宿资助费用(元)")
    private BigDecimal roomFundFee;

    @ApiModelProperty(value = "学员交通资助费用(元)")
    private BigDecimal trafficFundFee;

    @ApiModelProperty(value = "学员学费资助金额(元)")
    private BigDecimal studyFundFee;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}
