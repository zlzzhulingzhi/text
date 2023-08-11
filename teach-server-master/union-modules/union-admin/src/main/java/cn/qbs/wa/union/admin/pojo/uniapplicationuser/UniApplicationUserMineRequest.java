package cn.qbs.wa.union.admin.pojo.uniapplicationuser;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 【系统应用-用户】(UniApplicationUser)创建【系统应用-用户】参数
 *
 * @author makejava
 * @since 2022-07-08 09:03:12
 */
@Data
public class UniApplicationUserMineRequest {

    @NotBlank(message = "应用编码不能为空")
    @ApiModelProperty(value = "应用编码 [admin 管理员 org 机构职工]",example = "admin")
    private String loginCode;


}

