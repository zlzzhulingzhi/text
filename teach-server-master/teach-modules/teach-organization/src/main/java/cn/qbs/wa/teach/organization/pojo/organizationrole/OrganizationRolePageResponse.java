package cn.qbs.wa.teach.organization.pojo.organizationrole;

import cn.qbs.wa.teach.organization.entity.OrganizationRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 【机构角色】(OrganizationRole)分页查询【机构角色】响应
 *
 * @author makejava
 * @since 2021-11-10 08:43:00
 */
@Data
public class OrganizationRolePageResponse extends OrganizationRole {

    @JsonIgnore
    List<Long> menuIdList;

    @JsonIgnore
    String menuIdStr;

    @ApiModelProperty("菜单名称")
    String menuNames;


}

