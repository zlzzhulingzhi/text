package cn.qbs.wa.union.admin.pojo.uniapplicationuser;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 【系统应用-用户】(UniApplicationUser)创建【系统应用-用户】参数
 *
 * @author makejava
 * @since 2022-07-08 09:03:12
 */
@Data
public class UniApplicationUserAddRequest {

    @ApiModelProperty(value = "【系统用户ID】")
    private Long systemUserId;

    @ApiModelProperty(value = "【统一应用ID数组】")
    @NotEmpty
    private List<Long> uniAppIdList;


}

