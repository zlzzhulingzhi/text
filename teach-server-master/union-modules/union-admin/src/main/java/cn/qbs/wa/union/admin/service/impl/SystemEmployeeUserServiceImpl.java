package cn.qbs.wa.union.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.union.admin.entity.*;
import cn.qbs.wa.union.admin.mapper.SystemApplicationMapper;
import cn.qbs.wa.union.admin.mapper.SystemEmployeeUserMapper;
import cn.qbs.wa.union.admin.mapper.UniApplicationMapper;
import cn.qbs.wa.union.admin.pojo.systememployeeuser.*;
import cn.qbs.wa.union.admin.service.SystemEmployeeUserService;
import cn.qbs.wa.union.admin.service.UniApplicationUserService;
import cn.qbs.wa.union.admin.service.UniUserService;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 职工用户表(SystemEmployeeUser)表服务实现类
 *
 * @author makejava
 * @since 2022-07-08 09:03:03
 */
@Slf4j
@Service("systemEmployeeUserService")
public class SystemEmployeeUserServiceImpl extends ServiceImpl<SystemEmployeeUserMapper, SystemEmployeeUser> implements SystemEmployeeUserService {

    @Autowired
    UniUserService uniUserService;

    @Autowired
    UniApplicationUserService uniApplicationUserService;

    @Autowired
    UniApplicationMapper uniApplicationMapper;

    @Autowired
    SystemApplicationMapper systemApplicationMapper;

    @Override
    @Transactional
    public SystemEmployeeUser add(SystemEmployeeUserAddRequest params) {
        SystemEmployeeUser systemEmployeeUser = new SystemEmployeeUser();
        List<SystemEmployeeUser> employeeUserList = list(new LambdaQueryWrapper<SystemEmployeeUser>().eq(SystemEmployeeUser::getAccount, params.getAccount()));
        if (CollUtil.isNotEmpty(employeeUserList)) {
            systemEmployeeUser=employeeUserList.get(0);
        }else{
            UniUser uniUser = uniUserService.addUser(BeanUtil.copyProperties(params, UniUser.class));
            BeanUtils.copyProperties(params, systemEmployeeUser);
            systemEmployeeUser.setId(uniUser.getId());
            this.save(systemEmployeeUser);
        }
        addUniApplicationUser(systemEmployeeUser.getId(), params.getAppCode());
        return systemEmployeeUser;
    }

    private void addUniApplicationUser(Long userId, String appCode) {
        SystemApplication systemApplication = systemApplicationMapper.selectOne(new LambdaQueryWrapper<SystemApplication>().eq(SystemApplication::getCode, appCode));
        if (systemApplication != null) {
            List<UniApplicationUser> uniApplicationUserList = uniApplicationUserService.list(new LambdaQueryWrapper<UniApplicationUser>().eq(UniApplicationUser::getSystemUserId, userId));

            List<UniApplication> uniApplicationList = uniApplicationMapper.selectList(new LambdaQueryWrapper<UniApplication>()
                    .eq(UniApplication::getApplicationId, systemApplication.getId())
                    .notIn(CollUtil.isNotEmpty(uniApplicationUserList),
                            UniApplication::getId, uniApplicationUserList.stream()
                                    .map(UniApplicationUser::getUniAppId).collect(Collectors.toList()))
            );
            if (CollUtil.isNotEmpty(uniApplicationList)) {
                for (UniApplication uniApplication : uniApplicationList) {
                    UniApplicationUser uniApplicationUser = new UniApplicationUser();
                    uniApplicationUser.setSystemUserId(userId);
                    uniApplicationUser.setUniAppId(uniApplication.getId());
                    uniApplicationUserService.save(uniApplicationUser);
                }
            }

        }

    }

    @Override
    public IPage<SystemEmployeeUserPageResponse> page(SystemEmployeeUserPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public SystemEmployeeUserDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(SystemEmployeeUserUpdateRequest params) {
        if (params.getUserId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        List<SystemEmployeeUser> employeeUserList = list(new LambdaQueryWrapper<SystemEmployeeUser>().eq(SystemEmployeeUser::getAccount, params.getAccount()).ne(SystemEmployeeUser::getId, params.getUserId()));
        if (CollUtil.isNotEmpty(employeeUserList)) {
            throw new ServiceException("该账号已被占用");
        }
        SystemEmployeeUser systemEmployeeUser = new SystemEmployeeUser();
        BeanUtils.copyProperties(params, systemEmployeeUser);
        return this.updateById(systemEmployeeUser);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public SystemEmployeeUser innerAdd(InnerEmployeeUserAddRequest params) {
        SystemEmployeeUser systemEmployeeUser = new SystemEmployeeUser();
        List<SystemEmployeeUser> employeeUserList = list(new LambdaQueryWrapper<SystemEmployeeUser>().eq(SystemEmployeeUser::getAccount, params.getAccount()));
        if (CollUtil.isNotEmpty(employeeUserList)) {
            systemEmployeeUser=employeeUserList.get(0);
        }else{
            UniUser uniUser = uniUserService.addUser(BeanUtil.copyProperties(params, UniUser.class));
            BeanUtils.copyProperties(params, systemEmployeeUser);
            systemEmployeeUser.setId(uniUser.getId());
            this.save(systemEmployeeUser);
        }
        addUniApplicationUser(systemEmployeeUser.getId(), params.getAppCode());
        return systemEmployeeUser;
    }

    @Override
    public void innerUpdate(InnerEmployeeUserUpdateRequest params) {
        SystemEmployeeUser systemEmployeeUser = BeanUtil.copyProperties(params, SystemEmployeeUser.class);
        updateById(systemEmployeeUser);
    }

}

