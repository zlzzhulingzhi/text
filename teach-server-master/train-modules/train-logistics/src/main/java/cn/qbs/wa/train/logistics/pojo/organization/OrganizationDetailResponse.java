package cn.qbs.wa.train.logistics.pojo.organization;

import cn.qbs.wa.train.logistics.entity.Organization;
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
public class OrganizationDetailResponse extends Organization {

    @ApiModelProperty(value = "菜单列表")
    List<Long> menuIdList;

    @ApiModelProperty(value = "菜单列表")
    String realName;

    @ApiModelProperty(value = "菜单列表")
    Long employeeId;



}

