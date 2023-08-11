package cn.qbs.wa.teach.course.standard.pojo.coursegroup;

import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

/**
 * 课程标签表(CourseGroup)分页查询参数
 *
 * @author makejava
 * @since 2022-08-11 15:23:58
 */
@Data
public class CourseGroupPageRequest extends BasePageRequest {

	@ApiModelProperty(value = "机构id")
	private Long orgId;

	@ApiModelProperty(value = "课程id")
	private Long courseId;

	@ApiModelProperty(value = "标签id")
	private Long groupId;

}

