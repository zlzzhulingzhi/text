package cn.qbs.wa.teach.exam.admin.pojo.api;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 考试分页查询响应
 * @Author zcm
 * @Date 2022-01-19 13:56
 * @Version 1.0
 */
@Data
public class ApiExamineeeListResponse {

    @ApiModelProperty(value = "考生ID")
    private Long examId;

    @ApiModelProperty(value = "考生用户ID")
    private Long userId;

    @ApiModelProperty(value = "考生职工ID")
    private Long employeeId;

    @ApiModelProperty(value = "考生学员ID")
    private Long studentId;

    @ApiModelProperty(value = "考生名称")
    private String examineeName;

    @ApiModelProperty(value = "考试名称")
    private String examName;

    @ApiModelProperty(value = "分数")
    private BigDecimal score;

}

