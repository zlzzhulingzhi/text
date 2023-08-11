package cn.qbs.wa.teach.question.pojo.question.basket;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 从试题篮移除试题参数
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-25 09:42:39
 */
@Data
public class QuestionBasketRemoveRequest {

    @NotNull(message = "试题ID列表不能为空！")
    @ApiModelProperty(value = "试题ID列表")
    private List<Long> questionIdList;

}

