package cn.qbs.wa.teach.organization.pojo.organizationconfig;


import cn.qbs.wa.teach.organization.enums.DecorationEnum;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 机构配置表(OrganizationConfig)更新机构配置表参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-12-08 16:04:26
 */
@Data
public class OrganizationConfigUpdateRequest {
    
    @ApiModelProperty(value = "主键标识")
    private Long id;
    
    @ApiModelProperty(value = "机构Id")
    private Long orgId;

    @ApiModelProperty(value = "1-内训PC装修 2-外训PC装修 3-内训H5 4-外训H5", example = "1", notes = "默认为 1-内训PC装修")
    private Integer type = DecorationEnum.PC.getCode();
    
    @ApiModelProperty(value = "json配置内容")
    private String text;

    @ApiModelProperty(value = "登录页面宣传图地址")
    private String loginPosterUrl;

}

