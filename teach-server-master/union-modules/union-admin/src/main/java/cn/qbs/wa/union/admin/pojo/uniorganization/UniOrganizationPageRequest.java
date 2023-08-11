package cn.qbs.wa.union.admin.pojo.uniorganization;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;
import lombok.EqualsAndHashCode;

/**
 * 统一机构(UniOrganization)分页查询参数
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-12 15:44:59
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UniOrganizationPageRequest extends BasePageRequest {
    
    @ApiModelProperty(value = "机构性质")
    private String category;

    @ApiModelProperty(value = "机构名称")
    private String name;
    
    @ApiModelProperty(value = "计划标识(0-默认 1-万人计划)")
    private Integer plan;

    private Integer enabled;

}

