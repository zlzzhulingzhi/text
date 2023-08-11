package cn.qbs.wa.union.auth.pojo.systemapplication;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 【系统应用】(SystemApplication)分页查询参数
 *
 * @author makejava
 * @since 2022-07-08 09:03:02
 */
@Data
public class SystemApplicationPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "应用名称")
    private String name;

    @ApiModelProperty(value = "访问地址")
    private String url;

    @ApiModelProperty(value = "权限标识")
    private String permission;

    @ApiModelProperty(value = "说明")
    private String represent;

    @ApiModelProperty(value = "主机名称")
    private String hostName;

    @ApiModelProperty(value = "状态")
    private Integer enable;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "图标")
    private String iconUrl;

}

