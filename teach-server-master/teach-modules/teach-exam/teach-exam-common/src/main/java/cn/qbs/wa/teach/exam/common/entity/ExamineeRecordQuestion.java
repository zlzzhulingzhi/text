package cn.qbs.wa.teach.exam.common.entity;


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
 * @author zcm
 * @since 2021-12-13 17:27:21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ExamineeRecordQuestion extends Model {

    private static final long serialVersionUID = -49244917268700833L;


    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "考生考试记录id")
    private Long examineeRecordId;

    @ApiModelProperty(value = "题目编号")
    private Long questionId;

    @ApiModelProperty(value = "答案")
    private String answer;

    @ApiModelProperty(value = "答题时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime answerTime;

    @ApiModelProperty(value = "答案图片的访问url")
    private String answerUrl;

    @ApiModelProperty(value = "0 未作答  1已作答")
    private Integer answerStatus;

    @ApiModelProperty(value = "0 错 1对")
    private Integer correctResult;

    @ApiModelProperty(value = "教师批改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime correctTime;

    @ApiModelProperty(value = "得分")
    private BigDecimal score;

    @ApiModelProperty(value = "题目分数")
    private BigDecimal questionScore;

    @ApiModelProperty(value = "0 未批改  1已批改")
    private Integer correctStatus;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "批改人")
    private Long correctBy;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "排序号")
    private Integer sortNum;

}
