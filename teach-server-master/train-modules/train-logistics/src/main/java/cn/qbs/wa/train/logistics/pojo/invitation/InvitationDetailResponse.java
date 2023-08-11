package cn.qbs.wa.train.logistics.pojo.invitation;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 邀请表(Invitation)邀请表详情
 *
 * @author makejava
 * @since 2022-06-20 19:10:53
 */
@Data
public class InvitationDetailResponse {

	@ApiModelProperty(value = "")
	private Long id;

	@ApiModelProperty(value = "机构id")
	private Long orgId;

	@ApiModelProperty(value = "部门id")
	private Long deptId;

	@ApiModelProperty(value = "创建人id")
	private Long createBy;

	@ApiModelProperty(value = "邀请-部门id")
	private Long invDeptId;

	@ApiModelProperty(value = "机构名称")
	private String orgName;

	@ApiModelProperty(value = "部门名称")
	private String fullName;

	@ApiModelProperty(value = "标签集")
	private List<InvitationGroupResponse> groupList;

	@ApiModelProperty(value = "业务类型 1学员")
	private Integer businessType;

	@ApiModelProperty(value = "开始时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private LocalDateTime startTime;

	@ApiModelProperty(value = "结束时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private LocalDateTime endTime;

	@ApiModelProperty(value = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private LocalDateTime createTime;

	@ApiModelProperty(value = "0 禁用 1启用")
	private Integer enabled;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value = "邀请码")
	private String invitationKey;

	@ApiModelProperty(value = "邀请密码")
	private String invitationPassword;

}

