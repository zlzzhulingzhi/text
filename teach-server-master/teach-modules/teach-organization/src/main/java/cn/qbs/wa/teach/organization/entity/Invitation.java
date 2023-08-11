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
 * @since 2022-06-20 19:10:50
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Invitation extends Model {

	private static final long serialVersionUID = 486459519909846159L;


	@ApiModelProperty(value = "")
	private Long id;

	@ApiModelProperty(value = "机构id")
	private Long orgId;

	@ApiModelProperty(value = "业务类型 1学员")
	private Integer businessType;

	@ApiModelProperty(value = "开始时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private LocalDateTime startTime;

	@ApiModelProperty(value = "结束时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private LocalDateTime endTime;

	@ApiModelProperty(value = "0 禁用 1启用")
	private Integer enabled;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value = "邀请码 雪花算法")
	private String invitationKey;

	@ApiModelProperty(value = "邀请密码")
	private String invitationPassword;

	@ApiModelProperty(value = "")
	@TableField(fill = FieldFill.INSERT)
	private Long createBy;

	@ApiModelProperty(value = "")
	@TableField(fill = FieldFill.INSERT)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private LocalDateTime createTime;

	@ApiModelProperty(value = "")
	@TableField(fill = FieldFill.UPDATE)
	private Long updateBy;

	@ApiModelProperty(value = "")
	@TableField(fill = FieldFill.UPDATE)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private LocalDateTime updateTime;

}
