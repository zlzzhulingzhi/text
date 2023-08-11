package cn.qbs.wa.teach.course.api.pojo.DTO.lecturer;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 【课程讲师】(CourseLecturer)【课程讲师】详情
 *
 * @author makejava
 * @version 1.0
 * @date 2022-06-09 10:24:39
 */
@Data
public class CourseLecturerDetailDTO {

	@ApiModelProperty(value = "【主键标识】")
	private Long id;

	@ApiModelProperty(value = "【组织机构ID】")
	private Long orgId;

	@ApiModelProperty(value = "【课程ID】")
	private Long courseId;

	@ApiModelProperty(value = "【讲师ID】")
	private Long lecturerId;

	@ApiModelProperty(value = "【讲师姓名】")
	private String lecturerName;

	@ApiModelProperty(value = "【讲师头像】")
	private String lecturerHeadImgUrl;

	@ApiModelProperty(value = "【讲师简介】")
	private String lecturerIntro;

	@ApiModelProperty(value = "【创建人ID】")
	private Long createBy;

	@ApiModelProperty(value = "【创建时间】")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private LocalDateTime createTime;
}

