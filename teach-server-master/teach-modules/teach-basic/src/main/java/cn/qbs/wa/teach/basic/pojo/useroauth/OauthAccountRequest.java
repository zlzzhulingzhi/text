package cn.qbs.wa.teach.basic.pojo.useroauth;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户绑定微信关系表(UserOauth)创建用户绑定微信关系表参数
 *
 * @author makejava
 * @version 1.0
 * @date 2022-03-04 10:21:49
 */
@Data
public class OauthAccountRequest {

    @ApiModelProperty(value = "【微信用户ID】")
    private String uid;

}

