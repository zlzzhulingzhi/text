package cn.qbs.wa.teach.exam.admin.pojo.exam;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 阅卷-考试记录分页查询响应
 *
 * @Author zcm
 * @Date 2022-01-15 16:23
 * @Version 1.0
 */
@Data
public class MarkingExamRecordPageResponse {

    @ApiModelProperty(value = "考试记录ID")
    private Long examineeRecordId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "考生ID")
    private Long examineeId;

    @ApiModelProperty(value = "考生姓名")
    private String examineeName;

    @ApiModelProperty(value = "考试分数")
    private BigDecimal score;

    @ApiModelProperty(value = "交卷时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime submitTime;

    @ApiModelProperty(value = "批阅状态 1 未批阅 2 已批阅")
    private Integer correctStatus;

}

