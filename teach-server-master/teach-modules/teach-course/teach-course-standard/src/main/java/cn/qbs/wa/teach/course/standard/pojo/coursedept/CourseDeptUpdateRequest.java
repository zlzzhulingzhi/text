package cn.qbs.wa.teach.course.standard.pojo.coursedept;

import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 课程部门表(CourseDept)更新课程部门表参数
 *
 * @author makejava
 * @since 2022-08-10 19:27:56
 */
@Data
public class CourseDeptUpdateRequest {

	@ApiModelProperty(value = "主键id")
	private Long id;

	@ApiModelProperty(value = "机构id")
	private Long orgId;

	@ApiModelProperty(value = "课程id")
	private Long courseId;

	@ApiModelProperty(value = "部门id")
	private Long deptId;

}

