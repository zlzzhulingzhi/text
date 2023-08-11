package cn.qbs.wa.teach.question.pojo.category;

import cn.qbs.wa.teach.common.core.domain.IdListParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 分类试题数量查询参数
 *
 * @Author zcm
 * @Date 2022-06-20 9:22
 * @Version 1.0
 */
@Data
public class CategoryQuestionCountRequest extends IdListParam {

    @ApiModelProperty(value = "是否启用 【1-启用，0-废弃】")
    private Boolean enabled;

}

