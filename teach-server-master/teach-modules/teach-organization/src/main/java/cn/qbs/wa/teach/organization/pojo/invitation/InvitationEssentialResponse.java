package cn.qbs.wa.teach.organization.pojo.invitation;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 邀请表基本信息
 *
 * @author makejava
 * @since 2022-06-20 19:10:53
 */
@Data
public class InvitationEssentialResponse {

	@ApiModelProperty(value = "")
	private Long id;

	@ApiModelProperty(value = "机构名称")
	private String orgName;

	@ApiModelProperty(value = "开始时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private LocalDateTime startTime;

	@ApiModelProperty(value = "结束时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private LocalDateTime endTime;

	@ApiModelProperty(value = "备注")
	private String remark;

}

