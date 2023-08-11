package cn.qbs.wa.teach.exam.admin.pojo.exam;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 试题正确率包裹类
 * @Author zcm
 * @Date 2021-12-28 9:44
 * @Version 1.0
 */
@Data
public class QuestionCorrectRateWrap {

    @ApiModelProperty(value = "试题ID")
    private List<QuestionType> questionTypeList;

    @Data
    public static class QuestionType {

        @ApiModelProperty(value = "题型ID")
        private Long questionTypeId;

        @ApiModelProperty(value = "题型名称")
        private String questionTypeName;

        @ApiModelProperty(value = "排序号")
        private Integer sortNum;

        @ApiModelProperty(value = "试题正确率列表")
        private List<QuestionCorrectRate> questionCorrectRateList;

    }

}
