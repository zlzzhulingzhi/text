package cn.qbs.wa.teach.organization.api.pojo.DTO.organization;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 职工信息
 * @author Administrator
 */
@Data
public class OrganizationListResultDTO {


    @ApiModelProperty(value = "标识")
    private Long id;

    @ApiModelProperty(value = "机构名称")
    private String name;

    @ApiModelProperty(value = "排序号")
    private Integer sort;

    @ApiModelProperty(value = "启用状态 0-禁用 1-启用")
    private Integer enabled;

    @ApiModelProperty(value = "最后修改人ID")
    private Long updateBy;

    @ApiModelProperty(value = "最后修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "创建人ID")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "机构分类 1: 企业 2: 高校 3: 自营 4: 培训机构")
    private Integer category;

    @ApiModelProperty(value = "PC端logo url")
    private String pcLogoUrl;

    @ApiModelProperty(value = "H5端logo url")
    private String h5LogoUrl;

    @ApiModelProperty(value = "绑定用户ID")
    private Long bindUserId;

    @ApiModelProperty(value = "域名")
    private String domain;

    @ApiModelProperty(value = "企业模板ID")
    private Long templateId;

    @ApiModelProperty(value = "机构介绍")
    private String intro;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "0未添加 1已添加")
    private Integer added=0;
}
