package cn.qbs.wa.teach.course.standard.pojo.coursedept;

import lombok.Data;
import io.swagger.annotations.ApiModelProperty;
import cn.qbs.wa.teach.domain.BasePageRequest;

import java.util.List;

/**
 * 课程部门表(CourseDept)分页查询参数
 *
 * @author makejava
 * @since 2022-08-10 19:27:56
 */
@Data
public class CourseDeptPageRequest extends BasePageRequest {

	@ApiModelProperty(value = "机构id")
	private Long orgId;

	@ApiModelProperty(value = "课程id")
	private Long courseId;

	@ApiModelProperty(value = "上架状态 0下架 1上架")
	private Integer shelfStatus;

	@ApiModelProperty(value = "是否精品 0-否 1-是")
	private Integer gooded;

	@ApiModelProperty(value = "课程类型 record-录播 live-直播 mix-综合")
	private String courseType;

	@ApiModelProperty(value = "课程名称")
	private String courseName;

	@ApiModelProperty(value = "讲师")
	private String lecturerName;

	@ApiModelProperty(value = "【分类ID集合】")
	private List<Long> categoryIdList;

	@ApiModelProperty(value = "部门id集合")
	private List<Long> deptIdList;

}

