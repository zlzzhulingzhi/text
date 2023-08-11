package cn.qbs.wa.teach.organization.pojo.organization;

import cn.qbs.wa.teach.organization.entity.Organization;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class OrganizationListResponse extends Organization {

    @ApiModelProperty(value = "机构id")
    private Long orgId;

}

