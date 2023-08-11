package cn.qbs.wa.train.logistics.entity;

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
 * @since 2022-06-20 19:16:48
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InvitationRecord extends Model {

	private static final long serialVersionUID = -86512684169450217L;


	@ApiModelProperty(value = "")
	private Long id;

	@ApiModelProperty(value = "机构id")
	private Long orgId;

	@ApiModelProperty(value = "业务类型 1学员")
	private Integer businessType;

	@ApiModelProperty(value = "业务id")
	private Long businessId;

	@ApiModelProperty(value = "邀请id")
	private Long invitationId;

	@ApiModelProperty(value = "邀请用户id")
	private Long inviteBy;

	@ApiModelProperty(value = "")
	@TableField(fill = FieldFill.INSERT)
	private Long createBy;

	@ApiModelProperty(value = "")
	@TableField(fill = FieldFill.INSERT)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private LocalDateTime createTime;

}
