package cn.qbs.wa.train.basic.pojo.user;

import cn.qbs.wa.train.basic.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/4 18:52
 */
@Data
public class PageUserResponse {

    @ApiModelProperty(value = "总页数")
    private Integer total;

    private List<User> userList;

}
