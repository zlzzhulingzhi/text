package cn.qbs.wa.train.basic.pojo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/4 19:29
 */
@Data
public class UserListRequest{

    @ApiModelProperty("搜索内容")
    String searchContent;

    @ApiModelProperty("加密搜索内容")
    String encodeSearchContent;

    @ApiModelProperty("禁用/启用")
    Integer enabled;

    @ApiModelProperty("用户id数组")
    List<Long> idList;
}
