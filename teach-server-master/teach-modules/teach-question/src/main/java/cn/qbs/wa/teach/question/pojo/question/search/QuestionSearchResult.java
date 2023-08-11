package cn.qbs.wa.teach.question.pojo.question.search;

import cn.qbs.wa.teach.question.pojo.category.SimpleCategoryDTO;
import cn.qbs.wa.teach.question.pojo.question.BasicQuestion;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 试题搜索结果类
 * @Author zcm
 * @Date 2021/11/17 17:08
 * @Version 1.0
 */
@Data
public class QuestionSearchResult extends BasicQuestion implements Serializable {

    @ApiModelProperty(value = "分类列表")
    private List<SimpleCategoryDTO> categoryList = new ArrayList<>();

    @ApiModelProperty(value = "排序号(同一题型内)")
    private Integer localOrder;

    /**
     * 答案数量，填空题使用此属性
     */
    @ApiModelProperty(value = "答案数量")
    private Integer answerCount;

    @ApiModelProperty(value = "是否启用 【1：启用，0：禁用】")
    private Boolean enabled;

}
