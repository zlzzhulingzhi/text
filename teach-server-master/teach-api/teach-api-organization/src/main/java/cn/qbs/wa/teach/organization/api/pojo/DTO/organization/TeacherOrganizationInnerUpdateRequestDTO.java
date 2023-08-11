package cn.qbs.wa.teach.organization.api.pojo.DTO.organization;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 组织机构(Organization)更新组织机构参数
 *
 * @author makejava
 * @since 2021-11-09 20:13:14
 */
@Data
public class TeacherOrganizationInnerUpdateRequestDTO {

    @ApiModelProperty(value = "标识")
    private Long id;

    @ApiModelProperty(value = "机构名称")
    private String name;

    @ApiModelProperty(value = "启用状态 0-禁用 1-启用")
    private Integer enabled;

    @ApiModelProperty(value = "机构分类 1: 企业 2: 高校 3: 自营 4: 培训机构")
    private String category;

}

