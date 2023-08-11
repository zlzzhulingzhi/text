package cn.qbs.wa.train.basic.service;

import cn.qbs.wa.train.basic.entity.User;
import cn.qbs.wa.train.basic.entity.UserOauth;
import cn.qbs.wa.train.basic.pojo.useroauth.OauthAccountRequest;
import cn.qbs.wa.train.basic.pojo.useroauth.OauthInfoResponse;
import cn.qbs.wa.train.basic.pojo.useroauth.UserAccountRequest;
import cn.qbs.wa.train.basic.pojo.useroauth.UserOauthBindingRequest;
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

