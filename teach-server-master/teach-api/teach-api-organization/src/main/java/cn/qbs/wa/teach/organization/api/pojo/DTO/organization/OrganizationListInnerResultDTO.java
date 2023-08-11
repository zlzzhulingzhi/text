package cn.qbs.wa.teach.organization.api.pojo.DTO.organization;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 职工信息
 * @author Administrator
 */
@Data
public class OrganizationListInnerResultDTO {


    @ApiModelProperty(value = "标识")
    private Long id;

    @ApiModelProperty(value = "机构名称")
    private String name;

    @ApiModelProperty(value = "机构简称")
    private String companyName;
}
