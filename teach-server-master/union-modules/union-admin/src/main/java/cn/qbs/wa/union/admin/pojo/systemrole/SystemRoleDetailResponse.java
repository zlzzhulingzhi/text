package cn.qbs.wa.union.admin.pojo.systemrole;

import cn.qbs.wa.union.admin.entity.SystemRole;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 【系统角色】(SystemRole)【系统角色】详情
 *
 * @author makejava
 * @since 2022-07-08 09:03:05
 */
@Data
public class SystemRoleDetailResponse extends SystemRole {

    @ApiModelProperty(value = "菜单id数组")
    List<Long> menuIdList;

}

