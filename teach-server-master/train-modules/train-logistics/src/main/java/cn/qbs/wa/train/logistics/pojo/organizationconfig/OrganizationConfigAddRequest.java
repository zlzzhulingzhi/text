package cn.qbs.wa.train.logistics.pojo.organizationconfig;


import cn.qbs.wa.train.logistics.enums.DecorationEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 机构配置表(OrganizationConfig)创建机构配置表参数
 *
 * @author makejava
 * @version 1.0
 * @date 2021-12-08 16:04:26
 */
@Data
public class OrganizationConfigAddRequest {

    @ApiModelProperty(value = "机构Id")
    private Long orgId;

    @ApiModelProperty(value = "1-内训PC装修 2-外训PC装修 3-内训H5 4-外训H5", example = "1", notes = "默认为 1-内训PC装修")
    private Integer type = DecorationEnum.PC.getCode();
    
    @ApiModelProperty(value = "json配置内容")
    private String text;

}

