package cn.qbs.wa.teach.organization.pojo.invitation;

import cn.qbs.wa.teach.common.core.domain.LoginUser;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 邀请表(Invitation)邀请表详情
 *
 * @author makejava
 * @since 2022-06-20 19:10:53
 */
@Data
public class LoginUserAndPhoneIsExist extends LoginUser {

	@ApiModelProperty(value = "手机号是否已注册")
	private boolean phoneIsExist = false;

}

