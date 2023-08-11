package cn.qbs.wa.teach.exam.admin.pojo.examgroup;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 考试标签表(ExamGroup)分页查询参数
 *
 * @author makejava
 * @since 2022-08-12 17:22:06
 */
@Data
public class ExamGroupPageRequest extends BasePageRequest {

	@ApiModelProperty(value = "机构id")
	private Long orgId;

	@ApiModelProperty(value = "考试id")
	private Long examId;

	@ApiModelProperty(value = "标签id")
	private Long groupId;

}

