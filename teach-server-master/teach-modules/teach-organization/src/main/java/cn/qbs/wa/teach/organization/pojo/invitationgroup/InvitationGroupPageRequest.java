package cn.qbs.wa.teach.organization.pojo.invitationgroup;

import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 邀请分组表(InvitationGroup)分页查询参数
 *
 * @author makejava
 * @since 2022-06-20 19:16:19
 */
@Data
public class InvitationGroupPageRequest extends BasePageRequest {

	@ApiModelProperty(value = "机构id")
	private Long orgId;

	@ApiModelProperty(value = "邀请id")
	private Long invitationId;

	@ApiModelProperty(value = "分组id")
	private Long groupId;

}

