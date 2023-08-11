package cn.qbs.wa.teach.organization.service;

import cn.qbs.wa.teach.organization.entity.AppOauth;
import cn.qbs.wa.teach.organization.pojo.appoauth.AppOAuthUserRequest;
import cn.qbs.wa.teach.organization.pojo.student.LoginInfoResponse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户第三方应用登录绑定表(AppOauth)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2022-03-02 19:09:45
 */
public interface AppOauthService extends IService<AppOauth> {

    LoginInfoResponse getLoginInfo(AppOAuthUserRequest oAuthUserRequest);
}

