package cn.qbs.wa.union.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.union.auth.entity.SystemApplication;
import cn.qbs.wa.union.auth.entity.SystemEmployeeUser;
import cn.qbs.wa.union.auth.entity.SystemUser;
import cn.qbs.wa.union.auth.entity.UniUserOauth;
import cn.qbs.wa.union.auth.enumerate.SocialPlatformEnum;
import cn.qbs.wa.union.auth.mapper.UniUserOauthMapper;
import cn.qbs.wa.union.auth.pojo.login.UserAccountRequest;
import cn.qbs.wa.union.auth.pojo.login.UserOauthBindingRequest;
import cn.qbs.wa.union.auth.pojo.login.WechatBindingRequest;
import cn.qbs.wa.union.auth.pojo.login.WechatUnBindingRequest;
import cn.qbs.wa.union.auth.pojo.uniuser.UniUserDTO;
import cn.qbs.wa.union.auth.service.SystemApplicationService;
import cn.qbs.wa.union.auth.service.SystemEmployeeUserService;
import cn.qbs.wa.union.auth.service.SystemUserService;
import cn.qbs.wa.union.auth.service.UniUserOauthService;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.LoginUser;
import cn.qbs.wa.teach.common.core.domain.SysUser;
import cn.qbs.wa.teach.common.core.enums.UniClientCodeEnum;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户第三方应用登录绑定表(UniUserOauth)表服务实现类
 *
 * @author makejava
 * @since 2022-07-12 13:53:43
 */
@Slf4j
@Service("uniUserOauthService")
public class UniUserOauthServiceImpl extends ServiceImpl<UniUserOauthMapper, UniUserOauth> implements UniUserOauthService {

    @Autowired
    SystemApplicationService systemApplicationService;

    @Autowired
    SystemUserService systemUserService;

    @Autowired
    SystemEmployeeUserService systemEmployeeUserService;


    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    @Transactional
    public LoginUser bindingByWechat(WechatBindingRequest request, WxOAuth2UserInfo wxUserInfo, UniUserDTO uniUserDTO) {
        SystemApplication systemApplication = systemApplicationService.getOne(new LambdaQueryWrapper<SystemApplication>().eq(SystemApplication::getCode, request.getLoginCode()));
        if (systemApplication != null) {
            List<UniUserOauth> userOauthList = list(new LambdaQueryWrapper<UniUserOauth>()
                    .eq(UniUserOauth::getAppId, systemApplication.getId()).eq(UniUserOauth::getUid, wxUserInfo.getUnionId()));
            if (CollUtil.isNotEmpty(userOauthList)) {
                throw new ServiceException("抱歉,该微信号已被绑定");
            }


            UniUserOauth uniUserOauth = new UniUserOauth();
            uniUserOauth.setAppId(systemApplication.getId());
            uniUserOauth.setUserId(uniUserDTO.getUserId());
            uniUserOauth.setUid(wxUserInfo.getUnionId());
            uniUserOauth.setOauthApp(SocialPlatformEnum.WEIXIN.getName());
            save(uniUserOauth);


            UniClientCodeEnum uniClientCodeEnum = UniClientCodeEnum.getEnumByCode(request.getLoginCode());
            switch (uniClientCodeEnum) {
                case ADMIN:
                    updateAdminHead(uniUserDTO.getUserId(), wxUserInfo);
                    break;
                case ORG:
                    updateEmployeeHead(uniUserDTO.getUserId(), wxUserInfo);
                    break;
            }


            LoginUser loginUser = BeanUtil.copyProperties(uniUserDTO, LoginUser.class);
            loginUser.setSysUser(BeanUtil.copyProperties(uniUserDTO, SysUser.class));
            return loginUser;
        }
        return null;
    }

    @Override
    public void unBindingByWechat(WechatUnBindingRequest request) {
        SystemApplication systemApplication = systemApplicationService.getOne(new LambdaQueryWrapper<SystemApplication>().eq(SystemApplication::getCode, request.getLoginCode()));
        if (systemApplication != null) {
            remove(new LambdaQueryWrapper<UniUserOauth>().eq(UniUserOauth::getAppId, systemApplication.getId()).eq(UniUserOauth::getUserId, SecurityContextHolder.getUserId()));
        }
    }

    @Override
    public void bindingSocial(UserOauthBindingRequest request) {
        SystemApplication systemApplication = systemApplicationService.getOne(new LambdaQueryWrapper<SystemApplication>().eq(SystemApplication::getCode, request.getLoginCode()));
        if (systemApplication == null) {
            throw new ServiceException("无法找到该应用");
        }
        UniClientCodeEnum uniClientCodeEnum = UniClientCodeEnum.getEnumByCode(request.getLoginCode());
        List<UniUserOauth> uniUserOauthList;
        switch (uniClientCodeEnum) {
            case ADMIN:
                break;
            case ORG:
                uniUserOauthList = list(new LambdaQueryWrapper<UniUserOauth>().eq(UniUserOauth::getUserId, request.getUserId()));
                if (CollUtil.isNotEmpty(uniUserOauthList)) {
                    throw new ServiceException("该用户id已绑定");
                }
                uniUserOauthList = list(new LambdaQueryWrapper<UniUserOauth>().eq(UniUserOauth::getUid, request.getUid()));
                if (CollUtil.isNotEmpty(uniUserOauthList)) {
                    throw new ServiceException("该微信已绑定");
                }

                UniUserOauth uniUserOauth = new UniUserOauth();
                uniUserOauth.setAppId(systemApplication.getId());
                uniUserOauth.setUserId(request.getUserId());
                uniUserOauth.setUid(request.getUid());
                uniUserOauth.setOauthApp(SocialPlatformEnum.WEIXIN.getName());
                save(uniUserOauth);
                break;
        }
    }

    @Override
    public Boolean unbindSocial(Long userId) {
        return remove(new LambdaQueryWrapper<UniUserOauth>().eq(UniUserOauth::getUserId,userId));
    }

    @Override
    public Boolean checkBind(UserAccountRequest request) {
       return CollUtil.isNotEmpty(list(new LambdaQueryWrapper<UniUserOauth>().eq(UniUserOauth::getUserId,request.getUserId())));
    }

    private void updateEmployeeHead(Long userId, WxOAuth2UserInfo wxUserInfo) {
        SystemEmployeeUser systemEmployeeUser = new SystemEmployeeUser();
        systemEmployeeUser.setId(userId);
        systemEmployeeUser.setHeadImgUrl(wxUserInfo.getHeadImgUrl());
        systemEmployeeUser.setNickName(wxUserInfo.getNickname());
        systemEmployeeUserService.updateById(systemEmployeeUser);
    }

    private void updateAdminHead(Long userId, WxOAuth2UserInfo wxUserInfo) {
        SystemUser systemUser = new SystemUser();
        systemUser.setId(userId);
        systemUser.setHeadImgUrl(wxUserInfo.getHeadImgUrl());
        systemUser.setNickName(wxUserInfo.getNickname());
        systemUserService.updateById(systemUser);
    }


    private UniUserDTO employeeLogin(String account) {
        return BeanUtil.copyProperties(systemEmployeeUserService.getOne(new LambdaQueryWrapper<SystemEmployeeUser>().eq(SystemEmployeeUser::getAccount, account)), UniUserDTO.class);
    }

    private UniUserDTO adminLogin(String account) {
        return BeanUtil.copyProperties(systemUserService.getOne(new LambdaQueryWrapper<SystemUser>().eq(SystemUser::getAccount, account)), UniUserDTO.class);
    }

}

