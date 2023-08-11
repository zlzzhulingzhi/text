package cn.qbs.wa.teach.exam.admin.pojo.examgroup;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 考试标签表(ExamGroup)创建考试标签表参数
 *
 * @author makejava
 * @since 2022-08-12 17:22:07
 */
@Data
public class ExamGroupBatchAddRequest {

	@ApiModelProperty(value = "考试标签集合")
	private List<ExamGroupAddRequest> examGroupList;

}

