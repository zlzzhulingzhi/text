package cn.qbs.wa.teach.exam.admin.pojo.api;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 查询考试考生列表响应
 * @Author zcm
 * @Date 2022-01-19 13:56
 * @Version 1.0
 */
@Data
public class ApiExamPageResponse {

    @ApiModelProperty(value = "考试id")
    private Long examId;

    @ApiModelProperty(value = "考试名称")
    private String examName;

    @ApiModelProperty(value = "通过分数")
    private BigDecimal passScore;

    @ApiModelProperty(value = "创建时间")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

}

