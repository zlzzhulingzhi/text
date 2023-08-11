package cn.qbs.wa.teach.organization.pojo.invitationgroup;

import cn.qbs.wa.teach.organization.entity.InvitationGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 邀请分组表(InvitationGroup)邀请分组表详情
 *
 * @author makejava
 * @since 2022-06-20 19:16:20
 */
@Data
public class InvitationGroupListResponse {

	@ApiModelProperty(value = "邀请Id")
	private Long invitaionId;

	@ApiModelProperty(value = "邀请分组id")
	private Long invGroupId;

	@ApiModelProperty(value = "分组id")
	private Long groupId;

	@ApiModelProperty(value = "分组名称")
	private String groupName;
}

