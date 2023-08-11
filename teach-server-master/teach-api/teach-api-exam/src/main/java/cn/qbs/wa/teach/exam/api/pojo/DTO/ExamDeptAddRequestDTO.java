package cn.qbs.wa.teach.exam.api.pojo.DTO;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 考试部门表(ExamDept)创建考试部门表参数
 *
 * @author makejava
 * @since 2022-08-12 13:57:07
 */
@Data
public class ExamDeptAddRequestDTO {

	@ApiModelProperty(value = "机构id")
	private Long orgId;

	@ApiModelProperty(value = "考试id")
	private Long examId;

	@ApiModelProperty(value = "部门id")
	private Long deptId;

	@ApiModelProperty(value = "部门名称（子级名称为null）")
	private String deptName;

}
