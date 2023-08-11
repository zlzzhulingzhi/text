package cn.qbs.wa.teach.course.standard.pojo.coursedept;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 课程部门表(CourseDept)创建课程部门表参数
 *
 * @author makejava
 * @since 2022-08-10 19:27:56
 */
@Data
public class CourseDeptBatchAddRequest {

	@ApiModelProperty(value = "课程部门集合")
	private List<CourseDeptAddRequest> courseDeptList;

}

