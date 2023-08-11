package cn.qbs.wa.train.basic.pojo.user;

import cn.qbs.wa.train.basic.pojo.app.ApplicationFullResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/3 14:26
 */
@Data
public class UserLoginPermissionResponse {

    @ApiModelProperty("应用列表")
    List<ApplicationFullResponse> appList;
}
