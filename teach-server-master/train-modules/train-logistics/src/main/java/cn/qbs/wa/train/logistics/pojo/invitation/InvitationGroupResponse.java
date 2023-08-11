package cn.qbs.wa.train.logistics.pojo.invitation;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 邀请表分页分组详情
 *
 * @author makejava
 * @since 2022-06-20 19:16:20
 */
@Data
public class InvitationGroupResponse {

	@ApiModelProperty(value = "分组id")
	private Long invGroupId;

	@ApiModelProperty(value = "标签id")
	private Long groupId;

	@ApiModelProperty(value = "标签名称")
	private String groupName;
}

