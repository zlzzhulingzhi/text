package cn.qbs.wa.teach.exam.admin.pojo.exam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @Author WX
 * @Date 2021-12-24 18:44
 * @Version 1.0
 */
@Data
public class ExamUserRequest {

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "考试id")
    private Long examId;

    @ApiModelProperty(value = "学员id")
    private Long studentId;

}
