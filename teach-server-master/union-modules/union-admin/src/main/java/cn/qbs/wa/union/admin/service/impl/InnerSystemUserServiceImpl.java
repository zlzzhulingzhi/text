package cn.qbs.wa.union.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.union.admin.entity.*;
import cn.qbs.wa.union.admin.enums.InitRoleEnum;
import cn.qbs.wa.union.admin.mapper.SystemApplicationMapper;
import cn.qbs.wa.union.admin.mapper.SystemRoleMapper;
import cn.qbs.wa.union.admin.mapper.SystemUserMapper;
import cn.qbs.wa.union.admin.pojo.systemuser.InnerSystemUserAddRequest;
import cn.qbs.wa.union.admin.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/7/18 14:57
 */
@Service
public class InnerSystemUserServiceImpl extends ServiceImpl<SystemUserMapper, SystemUser> implements InnerSystemUserService {

    @Autowired
    UniUserService uniUserService;

    @Autowired
    SystemUserRoleService systemUserRoleService;

    @Autowired
    SystemRoleMapper systemRoleMapper;

    @Autowired
    UniApplicationUserService uniApplicationUserService;

    @Autowired
    UniApplicationService uniApplicationService;

    @Autowired
    SystemApplicationMapper systemApplicationMapper;


    @Override
    public SystemUser innerAdd(InnerSystemUserAddRequest params) {
        List<SystemUser> systemUserList = list(new LambdaQueryWrapper<SystemUser>().eq(SystemUser::getAccount, params.getAccount()));
        SystemUser systemUser;
        if (CollUtil.isNotEmpty(systemUserList)) {
            systemUser = systemUserList.get(0);
        } else {
            UniUser uniUser = uniUserService.addUser(BeanUtil.copyProperties(params, UniUser.class));
            systemUser = getById(uniUser.getId());
            if (systemUser == null) {
                systemUser = new SystemUser();
                BeanUtils.copyProperties(params, systemUser);
                systemUser.setId(uniUser.getId());
                this.save(systemUser);
            }
        }
        // saveInnerSystemUserRole(systemUser.getId());  这里是根据角色判断是不是这个统一认证的运维人员 普通人员没必要添加
        saveInnerUniAppUser(systemUser.getId(), params.getAppCode(), params.getExtra());
        return systemUser;
    }

    public void saveInnerUniAppUser(Long userId, String appCode, List<Long> extra) {
        SystemApplication systemApplication = systemApplicationMapper.selectOne(new LambdaQueryWrapper<SystemApplication>().eq(SystemApplication::getCode, appCode));
        if (systemApplication != null) {
            List<UniApplicationUser> uniApplicationUserList = uniApplicationUserService.list(new LambdaQueryWrapper<UniApplicationUser>().eq(UniApplicationUser::getSystemUserId, userId));
            List<UniApplication> uniApplicationList = uniApplicationService.list(new LambdaQueryWrapper<UniApplication>()
                    .eq(UniApplication::getApplicationId, systemApplication.getId())
                    .in(CollUtil.isNotEmpty(extra), UniApplication::getUniAppTypeId, extra)
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

    private void saveInnerSystemUserRole(Long userId) {
        List<SystemRole> systemRoleList = systemRoleMapper.selectList(new LambdaQueryWrapper<SystemRole>().eq(SystemRole::getCode, InitRoleEnum.ADMIN.getCode()));
        if (CollUtil.isNotEmpty(systemRoleList)) {
            List<Long> roleIdList = systemRoleList.stream().map(SystemRole::getId).collect(Collectors.toList());
            List<SystemUserRole> userRoleList = systemUserRoleService.list(new LambdaQueryWrapper<SystemUserRole>().eq(SystemUserRole::getUserId, userId));
            if (CollUtil.isNotEmpty(userRoleList)) {
                List<Long> roleIdListExist = userRoleList.stream().map(SystemUserRole::getRoleId).collect(Collectors.toList());
                roleIdList.removeAll(roleIdListExist);
            }
            if (CollUtil.isNotEmpty(roleIdList)) {
                List<SystemUserRole> systemUserRoleList = roleIdList.stream().map(i -> {
                    SystemUserRole systemUserRole = new SystemUserRole();
                    systemUserRole.setRoleId(i);
                    systemUserRole.setUserId(userId);
                    return systemUserRole;
                }).collect(Collectors.toList());
                systemUserRoleService.saveBatch(systemUserRoleList);
            }
        }
    }


}
