package cn.qbs.wa.teach.question.pojo.question.basket;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 试题篮分组统计项
 * @Author zcm
 * @Date 2021/11/25 10:25
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionBasketGroupCountItem {

    @ApiModelProperty(value = "题型ID")
    private Long questionTypeId;

    @ApiModelProperty(value = "题型名称")
    private String questionTypeName;

    @ApiModelProperty(value = "排序号")
    private Integer sortNum;

    @ApiModelProperty(value = "试题ID列表")
    private List<Long> questionIdList;

}
