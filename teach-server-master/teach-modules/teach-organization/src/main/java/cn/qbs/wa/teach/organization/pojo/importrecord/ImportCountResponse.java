package cn.qbs.wa.teach.organization.pojo.importrecord;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 导入记录表(ImportRecord)导入记录表详情
 *
 * @author makejava
 * @since 2022-06-22 13:47:49
 */
@Data
public class ImportCountResponse {

	@ApiModelProperty(value = "导入记录id")
	private Long id;

	@ApiModelProperty(value = "成功数量")
	private Integer success;

	@ApiModelProperty(value = "失败数量")
	private Integer failure;
}

