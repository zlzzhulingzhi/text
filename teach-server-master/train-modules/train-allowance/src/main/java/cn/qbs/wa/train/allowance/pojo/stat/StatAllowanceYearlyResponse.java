package cn.qbs.wa.train.allowance.pojo.stat;


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
public class StatAllowanceYearlyResponse{

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

    @ApiModelProperty(value = "资助类别")
    private String category;

}
