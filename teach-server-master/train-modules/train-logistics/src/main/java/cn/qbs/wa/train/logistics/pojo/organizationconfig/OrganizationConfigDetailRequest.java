package cn.qbs.wa.train.logistics.pojo.organizationconfig;

import cn.qbs.wa.train.logistics.enums.DecorationEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 机构配置表(OrganizationConfig)机构配置表详情
 *
 * @author makejava
 * @version 1.0
 * @date 2021-12-08 16:04:26
 */
@Data
public class OrganizationConfigDetailRequest {

    @ApiModelProperty(value = "机构Id")
    private Long orgId;

    @ApiModelProperty(value = "1-内训PC装修 2-外训PC装修 3-内训H5 4-外训H5", notes = "默认为 1-内训PC装修")
    private Integer type = DecorationEnum.PC.getCode();

}

