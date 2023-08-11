package cn.qbs.wa.union.admin.pojo.uniapplicationuser;


import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 【系统应用-用户】(UniApplicationUser)分页查询参数
 *
 * @author makejava
 * @since 2022-07-08 09:03:11
 */
@Data
public class UniApplicationUserPageRequest extends BasePageRequest {

    @ApiModelProperty(value = "【系统用户ID】")
    private Long systemUserId;

    @ApiModelProperty(value = "【统一应用ID】")
    private Long uniAppId;

    @ApiModelProperty(value = "【应用在常用应用中的排序】")
    private Integer sort;

}

