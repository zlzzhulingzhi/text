package cn.qbs.wa.train.logistics.pojo.invitationgroup;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 邀请分组表(InvitationGroup)更新邀请分组表参数
 *
 * @author makejava
 * @since 2022-06-20 19:16:20
 */
@Data
public class InvitationGroupUpdateRequest {

	@ApiModelProperty(value = "")
	private Long id;

	@ApiModelProperty(value = "机构id")
	private Long orgId;

	@ApiModelProperty(value = "邀请id")
	private Long invitationId;

	@ApiModelProperty(value = "分组id")
	private Long groupId;

}

