package cn.qbs.wa.teach.exam.api.pojo.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 考试部门表(ExamDept)创建考试部门表参数
 *
 * @author makejava
 * @since 2022-08-12 13:57:07
 */
@Data
public class ExamDeptBatchAddRequestDTO {

	@ApiModelProperty(value = "考试部门集合")
	private List<ExamDeptAddRequestDTO> examDeptList;

}
