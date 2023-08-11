package cn.qbs.wa.teach.organization.pojo.student;

import cn.qbs.wa.teach.common.security.annotation.EncodeContent;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yjx
 */
@Data
public class StudentInfoResponse {

	@ApiModelProperty(value = "【主键】")
	private Long id;

	@ApiModelProperty(value = "【机构ID】")
	private Long orgId;

	@ApiModelProperty(value = "【会员ID】")
	private Long memberId;

	@ApiModelProperty(value = "【账号】")
	@EncodeContent
	private String account;

	@ApiModelProperty(value = "【手机号】")
	private String phone;

	@ApiModelProperty(value = "【密码】")
	private String password;

	@ApiModelProperty(value = "【真实姓名】")
	private String realName;

	@ApiModelProperty(value = "【真实姓名】")
	private String nickName;

	@ApiModelProperty(value = "【头像地址】")
	private String headImgUrl;

	@ApiModelProperty(value = "【0表示账号不可用  1表示账号可用】")
	private Integer enabled;

	@ApiModelProperty(value = "邮箱")
	private String email;

	@ApiModelProperty(value = "第三方应用的用户唯一ID")
	private String uid;

	@ApiModelProperty(value = "第三方应用的用户标识")
	private String openId;

	@ApiModelProperty(value = "资源学员id")
	private Long crmStudentId;

}
