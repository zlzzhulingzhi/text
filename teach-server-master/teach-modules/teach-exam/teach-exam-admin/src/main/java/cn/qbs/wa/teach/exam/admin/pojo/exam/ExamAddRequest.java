package cn.qbs.wa.teach.exam.admin.pojo.exam;


import cn.qbs.wa.teach.common.core.deserializer.LocalDateTimeDeserializer;
import cn.qbs.wa.teach.exam.common.enumerate.ExamModeEnum;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 考试表(Exam)创建考试表参数
 *
 * @author zcm
 * @since 2021-12-14 13:52:10
 */
@Data
public class ExamAddRequest {

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @NotBlank(message = "考试名称不能为空！")
    @Length(max = 50, message = "考试名称限制在50字以内！")
    @ApiModelProperty(value = "考试名称")
    private String examName;

    @NotNull(message = "试卷ID不能为空！")
    @ApiModelProperty(value = "试卷id")
    private Long paperId;

    @ApiModelProperty(value = "状态  1 未考试 2 考试中 3考试结束")
    private Integer status;

    @ApiModelProperty(value = "编辑状态 0 不可编辑 1可编辑")
    private Integer edited;

//    @NotNull(message = "考试开始时间不能为空！")
    @ApiModelProperty(value = "开始时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime startTime;

//    @NotNull(message = "考试截止时间不能为空！")
    @ApiModelProperty(value = "截止（结束）时间")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime endTime;

//    @NotNull(message = "考试模式不能为空！")
    @ApiModelProperty(value = "考试模式 1-标准模式 2-高级模式")
    private Integer examMode = ExamModeEnum.STANDARD.getMode();

//    @NotNull(message = "考试时长不能为空！")
    @ApiModelProperty(value = "考试时长,单位是分钟")
    private Integer duration;

    @NotNull(message = "考试次数不能为空！")
    @ApiModelProperty(value = "考试次数 -1为无限考试")
    private Integer limitCount;

    @ApiModelProperty(value = "介绍")
    private String introduction;

    @NotNull(message = "考试发布方式不能为空！")
    @ApiModelProperty(value = "发布方式 { 1: 创建时发布, 2 手动发布, 3: 定时发布}")
    @Range(min = 1, max = 3, message = "发布方式不合法！")
    private Integer releaseMode;

    @ApiModelProperty(value = "通过分数")
    private BigDecimal passScore;

    // 需求更改，不需要考试分类
//    @NotEmpty(message = "考试分类分类ID列表不能为空！")
    @ApiModelProperty(value = "考试分类ID列表")
    private List<Long> categoryIdList;

//    @NotEmpty(message = "规则ID列表不能为空！")
    @ApiModelProperty(value = "规则ID列表")
    private List<Long> ruleIdList;

    @NotNull(message = "可见用户不能为空")
    @Range(min = 1, max = 2, message = "可见用户不合法！")
    @ApiModelProperty(value = "可见用户 1: 所有人可见 2: 部分可见")
    private Integer userVisible;

//    @NotEmpty(message = "可见用户列表不能为空！")
    @ApiModelProperty(value = "可见用户列表")
    private List<VisibleUser> visibleUserList;

    @ApiModelProperty(value = "发布时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime releaseTime;

    @ApiModelProperty(value = "入场开始时间")
    @JSONField(format = "yyyy-MM-dd HH:mm")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime admissionStartTime;

    @ApiModelProperty(value = "入场截止时间")
    @JSONField(format = "yyyy-MM-dd HH:mm")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime admissionEndTime;

}

