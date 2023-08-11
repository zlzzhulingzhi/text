package cn.qbs.wa.union.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.basic.api.RemoteUserService;
import cn.qbs.wa.teach.basic.api.pojo.DTO.user.UserAdminAddDTO;
import cn.qbs.wa.union.admin.entity.SystemApplication;
import cn.qbs.wa.union.admin.entity.SystemUser;
import cn.qbs.wa.union.admin.entity.UniApplicationUser;
import cn.qbs.wa.union.admin.mapper.SystemApplicationMapper;
import cn.qbs.wa.union.admin.mapper.SystemUserMapper;
import cn.qbs.wa.union.admin.mapper.UniApplicationUserMapper;
import cn.qbs.wa.union.admin.pojo.uniapplicationuser.*;
import cn.qbs.wa.union.admin.service.UniApplicationUserService;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.enums.AppCodeEnum;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
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
 * 【系统应用-用户】(UniApplicationUser)表服务实现类
 *
 * @author makejava
 * @since 2022-07-08 09:03:11
 */
@Slf4j
@Service("uniApplicationUserService")
public class UniApplicationUserServiceImpl extends ServiceImpl<UniApplicationUserMapper, UniApplicationUser> implements UniApplicationUserService {

    @Autowired
    SystemApplicationMapper systemApplicationMapper;

    @Autowired
    SystemUserMapper systemUserMapper;

    @Autowired
    RemoteUserService remoteUserService;

    @Override
    @Transactional
    public boolean add(UniApplicationUserAddRequest params) {
        Long userId = params.getSystemUserId();
        remove(new LambdaQueryWrapper<UniApplicationUser>().eq(UniApplicationUser::getSystemUserId, userId));
        List<UniApplicationUser> userList = params.getUniAppIdList().stream().map(uniAppId -> {
            UniApplicationUser uniApplicationUser = new UniApplicationUser();
            uniApplicationUser.setSystemUserId(userId);
            uniApplicationUser.setUniAppId(uniAppId);
            return uniApplicationUser;
        }).collect(Collectors.toList());
        boolean saveBatch = saveBatch(userList);
        List<SystemApplication> applicationList = systemApplicationMapper.listByUniAppId(params.getUniAppIdList());
        SystemUser systemUser = systemUserMapper.selectById(params.getSystemUserId());
        addRemoteUser(applicationList, systemUser);
        return saveBatch;
    }

    private void addRemoteUser(List<SystemApplication> applicationList, SystemUser systemUser) {
        if (CollUtil.isNotEmpty(applicationList) && systemUser != null) {
            //先把账号信息解密
            systemUser.setAccount(AesUtil.decode(systemUser.getAccount()));
            systemUser.setPhone(AesUtil.decode(systemUser.getPhone()));
            Object remoteData = null;
            for (SystemApplication systemApplication : applicationList) {
                String applicationCode = systemApplication.getCode();
                AppCodeEnum anEnum = AppCodeEnum.getEnumByCode(applicationCode);
                if (anEnum != null) {
                    switch (anEnum) {
                        case ADMIN:
                            break;
                        case TEACH:
                            UserAdminAddDTO teachUserAdminAddDTO = BeanUtil.copyProperties(systemUser, UserAdminAddDTO.class);
                            remoteData = remoteUserService.addUserAdmin(teachUserAdminAddDTO).getRemoteData();
                            if (remoteData == null) {
                                throw new ServiceException("培训服务失败");
                            }
                            break;
                    }
                }

            }
        }

    }

    @Override
    public IPage<UniApplicationUserPageResponse> page(UniApplicationUserPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public UniApplicationUserDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(UniApplicationUserUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        UniApplicationUser uniApplicationUser = new UniApplicationUser();
        BeanUtils.copyProperties(params, uniApplicationUser);
        return this.updateById(uniApplicationUser);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public List<UniApplicationUserDetailResponse> getMineApplication(UniApplicationUserMineRequest request) {
        List<UniApplicationUserDetailResponse> mineApplication = baseMapper.getMineApplication(SecurityContextHolder.getUserId());
        if (CollUtil.isNotEmpty(mineApplication)) {
            return mineApplication.stream().filter(i -> request.getLoginCode().equals(i.getUniClientCode())).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List<UniApplicationUserDetailResponse> getApplicationList(IdRequest idRequest) {
        return baseMapper.getMineListApplication(idRequest.getId());
    }

}

