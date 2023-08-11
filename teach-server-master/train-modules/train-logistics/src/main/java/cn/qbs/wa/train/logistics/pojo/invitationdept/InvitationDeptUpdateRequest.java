package cn.qbs.wa.train.logistics.pojo.invitationdept;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 邀请部门表(InvitationDept)更新邀请部门表参数
 *
 * @author makejava
 * @since 2022-06-20 19:11:32
 */
@Data
public class InvitationDeptUpdateRequest {

	@ApiModelProperty(value = "")
	private Long id;

	@ApiModelProperty(value = "机构id")
	private Long orgId;

	@ApiModelProperty(value = "邀请id")
	private Long invitationId;

	@ApiModelProperty(value = "部门id")
	private Long deptId;

}

