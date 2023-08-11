package cn.qbs.wa.teach.course.standard.pojo.coursegroup;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 课程部门表(CourseDept)创建课程部门表参数
 *
 * @author makejava
 * @since 2022-08-24 9:07:56
 */
@Data
public class CourseGroupDeleteRequest extends IdListRequest {

	@ApiModelProperty(value = "部门id")
	private Long groupId;

}

