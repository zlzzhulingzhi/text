package cn.qbs.wa.teach.organization.pojo.invitationdept;

import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 邀请部门表(InvitationDept)分页查询参数
 *
 * @author makejava
 * @since 2022-06-20 19:11:31
 */
@Data
public class InvitationDeptPageRequest extends BasePageRequest {

	@ApiModelProperty(value = "机构id")
	private Long orgId;

	@ApiModelProperty(value = "邀请id")
	private Long invitationId;

	@ApiModelProperty(value = "部门id")
	private Long deptId;

}

