package cn.qbs.wa.teach.basic.api.factory;

import cn.qbs.wa.teach.basic.api.RemoteUserOauthService;
import cn.qbs.wa.teach.basic.api.pojo.DTO.oauth.SocialBindingDTO;
import cn.qbs.wa.teach.basic.api.pojo.DTO.oauth.SocialInfoDTO;
import cn.qbs.wa.teach.basic.api.pojo.DTO.user.UserInfoDTO;
import cn.qbs.wa.teach.common.core.domain.LoginUser;
import cn.qbs.wa.teach.common.core.domain.R;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author yjx
 * @version 1.0
 * @date 2022/03/04 15:03
 */
@Component
public class RemoteUserOauthFallbackFactory implements FallbackFactory<RemoteUserOauthService> {
    @Override
    public RemoteUserOauthService create(Throwable cause) {
        return new RemoteUserOauthService() {

            @Override
            public R<LoginUser> getSocialLoginInfo(SocialInfoDTO socialInfoDTO) {
                return null;
            }

            @Override
            public R<LoginUser> bindingSocial(SocialBindingDTO socialBindingDTO) {
                return null;
            }

            @Override
            public R<Boolean> getPhoneBindingInfo(UserInfoDTO userInfoDTO) {
                return null;
            }
        };
    }
}
