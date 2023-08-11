package cn.qbs.wa.teach.question.pojo.question.type;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.qbs.wa.teach.domain.BasePageRequest;


/**
 * 题型(QuestionType)分页查询参数
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-03 17:10:23
 */
@Data
public class QuestionTypePageRequest extends BasePageRequest {

    @ApiModelProperty(value = "题型名称")
    private String name;

    @ApiModelProperty(value = "是否启用 【1：启用，0：废弃】")
    private Boolean enabled;

}

