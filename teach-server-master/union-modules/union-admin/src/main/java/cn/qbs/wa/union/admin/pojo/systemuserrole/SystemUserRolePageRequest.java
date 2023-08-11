package cn.qbs.wa.union.admin.pojo.systemuserrole;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 【平台用户角色关联关系】(SystemUserRole)分页查询参数
 *
 * @author makejava
 * @since 2022-07-08 09:03:09
 */
@Data
public class SystemUserRolePageRequest extends BasePageRequest {

    @ApiModelProperty(value = "【系统用户ID】")
    private Long userId;

    @ApiModelProperty(value = "【角色ID】")
    private Long roleId;

}

