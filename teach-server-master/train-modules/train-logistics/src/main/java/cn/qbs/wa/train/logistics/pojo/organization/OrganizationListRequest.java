package cn.qbs.wa.train.logistics.pojo.organization;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 组织机构(Organization)创建组织机构参数
 *
 * @author makejava
 * @since 2021-11-09 20:13:14
 */
@Data
public class OrganizationListRequest {

    @ApiModelProperty(value = "机构Id")
    private Long id;


    @ApiModelProperty(value = "机构名称")
    private String name;


    @ApiModelProperty(value = "机构职工人数")
    private Integer employeeCount;



}

