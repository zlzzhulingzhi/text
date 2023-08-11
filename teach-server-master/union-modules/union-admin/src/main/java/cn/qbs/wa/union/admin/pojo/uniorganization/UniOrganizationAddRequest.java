package cn.qbs.wa.union.admin.pojo.uniorganization;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * 统一机构(UniOrganization)创建统一机构参数
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-12 15:44:59
 */
@Data
public class UniOrganizationAddRequest {

    @NotBlank(message = "机构名称不为空")
    @ApiModelProperty(value = "机构名称")
    private String name;

    @ApiModelProperty(value = "机构性质")
    private String category;
    
    @ApiModelProperty(value = "机构介绍")
    private String intro;
    
    @ApiModelProperty(value = "地址")
    private String address;
    
    @ApiModelProperty(value = "信用代码")
    private String creditCode;
    
    @ApiModelProperty(value = "联系人")
    private String contactPerson;
    
    @ApiModelProperty(value = "联系电话")
    private String contactNumber;
    
    @ApiModelProperty(value = "联系邮箱")
    private String contactEmail;

    @ApiModelProperty(value = "法人代表")
    private String legalPerson;

    @ApiModelProperty(value = "法人电话")
    private String legalNumber;

    @ApiModelProperty(value = "计划标识(0-默认 1-万人计划)")
    private Integer plan;

    @ApiModelProperty(value = "启用状态(0-不可用  1-可用)")
    private Integer enabled;

}

