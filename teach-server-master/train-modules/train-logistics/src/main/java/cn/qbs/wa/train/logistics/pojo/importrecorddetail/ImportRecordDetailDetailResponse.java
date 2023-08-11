package cn.qbs.wa.train.logistics.pojo.importrecorddetail;

import cn.qbs.wa.train.logistics.entity.ImportRecordDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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

