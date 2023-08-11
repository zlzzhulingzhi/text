package cn.qbs.wa.union.admin.pojo.uniorganization;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 统一机构(UniOrganization)分页查询统一机构响应
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-12 15:44:59
 */
@Data
public class UniOrganizationInfoResponse {

    @ApiModelProperty(value = "标识")
    private Long id;

    @ApiModelProperty(value = "机构分类 1: 企业 2: 高校 3: 自营 4: 培训机构")
    private Integer category;

    @ApiModelProperty(value = "机构名称")
    private String name;

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

    @ApiModelProperty(value = "计划标识(0-默认 1-万人计划)")
    private Integer plan;

    @ApiModelProperty(value = "启用状态(0-不可用  1-可用)")
    private Integer enabled;
}

