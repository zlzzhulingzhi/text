package cn.qbs.wa.train.basic.pojo.app;

import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 19:46
 */
@Data
public class ApplicationPageRequest extends BasePageRequest {



    @ApiModelProperty(value = "【应用名称】")
    private String name;

    @ApiModelProperty(value = "【应用是否可用】")
    private Integer enabled;

    @ApiModelProperty(value = "【统一应用类型ID  [2平台管理后台 3机构管理后台]】")
    private Long appTypeId;

}
