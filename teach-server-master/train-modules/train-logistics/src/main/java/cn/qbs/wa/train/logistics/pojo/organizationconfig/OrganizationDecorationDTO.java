package cn.qbs.wa.train.logistics.pojo.organizationconfig;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yjx
 */
@Data
public class OrganizationDecorationDTO {

    @ApiModelProperty(value = "主键标识")
    private Long id;

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "1-内训PC装修 2-外训PC装修 3-内训H5 4-外训H5")
    private Integer type;

    @ApiModelProperty(value = "json配置内容")
    private String text;

    @ApiModelProperty(value = "机构名称")
    private String orgName;

    @ApiModelProperty(value = "登录页面宣传图地址")
    private String loginPosterUrl;
}
