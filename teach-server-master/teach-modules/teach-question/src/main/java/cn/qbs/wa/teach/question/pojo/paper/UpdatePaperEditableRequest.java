package cn.qbs.wa.teach.question.pojo.paper;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * 更新试卷可编辑状态
 * @Author zcm
 * @Date 2022/1/6 11:09
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePaperEditableRequest {

    @NotNull(message = "试卷ID不能为空")
    @ApiModelProperty("试卷ID")
    private Long id;

    @NotNull
    @ApiModelProperty(value = "编辑状态 0 不可编辑 1可编辑")
    private Boolean editable;

}
