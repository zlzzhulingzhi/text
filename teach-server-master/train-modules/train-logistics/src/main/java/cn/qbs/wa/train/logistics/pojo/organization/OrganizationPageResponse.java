package cn.qbs.wa.train.logistics.pojo.organization;

import cn.qbs.wa.train.logistics.entity.Organization;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 组织机构(Organization)分页查询组织机构响应
 *
 * @author makejava
 * @since 2021-11-09 20:13:14
 */
@Data
public class OrganizationPageResponse extends Organization {

    @ApiModelProperty("企业类型名称")
    String categoryName;

    @ApiModelProperty("账号")
    String account;

    @ApiModelProperty("名称")
    String realName;

}

