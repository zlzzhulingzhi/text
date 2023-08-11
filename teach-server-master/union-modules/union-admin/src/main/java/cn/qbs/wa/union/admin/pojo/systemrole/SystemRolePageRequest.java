package cn.qbs.wa.union.admin.pojo.systemrole;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 【系统角色】(SystemRole)分页查询参数
 *
 * @author makejava
 * @since 2022-07-08 09:03:05
 */
@Data
public class SystemRolePageRequest extends BasePageRequest {

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

    @ApiModelProperty(value = "权重")
    private Integer priority;

    @ApiModelProperty(value = "【启用状态 false-0-未启用 true-1-启用】")
    private Integer enabled;

}

