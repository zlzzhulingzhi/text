package cn.qbs.wa.teach.organization.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author makejava
 * @since 2022-06-22 13:47:46
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ImportRecord extends Model {

	private static final long serialVersionUID = -25877815463262592L;


	@ApiModelProperty(value = "主键")
	private Long id;

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

	@ApiModelProperty(value = "创建人")
	@TableField(fill = FieldFill.INSERT)
	private Long createBy;

	@ApiModelProperty(value = "创建时间")
	@TableField(fill = FieldFill.INSERT)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private LocalDateTime createTime;

}
