package cn.qbs.wa.teach.organization.api.pojo.DTO.organization;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 组织机构(Organization)创建组织机构参数
 *
 * @author makejava
 * @since 2021-11-09 20:13:14
 */
@Data
public class OrganizationListInnerDTO {

    @ApiModelProperty(value = "机构Id")
    private List<Long> ids;

    @ApiModelProperty(value = "机构名称")
    private String name;

    @ApiModelProperty(value = "公司名称")
    private String companyName;
}

