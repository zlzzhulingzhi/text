package cn.qbs.wa.teach.exam.admin.pojo.examdept;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.qbs.wa.teach.exam.common.entity.ExamDept;

/**
 * 考试部门表(ExamDept)考试部门表详情
 *
 * @author makejava
 * @since 2022-08-12 13:57:08
 */
@Data
public class ExamDeptDetailResponse extends ExamDept {

	@ApiModelProperty(value = "部门名称")
	private String deptName;

	@ApiModelProperty(value = "父部门id")
	private Long parentId;

}

