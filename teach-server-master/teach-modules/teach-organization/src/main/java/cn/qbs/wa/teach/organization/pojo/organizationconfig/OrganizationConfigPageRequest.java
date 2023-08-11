package cn.qbs.wa.teach.organization.pojo.organizationconfig;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 机构配置表(OrganizationConfig)分页查询参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-12-08 16:04:26
 */
@Data
public class OrganizationConfigPageRequest extends BasePageRequest {
    
    @ApiModelProperty(value = "机构Id")
    private Long orgId;
    
    @ApiModelProperty(value = "1 首页装修")
    private Integer type;
    
    @ApiModelProperty(value = "json配置内容")
    private String text;

}

