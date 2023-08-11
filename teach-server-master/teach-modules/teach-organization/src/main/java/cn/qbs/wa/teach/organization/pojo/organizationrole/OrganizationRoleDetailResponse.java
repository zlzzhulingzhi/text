package cn.qbs.wa.teach.organization.pojo.organizationrole;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.qbs.wa.teach.organization.entity.OrganizationRole;

import java.util.List;

/**
 * 【机构角色】(OrganizationRole)【机构角色】详情
 *
 * @author makejava
 * @since 2021-11-10 08:43:00
 */
@Data
public class OrganizationRoleDetailResponse extends OrganizationRole {

    @ApiModelProperty("菜单id数组")
    List<Long> menuIdList;

}

