package cn.qbs.wa.union.admin.pojo.systemuser;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 平台系统子用户表(SystemUser)分页查询平台系统子用户表响应
 *
 * @author makejava
 * @since 2022-07-08 09:03:08
 */
@Data
public class SystemUserPageResponse extends SystemUserDetailResponse {

    @ApiModelProperty("角色名称")
    String roleName;

    @ApiModelProperty("角色Code")
    String roleCode;

    @ApiModelProperty(value = "权重")
    private Integer priority;

    @ApiModelProperty(value = "编辑状态 0 不可编辑 1可编辑")
    private Boolean editable;
}

