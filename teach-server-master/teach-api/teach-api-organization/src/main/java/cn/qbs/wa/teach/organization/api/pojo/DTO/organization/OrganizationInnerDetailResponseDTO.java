package cn.qbs.wa.teach.organization.api.pojo.DTO.organization;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 组织机构(Organization)组织机构详情
 *
 * @author makejava
 * @since 2021-11-09 20:13:15
 */
@Data
public class OrganizationInnerDetailResponseDTO{

    @ApiModelProperty(value = "PC端logo url")
    private String pcLogoUrl;

    @ApiModelProperty(value = "机构名称")
    private String name;

}

