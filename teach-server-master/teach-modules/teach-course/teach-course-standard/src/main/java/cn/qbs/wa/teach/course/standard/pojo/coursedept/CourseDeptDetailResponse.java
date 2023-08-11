package cn.qbs.wa.teach.course.standard.pojo.coursedept;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.qbs.wa.teach.course.common.entity.CourseDept;

/**
 * 课程部门表(CourseDept)课程部门表详情
 *
 * @author makejava
 * @since 2022-08-10 19:27:57
 */
@Data
public class CourseDeptDetailResponse extends CourseDept {

	@ApiModelProperty(value = "部门名称")
	private String deptName;

	@ApiModelProperty(value = "父部门id")
	private Long parentId;
}

