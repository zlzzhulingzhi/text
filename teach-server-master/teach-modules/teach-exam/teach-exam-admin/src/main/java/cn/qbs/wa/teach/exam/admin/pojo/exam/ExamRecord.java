package cn.qbs.wa.teach.exam.admin.pojo.exam;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *
 * @Author zcm
 * @Date 2021-12-24 18:44
 * @Version 1.0
 */
@Data
public class ExamRecord {

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "考生id")
    private Long examineeId;

    @ApiModelProperty(value = "考生姓名")
    private String examineeName;

    @ApiModelProperty(value = "0 未完成 1 待批改 2已批改 3中断")
    private Integer status;

    @ApiModelProperty(value = "是否纳入计算 0 否 1是")
    private Boolean calculated;

    @ApiModelProperty(value = "考试分数")
    private BigDecimal score;

    @ApiModelProperty(value = "开启答题时间")
    @JSONField(format = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "提交时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime submitTime;

    @ApiModelProperty(value = "考试用时多少时间完成(秒数)")
    private Integer useDuration;

    @ApiModelProperty(value = "考试用时格式化")
    private String useDurationFormat;

    @ApiModelProperty(value = "备注")
    private String remark;

}
