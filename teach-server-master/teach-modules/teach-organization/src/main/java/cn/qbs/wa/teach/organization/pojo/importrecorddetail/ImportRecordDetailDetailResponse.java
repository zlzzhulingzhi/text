package cn.qbs.wa.teach.organization.pojo.importrecorddetail;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import cn.qbs.wa.teach.organization.entity.ImportRecordDetail;

/**
 * 导入记录详情表(ImportRecordDetail)导入记录详情表详情
 *
 * @author makejava
 * @since 2022-06-22 13:48:26
 */
@Data
public class ImportRecordDetailDetailResponse extends ImportRecordDetail {

	@ApiModelProperty(value = "所属部门id")
	private Long deptId;

	@ApiModelProperty(value = "性别")
	private String sexName;
}

