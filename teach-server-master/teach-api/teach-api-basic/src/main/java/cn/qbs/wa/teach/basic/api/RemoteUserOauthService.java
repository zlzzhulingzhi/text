package cn.qbs.wa.teach.basic.api;

import cn.qbs.wa.teach.basic.api.factory.RemoteUserOauthFallbackFactory;
import cn.qbs.wa.teach.basic.api.pojo.DTO.oauth.SocialBindingDTO;
import cn.qbs.wa.teach.basic.api.pojo.DTO.oauth.SocialInfoDTO;
import cn.qbs.wa.teach.basic.api.pojo.DTO.user.UserInfoDTO;
import cn.qbs.wa.teach.common.core.domain.LoginUser;
import cn.qbs.wa.teach.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author yjx
 * @version 1.0
 * @date 2022/03/04 15:02
 */
@FeignClient(contextId = "remoteUserOauthService", name = "teach-basic", path = "basic/userOauth", fallbackFactory = RemoteUserOauthFallbackFactory.class)
public interface RemoteUserOauthService {

    @PostMapping("/login-social")
    R<LoginUser> getSocialLoginInfo(@RequestBody SocialInfoDTO socialInfoDTO);

    @PostMapping("/binding-social")
    R<LoginUser> bindingSocial(@RequestBody SocialBindingDTO socialBindingDTO);

    @PostMapping("/binding-check")
    R<Boolean> getPhoneBindingInfo(@RequestBody UserInfoDTO userInfoDTO);
}
