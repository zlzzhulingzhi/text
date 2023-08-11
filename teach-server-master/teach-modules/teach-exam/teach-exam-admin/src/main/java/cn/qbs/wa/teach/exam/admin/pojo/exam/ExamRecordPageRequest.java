package cn.qbs.wa.teach.exam.admin.pojo.exam;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 考试记录分页查询参数
 * @Author zcm
 * @Date 2022-01-03 17:25
 * @Version 1.0
 */
@Data
public class ExamRecordPageRequest extends BasePageRequest {

    @NotNull(message = "考试ID不能为空！")
    @ApiModelProperty(value = "考试记录ID")
    private Long examId;

}

