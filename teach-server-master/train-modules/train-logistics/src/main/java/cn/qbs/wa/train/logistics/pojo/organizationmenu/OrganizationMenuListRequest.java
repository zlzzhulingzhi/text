package cn.qbs.wa.train.logistics.pojo.organizationmenu;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/11 19:39
 */
@Data
public class OrganizationMenuListRequest {

    @ApiModelProperty("机构Id")
    Long orgId;
}
