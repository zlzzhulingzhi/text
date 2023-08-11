package cn.qbs.wa.union.auth.pojo.systemmenu;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 【系统菜单】(SystemMenu)分页查询参数
 *
 * @author makejava
 * @since 2022-07-08 09:03:04
 */
@Data
public class SystemMenuPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "【应用模块ID】")
    private Long appId;

    @ApiModelProperty(value = "【上级菜单ID】")
    private Long parentId;

    @ApiModelProperty(value = "【菜单类型 0-应用 1-菜单 2-板块 3-按钮】")
    private Integer type;

    @ApiModelProperty(value = "【菜单URI】")
    private String uri;

    @ApiModelProperty(value = "【菜单权限 例：student:score:more】")
    private String permission;

    @ApiModelProperty(value = "【菜单名称】")
    private String name;

    @ApiModelProperty(value = "【菜单说明】")
    private String remark;

    @ApiModelProperty(value = "【菜单图标URL】")
    private String iconUrl;

    @ApiModelProperty(value = "【菜单类别】 预留")
    private String category;

    @ApiModelProperty(value = "【菜单排序】")
    private Integer sort;

    @ApiModelProperty(value = "【启用状态 false-0-未启用 true-1-启用】")
    private Integer enabled;

}

