package cn.qbs.wa.teach.exam.api.pojo.DTO;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 考试标签表(ExamGroup)创建考试标签表参数
 *
 * @author makejava
 * @since 2022-08-12 17:22:07
 */
@Data
public class ExamGroupAddRequestDTO {

	@ApiModelProperty(value = "机构id")
	private Long orgId;

	@ApiModelProperty(value = "考试id")
	private Long examId;

	@ApiModelProperty(value = "标签id")
	private Long groupId;

}

