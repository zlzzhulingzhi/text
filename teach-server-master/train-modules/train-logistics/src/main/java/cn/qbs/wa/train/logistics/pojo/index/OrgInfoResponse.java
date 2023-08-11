package cn.qbs.wa.train.logistics.pojo.index;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrgInfoResponse {

    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @ApiModelProperty(value = "机构名称")
    private String orgName;

    @ApiModelProperty(value = "机构分类 1: 企业 2: 高校 3: 自营 4: 培训机构")
    private Integer category;

    @ApiModelProperty(value = "启用状态 0-禁用 1-启用")
    private Integer enabled;

    @ApiModelProperty("PC端logo url")
    private String pcLogoUrl;

    @ApiModelProperty("小端logo url")
    private String h5LogoUrl;

    @ApiModelProperty(value = "机构角色")
    private String roleNames;

    @ApiModelProperty(value = "域名")
    private String domain;
}
