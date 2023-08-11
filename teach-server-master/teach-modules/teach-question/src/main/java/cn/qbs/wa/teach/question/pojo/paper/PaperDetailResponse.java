package cn.qbs.wa.teach.question.pojo.paper;

import cn.qbs.wa.teach.question.pojo.category.SimpleCategoryDTO;
import cn.qbs.wa.teach.question.pojo.question.QuestionGroupResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.qbs.wa.teach.question.entity.Paper;

import java.util.List;

/**
 * 试卷(Paper)试卷详情
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-18 20:49:03
 */
@Data
public class PaperDetailResponse extends Paper {

    @ApiModelProperty(value = "题型列表")
    private List<QuestionGroupResponse> questionTypeList;

    @ApiModelProperty(value = "分类列表")
    private List<SimpleCategoryDTO> categoryList;

}

