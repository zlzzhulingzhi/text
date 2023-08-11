package cn.qbs.wa.train.logistics.pojo.organization.inner;

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

}
