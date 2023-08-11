package cn.qbs.wa.teach.exam.answer.pojo.exam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author zcm
 * @Date 2022/5/17 13:56
 * @Version 1.0
 */
@Data
public class SaveAttachment implements Serializable {

    @NotNull(message = "考试记录ID不能为空")
    @ApiModelProperty(value = "考试记录ID")
    private Long examineeRecordId;

    @NotBlank(message = "附件名称不能为空")
    @ApiModelProperty(value = "附件名称")
    private String attachmentName;

    @NotBlank(message = "附件Url不能为空")
    @ApiModelProperty(value = "附件Url")
    private String attachmentUrl;

}
