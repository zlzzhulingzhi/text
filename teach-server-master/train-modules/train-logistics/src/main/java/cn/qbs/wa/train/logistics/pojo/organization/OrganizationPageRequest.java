package cn.qbs.wa.train.logistics.pojo.organization;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 组织机构(Organization)分页查询参数
 *
 * @author makejava
 * @since 2021-11-09 20:13:13
 */
@Data
public class OrganizationPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "机构名称")
    private String name;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "启用状态 0-禁用 1-启用")
    private Integer enabled;


}

