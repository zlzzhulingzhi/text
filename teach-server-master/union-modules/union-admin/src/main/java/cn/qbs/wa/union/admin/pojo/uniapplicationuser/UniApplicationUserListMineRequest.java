package cn.qbs.wa.union.admin.pojo.uniapplicationuser;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 【系统应用-用户】(UniApplicationUser)分页查询参数
 *
 * @author makejava
 * @since 2022-07-08 09:03:11
 */
@Data
public class UniApplicationUserListMineRequest  {

    @ApiModelProperty(value = "【统一客户端类型码 如admin、org】")
    private String uniClientCode;


}

