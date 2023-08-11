package cn.qbs.wa.union.auth.service;

import cn.qbs.wa.union.auth.entity.UniUserOauth;
import cn.qbs.wa.union.auth.pojo.login.UserAccountRequest;
import cn.qbs.wa.union.auth.pojo.login.UserOauthBindingRequest;
import cn.qbs.wa.union.auth.pojo.login.WechatBindingRequest;
import cn.qbs.wa.union.auth.pojo.login.WechatUnBindingRequest;
import cn.qbs.wa.union.auth.pojo.uniuser.UniUserDTO;
import cn.qbs.wa.teach.common.core.domain.LoginUser;
import com.baomidou.mybatisplus.extension.service.IService;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;

import java.util.List;

/**
 * 用户第三方应用登录绑定表(UniUserOauth)表服务接口
 *
 * @author makejava
 * @since 2022-07-12 13:53:43
 */
public interface UniUserOauthService extends IService<UniUserOauth> {


    /**
     * 删除用户第三方应用登录绑定表
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    LoginUser bindingByWechat(WechatBindingRequest request, WxOAuth2UserInfo wxUserInfo, UniUserDTO userId);

    void unBindingByWechat(WechatUnBindingRequest request);

    void bindingSocial(UserOauthBindingRequest request);

    Boolean unbindSocial(Long userId);

    Boolean checkBind(UserAccountRequest request);
}

