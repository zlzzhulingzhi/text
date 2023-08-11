package cn.qbs.wa.train.logistics.pojo.invitationrecord;

import cn.qbs.wa.teach.domain.BasePageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 受邀记录表(InvitationRecord)分页查询参数
 *
 * @author makejava
 * @since 2022-06-20 19:16:50
 */
@Data
public class InvitationRecordPageRequest extends BasePageRequest {

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

