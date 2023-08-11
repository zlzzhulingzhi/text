package cn.qbs.wa.teach.organization.pojo.organization;


import cn.qbs.wa.teach.organization.entity.Organization;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 组织机构(Organization)创建组织机构参数
 *
 * @author makejava
 * @since 2021-11-09 20:13:14
 */
@Data
public class OrganizationSelectListResponse extends Organization {

    @ApiModelProperty(value = "0未添加 1已添加")
    private Integer added=0;


}

