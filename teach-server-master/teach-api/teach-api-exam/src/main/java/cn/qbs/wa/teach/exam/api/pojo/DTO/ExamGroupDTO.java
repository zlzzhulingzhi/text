package cn.qbs.wa.teach.exam.api.pojo.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author makejava
 * @since 2022-08-12 17:22:05
 */
@Data
public class ExamGroupDTO {

	@ApiModelProperty(value = "主键id")
	private Long id;

	@ApiModelProperty(value = "机构id")
	private Long orgId;

	@ApiModelProperty(value = "考试id")
	private Long examId;

	@ApiModelProperty(value = "标签id")
	private Long groupId;

	@ApiModelProperty(value = "标签名称")
	private String groupName;

	@ApiModelProperty(value = "创建人")
	private Long createBy;

	@ApiModelProperty(value = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private LocalDateTime createTime;

}
