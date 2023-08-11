package cn.qbs.wa.teach.exam.admin.pojo.exam;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 考试表(Exam)更新考试表参数
 *
 * @author zcm
 * @since 2021-12-14 13:52:10
 */
@Data
public class ExamUpdateRequest extends ExamAddRequest {

    @NotNull(message = "考试ID不能为空！")
    @ApiModelProperty(value = "考试ID")
    private Long id;

}

