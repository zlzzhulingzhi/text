package cn.qbs.wa.union.auth.pojo.uniapplication;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 统一应用展示(UniApplication)分页查询参数
 *
 * @author makejava
 * @since 2022-07-08 09:03:09
 */
@Data
public class UniApplicationPageRequest extends BasePageRequest {

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

