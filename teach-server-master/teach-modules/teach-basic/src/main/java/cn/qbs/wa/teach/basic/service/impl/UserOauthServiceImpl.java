package cn.qbs.wa.teach.basic.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.basic.entity.User;
import cn.qbs.wa.teach.basic.entity.UserOauth;
import cn.qbs.wa.teach.basic.mapper.UserMapper;
import cn.qbs.wa.teach.basic.mapper.UserOauthMapper;
import cn.qbs.wa.teach.basic.pojo.useroauth.OauthAccountRequest;
import cn.qbs.wa.teach.basic.pojo.useroauth.OauthInfoResponse;
import cn.qbs.wa.teach.basic.pojo.useroauth.UserAccountRequest;
import cn.qbs.wa.teach.basic.pojo.useroauth.UserOauthBindingRequest;
import cn.qbs.wa.teach.basic.service.UserOauthService;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户绑定微信关系表(UserOauth)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2022-03-04 10:21:49
 */
@Slf4j
@Service("userOauthService")
public class UserOauthServiceImpl extends ServiceImpl<UserOauthMapper, UserOauth> implements UserOauthService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User getUserInfo(OauthAccountRequest request) {
        return this.baseMapper.getUserInfo(request.getUid());
    }

    @Override
    public OauthInfoResponse getUserInfo(Long userId) {
        return this.baseMapper.getOauthInfo(userId);
    }

    @Override
    public User bindingSocial(UserOauthBindingRequest request) {
        User user;
        if (StrUtil.isBlank(request.getAccount())) {
            user = userMapper.selectById(SecurityContextHolder.getUserId());
        } else {
            user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getAccount, AesUtil.encode(request.getAccount())));
        }
        if (checkUser(user)) {
            throw new ServiceException("账号不存在");
        }
        UserOauth userOauth = new UserOauth();
        userOauth.setUid(request.getUid());
        userOauth.setUserId(user.getId());
        try {
            this.save(userOauth);
        } catch (Exception e) {
            throw new ServiceException("该微信用户已绑定其他账号");
        }
        if (StrUtil.isBlank(user.getHeadImgUrl())) {
            user.setHeadImgUrl(request.getHeadImgUrl());
            userMapper.updateById(user);
        }
        return user;
    }

    @Override
    public Boolean unbindSocial(Long userId) {
        this.baseMapper.delete(Wrappers.<UserOauth>lambdaQuery().eq(UserOauth::getUserId, userId));
        return Boolean.TRUE;
    }

    @Override
    public Boolean checkPhoneBind(UserAccountRequest request) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getAccount, AesUtil.encode(request.getAccount())));
        if (checkUser(user)) {
            return Boolean.TRUE;
        }
        Long oauthCount = baseMapper.selectCount(Wrappers.<UserOauth>lambdaQuery().eq(UserOauth::getUserId, user.getId()));
        return oauthCount != null && oauthCount > 0;
    }

    private boolean checkUser(User user) {
        if (user == null) {
            return true;
        }
        if (Constants.DB_FAIL.equals(user.getEnabled())) {
            throw new ServiceException("该账号已禁用");
        }
        return false;
    }

}

