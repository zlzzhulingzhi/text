package cn.qbs.wa.train.logistics.mapper;

import cn.qbs.wa.train.logistics.entity.AppOauth;
import cn.qbs.wa.train.logistics.pojo.appoauth.AppOAuthUserRequest;
import cn.qbs.wa.train.logistics.pojo.student.LoginInfoResponse;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户第三方应用登录绑定表(AppOauth)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2022-03-02 19:09:45
 */
public interface AppOauthMapper extends BaseMapper<AppOauth> {

    @InterceptorIgnore(tenantLine = "true")
    LoginInfoResponse getStudentLoginInfo(@Param("params") AppOAuthUserRequest oAuthUserRequest);

    @InterceptorIgnore(tenantLine = "true")
    LoginInfoResponse getEmployeeLoginInfo(@Param("params") AppOAuthUserRequest oAuthUserRequest);
}

