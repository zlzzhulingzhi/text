package cn.qbs.wa.teach.course.standard.pojo.coursegroup;

import lombok.Data;
import io.swagger.annotations.ApiModelProperty;

/**
 * 课程标签表(CourseGroup)更新课程标签表参数
 *
 * @author makejava
 * @since 2022-08-11 15:23:59
 */
@Data
public class CourseGroupUpdateRequest {

	@ApiModelProperty(value = "主键id")
	private Long id;

	@ApiModelProperty(value = "机构id")
	private Long orgId;

	@ApiModelProperty(value = "课程id")
	private Long courseId;

	@ApiModelProperty(value = "标签id")
	private Long groupId;

}

