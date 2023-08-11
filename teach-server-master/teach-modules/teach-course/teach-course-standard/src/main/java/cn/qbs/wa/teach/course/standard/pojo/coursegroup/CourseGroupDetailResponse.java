package cn.qbs.wa.teach.course.standard.pojo.coursegroup;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.qbs.wa.teach.course.common.entity.CourseGroup;

/**
 * 课程标签表(CourseGroup)课程标签表详情
 *
 * @author makejava
 * @since 2022-08-11 15:23:59
 */
@Data
public class CourseGroupDetailResponse extends CourseGroup {

	@ApiModelProperty(value = "标签名称")
	private String groupName;

}

