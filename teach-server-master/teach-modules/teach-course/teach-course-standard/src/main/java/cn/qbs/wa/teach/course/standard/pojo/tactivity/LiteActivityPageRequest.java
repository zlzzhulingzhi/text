package cn.qbs.wa.teach.course.standard.pojo.tactivity;

import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LiteActivityPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "活动名称")
    private String title;

}
