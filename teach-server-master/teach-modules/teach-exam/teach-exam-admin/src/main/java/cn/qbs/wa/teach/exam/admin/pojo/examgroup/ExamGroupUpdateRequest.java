package cn.qbs.wa.teach.exam.admin.pojo.examgroup;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 考试标签表(ExamGroup)更新考试标签表参数
 *
 * @author makejava
 * @since 2022-08-12 17:22:07
 */
@Data
public class ExamGroupUpdateRequest {

	@ApiModelProperty(value = "主键id")
	private Long id;

	@ApiModelProperty(value = "机构id")
	private Long orgId;

	@ApiModelProperty(value = "考试id")
	private Long examId;

	@ApiModelProperty(value = "标签id")
	private Long groupId;

}

