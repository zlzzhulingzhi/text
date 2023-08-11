package cn.qbs.wa.teach.basic.pojo.app;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/10 10:15
 */

@Data
public class ApplicationAddRequest {


    @ApiModelProperty(value = "【统一客户端类型码 如H5、PC】")
    private String clientCode;

    @ApiModelProperty(value = "【统一应用类型ID  [2平台管理后台 3机构管理后台]】")
    private Long appTypeId;

    @ApiModelProperty(value = "【应用图标地址】")
    private String iconUrl;

    @ApiModelProperty(value = "【应用名称】")
    private String name;

    @ApiModelProperty(value = "【应用主机】")
    private String host;

    @ApiModelProperty(value = "【应用访问地址】")
    private String uri;

    @ApiModelProperty(value = "【应用权限标识】")
    private String permission;

    @ApiModelProperty(value = "【应用备注】")
    private String remark;

    @ApiModelProperty(value = "【应用是否可用】")
    private Integer enabled;

    @ApiModelProperty(value = "【应用在分类中的排序】")
    private Integer sort;

}
