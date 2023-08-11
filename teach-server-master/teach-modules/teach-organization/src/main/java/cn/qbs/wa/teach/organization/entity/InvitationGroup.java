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
 * @since 2022-06-20 19:16:18
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InvitationGroup extends Model {

	private static final long serialVersionUID = 954910073185719450L;


	@ApiModelProperty(value = "")
	private Long id;

	@ApiModelProperty(value = "机构id")
	private Long orgId;

	@ApiModelProperty(value = "邀请id")
	private Long invitationId;

	@ApiModelProperty(value = "分组id")
	private Long groupId;

	@ApiModelProperty(value = "")
	@TableField(fill = FieldFill.INSERT)
	private Long createBy;

	@ApiModelProperty(value = "")
	@TableField(fill = FieldFill.INSERT)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private LocalDateTime createTime;

}
