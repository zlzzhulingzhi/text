package cn.qbs.wa.teach.exam.answer.pojo.center;

import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author yjx
 */
@Data
public class ExamineRecordRequest extends BasePageRequest {

    @NotNull(message = "考生ID不能为空")
    @ApiModelProperty(value = "考生id")
    private Long examineeId;

    @ApiModelProperty(value = "考试id")
    private Long examId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "机构id")
    private Long orgId;
}
