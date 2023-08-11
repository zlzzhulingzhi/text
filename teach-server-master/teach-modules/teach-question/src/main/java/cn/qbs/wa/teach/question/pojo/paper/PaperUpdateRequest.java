package cn.qbs.wa.teach.question.pojo.paper;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 更新试卷参数
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-18 20:49:03
 */
@Data
public class PaperUpdateRequest extends PaperAddRequest {

    @NotNull(message = "试卷ID不能为空")
    @ApiModelProperty(value = "ID")
    private Long id;

}

