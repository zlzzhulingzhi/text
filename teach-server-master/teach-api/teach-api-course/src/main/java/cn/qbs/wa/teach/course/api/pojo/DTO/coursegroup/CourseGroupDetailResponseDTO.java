package cn.qbs.wa.teach.course.api.pojo.DTO.coursegroup;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 课程标签表(CourseGroup)课程标签表详情
 *
 * @author makejava
 * @since 2022-08-11 15:23:59
 */
@Data
public class CourseGroupDetailResponseDTO {

	@ApiModelProperty(value = "主键id")
	private Long id;

	@ApiModelProperty(value = "机构id")
	private Long orgId;

	@ApiModelProperty(value = "课程id")
	private Long courseId;

	@ApiModelProperty(value = "标签id")
	private Long groupId;

}

