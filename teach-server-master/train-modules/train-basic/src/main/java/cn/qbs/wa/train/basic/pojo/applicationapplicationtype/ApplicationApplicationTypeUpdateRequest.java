package cn.qbs.wa.train.basic.pojo.applicationapplicationtype;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * (ApplicationApplicationType)更新参数
 *
 * @author makejava
 * @since 2021-11-10 10:22:57
 */
@Data
public class ApplicationApplicationTypeUpdateRequest {

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "应用id")
    private Long appId;

    @ApiModelProperty(value = "应用类型id")
    private Long appTypeId;

}

