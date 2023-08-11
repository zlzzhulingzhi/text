package cn.qbs.wa.union.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import cn.qbs.wa.teach.common.core.domain.LoginUser;
import cn.qbs.wa.teach.common.redis.service.RedisService;
import cn.qbs.wa.teach.common.security.service.TokenService;
import cn.qbs.wa.union.admin.entity.*;
import cn.qbs.wa.union.admin.mapper.SystemApplicationMapper;
import cn.qbs.wa.union.admin.mapper.SystemRoleMapper;
import cn.qbs.wa.union.admin.mapper.SystemUserMapper;
import cn.qbs.wa.union.admin.pojo.systemmenu.SystemMenuTreeResponse;
import cn.qbs.wa.union.admin.pojo.systemuser.*;
import cn.qbs.wa.union.admin.service.*;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.enums.AppCodeEnum;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.teach.common.core.utils.TreeUtil;
import cn.qbs.wa.teach.common.security.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 平台系统子用户表(SystemUser)表服务实现类
 *
 * @author makejava
 * @since 2022-07-08 09:03:07
 */
@Slf4j
@Service("systemUserService")
public class SystemUserServiceImpl extends ServiceImpl<SystemUserMapper, SystemUser> implements SystemUserService {

    @Autowired
    UniUserService uniUserService;

    @Autowired
    SystemUserRoleService systemUserRoleService;

    @Autowired
    UniApplicationService uniApplicationService;

    @Autowired
    UniApplicationUserService uniApplicationUserService;

    @Autowired
    SystemApplicationMapper systemApplicationMapper;

    @Autowired
    SystemRoleMapper systemRoleMapper;

    @Autowired
    private TokenService tokenService;

    @Override
    @Transactional
    public boolean add(SystemUserAddRequest params) {
        List<SystemUser> systemUserList = baseMapper.listAdmin(params.getAccount());
        if (CollUtil.isNotEmpty(systemUserList)) {
            throw new ServiceException("已存在相同用户");
        }
        SystemUser systemUser = new SystemUser();
        List<SystemUser> systemUserExistList = list(new LambdaQueryWrapper<SystemUser>().eq(SystemUser::getAccount, params.getAccount()));
        if (CollUtil.isNotEmpty(systemUserExistList)) {
            systemUser = systemUserExistList.get(0);
        } else {
            UniUser uniUser = uniUserService.addUser(BeanUtil.copyProperties(params, UniUser.class));
            BeanUtils.copyProperties(params, systemUser);
            systemUser.setId(uniUser.getId());
            this.save(systemUser);
        }
        saveSystemUserRole(params.getRoleIdList(), systemUser.getId());
        //默认分配一个平台应用
        saveInnerUniAppUser(systemUser.getId(), AppCodeEnum.ADMIN.getCode());
        return true;
    }

    public void saveInnerUniAppUser(Long userId, String appCode) {
        SystemApplication systemApplication = systemApplicationMapper.selectOne(new LambdaQueryWrapper<SystemApplication>().eq(SystemApplication::getCode, appCode));
        if (systemApplication != null) {
            List<UniApplicationUser> uniApplicationUserList = uniApplicationUserService.list(new LambdaQueryWrapper<UniApplicationUser>().eq(UniApplicationUser::getSystemUserId, userId));
            List<UniApplication> uniApplicationList = uniApplicationService.list(new LambdaQueryWrapper<UniApplication>()
                    .eq(UniApplication::getApplicationId, systemApplication.getId())
                    .notIn(CollUtil.isNotEmpty(uniApplicationUserList),
                            UniApplication::getId, uniApplicationUserList.stream()
                                    .map(UniApplicationUser::getUniAppId).collect(Collectors.toList()))
            );
            if (CollUtil.isNotEmpty(uniApplicationList)) {
                List<UniApplicationUser> applicationUserList = uniApplicationList.stream().map(i -> {
                    UniApplicationUser uniApplicationUser = new UniApplicationUser();
                    uniApplicationUser.setUniAppId(i.getId());
                    uniApplicationUser.setSystemUserId(userId);
                    return uniApplicationUser;
                }).collect(Collectors.toList());
                uniApplicationUserService.saveBatch(applicationUserList);
            }

        }

    }


    private void saveSystemUserRole(List<Long> roleIdList, Long userId) {
        if (CollUtil.isEmpty(roleIdList)) {
            return;
        }
        List<SystemRole> list = systemRoleMapper.selectList(Wrappers.<SystemRole>lambdaQuery().in(SystemRole::getId, roleIdList).eq(SystemRole::getEnabled, Constants.DB_TRUE));
        // 不能选择比当前操作人员角色高的角色
        systemUserRoleService.filterInvalid(list);
        if (CollUtil.isEmpty(list)) {
            return;
        }
        List<SystemUserRole> systemUserRoleList = list.stream().map(SystemRole::getId).map(i -> {
            SystemUserRole systemUserRole = new SystemUserRole();
            systemUserRole.setRoleId(i);
            systemUserRole.setUserId(userId);
            return systemUserRole;
        }).collect(Collectors.toList());
        systemUserRoleService.saveBatch(systemUserRoleList);
    }

    @Override
    public IPage<SystemUserPageResponse> page(SystemUserPageRequest params) {
        if (StringUtils.isNotBlank(params.getSearchContent()) && Validator.isMobile(params.getSearchContent())) {
            params.setSearchContent(AesUtil.encode(params.getSearchContent()));
        }
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public SystemUserDetailResponse detail(Serializable id) {
        SystemUserDetailResponse systemUserDetailResponse = baseMapper.selectDetailById(id);
        if (systemUserDetailResponse == null) {
            return new SystemUserDetailResponse();
        }
        systemUserDetailResponse.setRoleIdList(systemUserRoleService.list(new LambdaQueryWrapper<SystemUserRole>().eq(SystemUserRole::getUserId, id))
                .stream().map(SystemUserRole::getRoleId).collect(Collectors.toList()));
        return systemUserDetailResponse;
    }

    @Override
    @Transactional
    public boolean update(SystemUserUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        List<SystemUser> systemUserList = list(new LambdaQueryWrapper<SystemUser>().eq(SystemUser::getAccount, params.getAccount()).ne(SystemUser::getId, params.getId()));
        if (CollUtil.isNotEmpty(systemUserList)) {
            throw new ServiceException("该账号已被使用");
        }
        SystemUser systemUser = new SystemUser();
        BeanUtils.copyProperties(params, systemUser);
        systemUserRoleService.remove(new LambdaQueryWrapper<SystemUserRole>().eq(SystemUserRole::getUserId, params.getId()));
        saveSystemUserRole(params.getRoleIdList(), systemUser.getId());
        //if (StringUtils.isNotBlank(params.getPassword())) {
        //    systemUser.setPassword(SecurityUtils.encryptPassword(params.getPassword()));
        //}
        return this.updateById(systemUser);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public SystemUserRoleMenuMineResponse getMinePermission() {
        SystemUser systemUser = getById(SecurityContextHolder.getUserId());
        if(Constants.DB_FAIL.equals( systemUser.getEnabled())){
            throw new ServiceException("该用户已被禁用");
        }
        SystemUserRoleMenuMineResponse systemUserRoleMenuMineResponse = new SystemUserRoleMenuMineResponse();
        Long userId = SecurityContextHolder.getUserId();
        List<SystemUserRoleMenuDataResponse> userRoleMenuDataResponseList = this.baseMapper.getMinePermission(userId);
        if (CollUtil.isNotEmpty(userRoleMenuDataResponseList)) {
            List<String> permissions = userRoleMenuDataResponseList.stream().map(SystemUserRoleMenuDataResponse::getPermission).distinct().collect(Collectors.toList());
            systemUserRoleMenuMineResponse.setPermissions(permissions);
            systemUserRoleMenuMineResponse.setRoles(userRoleMenuDataResponseList.stream().map(SystemUserRoleMenuDataResponse::getRoleName).distinct().collect(Collectors.toList()));
            systemUserRoleMenuMineResponse.setMenuList(TreeUtil.tree(BeanUtil.copyToList
                    (userRoleMenuDataResponseList.stream().filter(TreeUtil.distinctByKey(SystemUserRoleMenuDataResponse::getId)).collect(Collectors.toList()), SystemMenuTreeResponse.class)));
            setRolePermission(userId, permissions);
        }
        return systemUserRoleMenuMineResponse;
    }

    private void setRolePermission(Long userId, List<String> permissions) {
        List<SystemRole> roles = systemUserRoleService.listRoleByUserId(userId);
        LoginUser loginUser = SecurityUtils.getLoginUser();
        loginUser.setPermissions(new HashSet<>(permissions));
        loginUser.setRoles(CollStreamUtil.toSet(roles, SystemRole::getCode));
        tokenService.refreshToken(loginUser);
    }

    @Override
    public void innerUpdate(InnerSystemUserUpdateRequest params) {
        SystemUser systemUser = new SystemUser();
        BeanUtils.copyProperties(params, systemUser);
        this.updateById(systemUser);
    }


}

