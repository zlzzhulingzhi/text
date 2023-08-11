package cn.qbs.wa.teach.course.standard.pojo.coursegroup;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 课程标签表(CourseGroup)创建课程标签表参数
 *
 * @author makejava
 * @since 2022-08-11 15:23:59
 */
@Data
public class CourseGroupBatchAddRequest {

	@ApiModelProperty(value = "课程标签集合")
	private List<CourseGroupAddRequest> courseGroupList;

}

