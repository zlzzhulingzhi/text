package cn.qbs.wa.union.admin.pojo.uniapplicationclient;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 统一应用客户端(UniApplicationClient)分页查询参数
 *
 * @author makejava
 * @since 2022-07-08 09:03:10
 */
@Data
public class UniApplicationClientPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "")
    private String clientCode;

    @ApiModelProperty(value = "")
    private String clientName;

}

