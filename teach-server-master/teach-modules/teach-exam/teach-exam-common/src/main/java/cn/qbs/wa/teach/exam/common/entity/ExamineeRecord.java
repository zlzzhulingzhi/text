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
 * @author makejava
 * @since 2022-02-17 16:40:04
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ExamineeRecord extends Model {

    private static final long serialVersionUID = -56729380400838166L;


    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "考生id")
    private Long examineeId;

    @ApiModelProperty(value = "0 未完成 1 待批改 2已批改 3中断")
    private Integer status;

    @ApiModelProperty(value = "是否纳入计算 0 否 1是")
    private Boolean calculated;

    @ApiModelProperty(value = "考试分数")
    private BigDecimal score;

    @ApiModelProperty(value = "开启答题时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "结束答题时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "提交时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime submitTime;

    @ApiModelProperty(value = "考试用时多少时间完成(秒数)")
    private Integer useDuration;

    @ApiModelProperty(value = "考试用时格式化")
    private String useDurationFormat;

    @ApiModelProperty(value = "")
    private String remark;

    @ApiModelProperty(value = "0 作废 1启用")
    private Integer enabled;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "附件名称")
    private String attachmentName;

    @ApiModelProperty(value = "附件Url")
    private String attachmentUrl;

}
