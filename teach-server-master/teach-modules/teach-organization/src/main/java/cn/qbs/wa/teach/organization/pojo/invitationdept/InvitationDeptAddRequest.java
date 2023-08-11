package cn.qbs.wa.teach.organization.pojo.invitationdept;

import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 邀请部门表(InvitationDept)创建邀请部门表参数
 *
 * @author makejava
 * @since 2022-06-20 19:11:31
 */
@Data
public class InvitationDeptAddRequest {

	@ApiModelProperty(value = "机构id")
	private Long orgId;

	@ApiModelProperty(value = "邀请id")
	private Long invitationId;

	@ApiModelProperty(value = "部门id")
	private Long deptId;

}

