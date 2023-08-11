package cn.qbs.wa.union.auth.pojo.uniapplication;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 统一应用展示(UniApplication)更新统一应用展示参数
 *
 * @author makejava
 * @since 2022-07-08 09:03:10
 */
@Data
public class UniApplicationUpdateRequest {

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "【统一客户端类型码 如admin、org】")
    private String uniClientCode;

    @ApiModelProperty(value = "【统一应用类型ID】")
    private Long uniAppTypeId;

    @ApiModelProperty(value = "【应用图标地址】")
    private String appIconUrl;

    @ApiModelProperty(value = "【应用名称】")
    private String appName;

    @ApiModelProperty(value = "【应用主机】")
    private String appHost;

    @ApiModelProperty(value = "【应用访问地址】")
    private String appUri;

    @ApiModelProperty(value = "【应用权限标识】")
    private String appPermission;

    @ApiModelProperty(value = "【应用备注】")
    private String appRemark;

    @ApiModelProperty(value = "【应用是否可用】")
    private Integer enable;

    @ApiModelProperty(value = "【应用在分类中的排序】")
    private Integer sort;

}

