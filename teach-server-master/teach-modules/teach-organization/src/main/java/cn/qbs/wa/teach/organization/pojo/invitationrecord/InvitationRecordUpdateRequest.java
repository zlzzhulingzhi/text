package cn.qbs.wa.teach.organization.pojo.invitationrecord;

import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 受邀记录表(InvitationRecord)更新受邀记录表参数
 *
 * @author makejava
 * @since 2022-06-20 19:16:50
 */
@Data
public class InvitationRecordUpdateRequest {

	@ApiModelProperty(value = "")
	private Long id;

	@ApiModelProperty(value = "机构id")
	private Long orgId;

	@ApiModelProperty(value = "业务类型 1学员")
	private Integer businessType;

	@ApiModelProperty(value = "业务id")
	private Long businessId;

	@ApiModelProperty(value = "邀请id")
	private Long invitationId;

	@ApiModelProperty(value = "邀请用户id")
	private Long inviteBy;

}

