package cn.qbs.wa.teach.question.pojo.question.search;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 试题统计结果
 * @Author zcm
 * @Date 2021/11/17 09:31
 * @Version 1.0
 */
@Data
public class QuestionCountResponse {

    @ApiModelProperty(value = "难度列表")
    private List<CountResult> difficultyList = new ArrayList<>();

    @ApiModelProperty(value = "题型列表")
    private List<CountResult> questionTypeList = new ArrayList<>();

}
