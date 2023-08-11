package cn.qbs.wa.teach.course.standard.pojo.category;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yjx
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryEnableRequest extends IdListRequest {

    @ApiModelProperty(value = "0 禁用  1 启用")
    private Integer enabled;
}
