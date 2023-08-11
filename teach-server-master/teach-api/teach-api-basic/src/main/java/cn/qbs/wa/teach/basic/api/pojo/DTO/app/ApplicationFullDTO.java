package cn.qbs.wa.teach.basic.api.pojo.DTO.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/10 16:55
 */
@Data
public class ApplicationFullDTO {

    @ApiModelProperty(value = "应用类别Id",example = "1 内训管理平台 1000 内训门户 2000 外训管理平台")
    Long applicationTypeId;
}
