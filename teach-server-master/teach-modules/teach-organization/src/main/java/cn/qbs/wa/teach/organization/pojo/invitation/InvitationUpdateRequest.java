package cn.qbs.wa.teach.organization.pojo.invitation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

/**
 * 邀请表(Invitation)更新邀请表参数
 *
 * @author makejava
 * @since 2022-06-20 19:10:53
 */
@Data
public class InvitationUpdateRequest {

	@ApiModelProperty(value = "")
	private Long id;

	@ApiModelProperty(value = "机构id")
	private Long orgId;

	@ApiModelProperty(value = "业务类型 1学员")
	private Integer businessType;

	@ApiModelProperty(value = "开始时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private LocalDateTime startTime;

	@ApiModelProperty(value = "结束时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private LocalDateTime endTime;

	@ApiModelProperty(value = "0 禁用 1启用")
	private Integer enabled;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value = "邀请码 雪花算法")
	private String invitationKey;

	@ApiModelProperty(value = "邀请密码")
	private String invitationPassword;

}

