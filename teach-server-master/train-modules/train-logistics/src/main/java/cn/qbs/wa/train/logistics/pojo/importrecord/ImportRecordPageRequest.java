package cn.qbs.wa.train.logistics.pojo.importrecord;

import cn.qbs.wa.teach.domain.BasePageRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 导入记录表(ImportRecord)分页查询参数
 *
 * @author makejava
 * @since 2022-06-22 13:47:48
 */
@Data
public class ImportRecordPageRequest extends BasePageRequest {

	@ApiModelProperty(value = "机构id")
	private Long orgId;

	@ApiModelProperty(value = "业务类型 1-学员 2-职工")
	private Integer businessType;

	@ApiModelProperty(value = "导入时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private LocalDateTime importTime;

	@ApiModelProperty(value = "总数")
	private Integer total;

	@ApiModelProperty(value = "成功数量")
	private Integer success;

	@ApiModelProperty(value = "失败数量")
	private Integer failure;

}

