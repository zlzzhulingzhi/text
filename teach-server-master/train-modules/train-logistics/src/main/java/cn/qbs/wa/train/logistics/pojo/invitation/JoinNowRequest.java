package cn.qbs.wa.train.logistics.pojo.invitation;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 扫码后点立即加入功能参数
 *
 * @author WX
 * @since 2022-06-20 19:10:52
 */
@Data
public class JoinNowRequest {

	@ApiModelProperty(value = "业务类型 1学员")
	private Integer businessType;

	@ApiModelProperty(value = "手机号")
	@NotBlank(message = "手机号不能为空")
	private String phone;

	@ApiModelProperty(value = "邀请码")
	private String invitationKey;

	@ApiModelProperty(value = "邀请密码")
	private String invitationPassword;

	@ApiModelProperty(value = "短信验证码")
	private String sms;

}

