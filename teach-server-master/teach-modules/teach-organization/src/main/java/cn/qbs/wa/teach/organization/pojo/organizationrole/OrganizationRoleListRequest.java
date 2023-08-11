package cn.qbs.wa.teach.organization.pojo.organizationrole;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/11 14:13
 */
@Data
public class OrganizationRoleListRequest {


    @ApiModelProperty("机构id")
    Long orgId;

    @ApiModelProperty("搜索内容")
    String searchContent;
}
