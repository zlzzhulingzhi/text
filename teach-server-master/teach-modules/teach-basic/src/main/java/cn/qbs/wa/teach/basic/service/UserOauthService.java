package cn.qbs.wa.teach.basic.service;

import cn.qbs.wa.teach.basic.entity.User;
import cn.qbs.wa.teach.basic.entity.UserOauth;
import cn.qbs.wa.teach.basic.pojo.user.UserInfoResponse;
import cn.qbs.wa.teach.basic.pojo.useroauth.OauthAccountRequest;
import cn.qbs.wa.teach.basic.pojo.useroauth.OauthInfoResponse;
import cn.qbs.wa.teach.basic.pojo.useroauth.UserAccountRequest;
import cn.qbs.wa.teach.basic.pojo.useroauth.UserOauthBindingRequest;
import cn.qbs.wa.teach.common.core.domain.LoginUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户绑定微信关系表(UserOauth)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2022-03-04 10:21:49
 */
public interface UserOauthService extends IService<UserOauth> {

    User getUserInfo(OauthAccountRequest request);

    OauthInfoResponse getUserInfo(Long userId);

    User bindingSocial(UserOauthBindingRequest request);

    Boolean checkPhoneBind(UserAccountRequest request);

    Boolean unbindSocial(Long userId);
}

