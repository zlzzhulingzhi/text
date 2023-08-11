package cn.qbs.wa.teach.course.api.pojo.DTO.coursedept;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 课程部门表(CourseDept)创建课程部门表参数
 *
 * @author makejava
 * @since 2022-08-10 19:27:56
 */
@Data
public class CourseDeptAddRequestDTO {

	@ApiModelProperty(value = "机构id")
	private Long orgId;

	@ApiModelProperty(value = "课程id")
	private Long courseId;

	@ApiModelProperty(value = "部门id")
	private Long deptId;

}

