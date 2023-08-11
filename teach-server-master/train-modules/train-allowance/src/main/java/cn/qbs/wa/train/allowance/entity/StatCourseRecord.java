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
 * @since 2022-11-19 13:56:54
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StatCourseRecord extends Model {

    private static final long serialVersionUID = 843030826431319609L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "申请ID")
    private Long applyId;

    @ApiModelProperty(value = "提交日期")
    private LocalDate submitDate;

    @ApiModelProperty(value = "总学时")
    private Integer totalLessonNum;

    @ApiModelProperty(value = "总人数")
    private Integer totalStudentNum;

    @ApiModelProperty(value = "实际补贴费用合计(元)")
    private BigDecimal totalFundFee;

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

}
