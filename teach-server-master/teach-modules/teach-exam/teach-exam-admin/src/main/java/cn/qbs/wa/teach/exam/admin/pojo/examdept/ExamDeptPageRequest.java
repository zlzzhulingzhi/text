package cn.qbs.wa.teach.exam.admin.pojo.examdept;


import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 考试部门表(ExamDept)分页查询参数
 *
 * @author makejava
 * @since 2022-08-12 13:57:07
 */
@Data
public class ExamDeptPageRequest extends BasePageRequest {

	@ApiModelProperty(value = "机构id")
	private Long orgId;

	@ApiModelProperty(value = "考试id")
	private Long examId;

	@ApiModelProperty(value = "部门id")
	private Long deptId;

}

