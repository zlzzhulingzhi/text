package cn.qbs.wa.union.admin.pojo.systemrole;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 【系统角色】(SystemRole)更新【系统角色】参数
 *
 * @author makejava
 * @since 2022-07-08 09:03:05
 */
@Data
public class SystemRoleUpdateRequest {

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "【应用模块ID】 预留")
    private Long appId;

    @ApiModelProperty(value = "【角色代码】")
    private String code;

    @ApiModelProperty(value = "【角色名称】")
    private String name;

    @ApiModelProperty(value = "【角色备注】")
    private String remark;

    @ApiModelProperty(value = "【角色排序序号】")
    private Integer sort;

    @ApiModelProperty(value = "【启用状态 false-0-未启用 true-1-启用】")
    private Integer enabled;

    @ApiModelProperty(value = "菜单id数组")
    List<Long> menuIdList;

}

