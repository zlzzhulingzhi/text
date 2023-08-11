package cn.qbs.wa.teach.exam.admin.pojo.exam;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 试题正确率
 * @Author zcm
 * @Date 2021-12-28 9:44
 * @Version 1.0
 */
@Data
public class QuestionCorrectRate {

    @ApiModelProperty(value = "同题型下下标")
    private Integer localIndex;

    @ApiModelProperty(value = "试题ID")
    private Long questionId;

    @ApiModelProperty(value = "正确率")
    private Integer correctRate;

}
