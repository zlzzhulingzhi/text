package cn.qbs.wa.teach.basic.pojo.applicationapplicationtype;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * (ApplicationApplicationType)分页查询参数
 *
 * @author makejava
 * @since 2021-11-10 10:22:56
 */
@Data
public class ApplicationApplicationTypePageRequest extends BasePageRequest {

    @ApiModelProperty(value = "应用id")
    private Long appId;

    @ApiModelProperty(value = "应用类型id")
    private Long appTypeId;

}

