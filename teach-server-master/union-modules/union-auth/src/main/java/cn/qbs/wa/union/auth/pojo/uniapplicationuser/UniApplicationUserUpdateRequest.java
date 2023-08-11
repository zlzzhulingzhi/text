package cn.qbs.wa.union.auth.pojo.uniapplicationuser;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 【系统应用-用户】(UniApplicationUser)更新【系统应用-用户】参数
 *
 * @author makejava
 * @since 2022-07-08 09:03:12
 */
@Data
public class UniApplicationUserUpdateRequest {

    @ApiModelProperty(value = "")
    private Long id;

    @ApiModelProperty(value = "【系统用户ID】")
    private Long systemUserId;

    @ApiModelProperty(value = "【统一应用ID】")
    private Long uniAppId;

    @ApiModelProperty(value = "【应用在常用应用中的排序】")
    private Integer sort;

}

