package cn.qbs.wa.train.logistics.pojo.importrecord;

import cn.qbs.wa.train.logistics.entity.ImportRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 导入记录表(ImportRecord)分页查询导入记录表响应
 *
 * @author makejava
 * @since 2022-06-22 13:47:49
 */
@Data
public class ImportRecordPageResponse extends ImportRecord {

	@ApiModelProperty(value = "导入操作人姓名")
	private String createName;

	@ApiModelProperty(value = "操作人手机号")
	private String phone;

}

