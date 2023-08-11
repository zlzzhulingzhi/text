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
 * @since 2022-11-03 19:30:24
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ApplyAllowanceStudent extends Model {

    private static final long serialVersionUID = -84830464910420191L;


    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "资助资金申请ID")
    private Long applyAllowanceId;

    @ApiModelProperty(value = "学员ID")
    private Long studentId;

    @ApiModelProperty(value = "学员用户ID")
    private Long memberId;

    @ApiModelProperty(value = "学员姓名")
    private String studentName;

    @ApiModelProperty(value = "单位ID")
    private Long unitId;

    @ApiModelProperty(value = "单位名称")
    private String unitName;

    @ApiModelProperty(value = "考试结果")
    private String examResult;

    @ApiModelProperty(value = "学时")
    private Integer lessonNum;

    @ApiModelProperty(value = "缴费金额(元)")
    private BigDecimal payAmount;

    @ApiModelProperty(value = "补助金额(元)")
    private BigDecimal supplyAmount;

    @ApiModelProperty(value = "考试成绩")
    private BigDecimal examScore;

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
