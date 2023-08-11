package cn.qbs.wa.teach.basic.pojo.user;

import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/4 19:29
 */
@Data
public class UserPageRequest extends BasePageRequest {

    @ApiModelProperty("搜索内容")
    String searchContent;

    @ApiModelProperty("加密搜索内容")
    String encodeSearchContent;

    @ApiModelProperty("禁用/启用")
    Integer enabled;
}
