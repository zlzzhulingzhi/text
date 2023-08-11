package cn.qbs.wa.train.logistics.pojo.organization;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 组织机构(Organization)创建组织机构参数
 *
 * @author makejava
 * @since 2021-11-09 20:13:14
 */
@Data
public class OrganizationAddRequest {

    @ApiModelProperty(value = "机构id")
    private Long orgId;

    @ApiModelProperty(value = "机构名称")
    private String name;

    @NotBlank(message = "公司名称不能为空")
    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "排序号")
    private Integer sort;

    @ApiModelProperty(value = "启用状态 0-禁用 1-启用")
    private Integer enabled;

    @ApiModelProperty(value = "机构分类 1: 企业 2: 高校 3: 自营 4: 培训机构")
    private String category;

    @ApiModelProperty(value = "PC端logo url")
    private String pcLogoUrl;

    @ApiModelProperty(value = "H5端logo url")
    private String h5LogoUrl;


    @ApiModelProperty(value = "域名")
    private String domain;

    @ApiModelProperty(value = "企业模板ID")
    private Long templateId;

    @ApiModelProperty(value = "机构介绍")
    private String intro;

    @ApiModelProperty(value = "机构介绍")
    private String remark;

    @ApiModelProperty(value = "菜单列表")
    List<Long> menuIdList;




}

