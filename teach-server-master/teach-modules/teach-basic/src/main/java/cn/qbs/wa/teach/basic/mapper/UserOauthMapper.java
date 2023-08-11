package cn.qbs.wa.teach.basic.mapper;

import cn.qbs.wa.teach.basic.entity.User;
import cn.qbs.wa.teach.basic.entity.UserOauth;
import cn.qbs.wa.teach.basic.pojo.useroauth.OauthInfoResponse;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户绑定微信关系表(UserOauth)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2022-03-04 10:21:49
 */
public interface UserOauthMapper extends BaseMapper<UserOauth> {

    @InterceptorIgnore(tenantLine = "true")
    User getUserInfo(@Param("uid") String uid);

    @InterceptorIgnore(tenantLine = "true")
    OauthInfoResponse getOauthInfo(@Param("userId") Long userId);
}

