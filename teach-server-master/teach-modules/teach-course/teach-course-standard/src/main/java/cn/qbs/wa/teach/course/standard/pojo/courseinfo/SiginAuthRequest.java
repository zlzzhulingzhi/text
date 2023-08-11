package cn.qbs.wa.teach.course.standard.pojo.courseinfo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 课程报名密令验证参数
 * @Author: wx
 * @Date: 2022/6/25 10:52
 */
@Data
public class SiginAuthRequest {

	@NotNull(message = "课程id不能为空")
	@ApiModelProperty(value = "【课程id】")
	private Long courseId;

	@NotBlank(message = "口令不能为空")
	@ApiModelProperty(value = "【口令】")
	private String signUpAuthValue;
}
