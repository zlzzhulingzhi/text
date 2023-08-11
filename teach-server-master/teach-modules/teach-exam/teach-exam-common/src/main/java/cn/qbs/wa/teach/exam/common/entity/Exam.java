package cn.qbs.wa.teach.exam.common.entity;


import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
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
 * @since 2022-02-17 16:40:03
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Exam extends Model {

    private static final long serialVersionUID = 929508792690905084L;


    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "考试名称")
    private String examName;

    @ApiModelProperty(value = "试卷id")
    private Long paperId;

    @ApiModelProperty(value = "试卷分数/考试总分")
    private BigDecimal paperScore;

    @ApiModelProperty(value = "状态 1-未考试 2-考试中 3-考试结束")
    private Integer status;

    @ApiModelProperty(value = "上架状态 0-下架 1-上架")
    private Integer shelfStatus;

    @ApiModelProperty(value = "编辑状态 0-不可编辑 1-可编辑")
    private Integer edited;

    @ApiModelProperty(value = "开始时间")
    @JSONField(format = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "截止（结束）时间")
    @JSONField(format = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "难度")
    private Integer difficulty;

    @ApiModelProperty(value = "考试时长,单位是分钟")
    private Integer duration;

    @ApiModelProperty(value = "考试次数 -1为无限考试")
    private Integer limitCount;

    @ApiModelProperty(value = "介绍")
    private String introduction;

    @ApiModelProperty(value = "可见用户 1: 所有人可见 2: 部分可见")
    private Integer userVisible;

    @ApiModelProperty(value = "发布方式 { 1: 创建时发布, 2 手动发布, 3: 定时发布}")
    private Integer releaseMode;

    @ApiModelProperty(value = "通过分数")
    @TableField(updateStrategy= FieldStrategy.IGNORED)
    private BigDecimal passScore;

    @ApiModelProperty(value = "发布时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime releaseTime;

    @ApiModelProperty(value = "考试模式 1-标准模式 2-高级模式")
    private Integer examMode;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateBy;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "是否启用 1:启用，0:禁用")
    private Integer enabled;

    @ApiModelProperty(value = "启动方式 1-自动 2-手动")
    private Integer startWay;

    @ApiModelProperty(value = "结束方式 1-自动 2-手动")
    private Integer endWay;

    @ApiModelProperty(value = "入场开始时间")
    @JSONField(format = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime admissionStartTime;

    @ApiModelProperty(value = "入场截止时间")
    @JSONField(format = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime admissionEndTime;


}
