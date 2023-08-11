package cn.qbs.wa.teach.exam.admin.pojo.examgroup;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.qbs.wa.teach.exam.common.entity.ExamGroup;

/**
 * 考试标签表(ExamGroup)考试标签表详情
 *
 * @author makejava
 * @since 2022-08-12 17:22:07
 */
@Data
public class ExamGroupDetailResponse extends ExamGroup {

	@ApiModelProperty(value = "标签名称")
	private String groupName;
}

