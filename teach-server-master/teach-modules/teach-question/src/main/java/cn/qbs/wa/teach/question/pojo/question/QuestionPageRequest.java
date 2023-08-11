package cn.qbs.wa.teach.question.pojo.question;

import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * 试题(Question)分页查询参数
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-03 18:45:25
 */
@Data
public class QuestionPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "题型ID列表")
    private List<Integer> questionTypeIds;

    @ApiModelProperty(value = "难度列表: 简单(1)、一般(2)、稍难(3)、困难(4)、极难(5)")
    private List<Integer> difficultys;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "分类ID列表")
    private List<Long> categoryIds;

    @ApiModelProperty(value = "搜索关键字")
    private String keyword;

}

