package cn.qbs.wa.union.admin.pojo.uniapplicationclient;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 统一应用客户端(UniApplicationClient)创建统一应用客户端参数
 *
 * @author makejava
 * @since 2022-07-08 09:03:10
 */
@Data
public class UniApplicationClientAddRequest {

    @ApiModelProperty(value = "")
    private String clientCode;

    @ApiModelProperty(value = "")
    private String clientName;

}

