package cn.qbs.wa.teach.exam.admin.pojo.examdept;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 考试部门表(ExamDept)更新考试部门表参数
 *
 * @author makejava
 * @since 2022-08-12 13:57:07
 */
@Data
public class ExamDeptUpdateRequest {

	@ApiModelProperty(value = "主键id")
	private Long id;

	@ApiModelProperty(value = "机构id")
	private Long orgId;

	@ApiModelProperty(value = "考试id")
	private Long examId;

	@ApiModelProperty(value = "部门id")
	private Long deptId;

}

