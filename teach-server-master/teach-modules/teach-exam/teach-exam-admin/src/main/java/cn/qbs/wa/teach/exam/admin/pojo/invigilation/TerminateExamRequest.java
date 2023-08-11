package cn.qbs.wa.teach.exam.admin.pojo.invigilation;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 终止考试请求参数
 * @Author zcm
 * @Date 2022-01-25 10:16
 * @Version 1.0
 */
@Data
public class TerminateExamRequest {

    @NotNull(message = "考试记录ID不能为空！")
    @ApiModelProperty(value = "考试记录ID")
    private Long examineeRecordId;

    @NotBlank(message = "终止考试原因不能为空！")
    @Length(max = 100, message = "终止理由限制100字以内！")
    @ApiModelProperty(value = "原因")
    private String reason;

}

