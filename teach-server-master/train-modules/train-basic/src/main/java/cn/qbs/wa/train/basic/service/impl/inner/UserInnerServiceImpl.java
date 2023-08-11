package cn.qbs.wa.train.basic.service.impl.inner;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.domain.EncodeUser;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.teach.out.union.admin.api.DTO.InnerEmployeeUserAddDTO;
import cn.qbs.wa.teach.out.union.admin.api.DTO.InnerSystemUserUpdateDTO;
import cn.qbs.wa.teach.out.union.admin.api.DTO.SystemEmployeeUserAddResultDTO;
import cn.qbs.wa.teach.out.union.admin.api.RemoteUnionSystemEmployeeUserService;
import cn.qbs.wa.teach.out.union.admin.api.enums.UnionAppCodeEnum;
import cn.qbs.wa.train.basic.entity.User;
import cn.qbs.wa.train.basic.mapper.UserMapper;
import cn.qbs.wa.train.basic.pojo.user.UserOrgAddRequest;
import cn.qbs.wa.train.basic.pojo.user.UserOrgUpdateRequest;
import cn.qbs.wa.train.basic.service.inner.UserInnerService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (User)表服务实现类
 *
 * @author makejava
 * @since 2021-11-02 14:55:30
 */
@Slf4j
@Service("userInnerService")
public class UserInnerServiceImpl extends ServiceImpl<UserMapper, User> implements UserInnerService {

    @Autowired
    RemoteUnionSystemEmployeeUserService remoteUnionSystemEmployeeUserService;

    @Autowired
    UserInnerService userInnerService;

    private void encodeInfo(EncodeUser request) {
        if (StringUtils.isNotBlank(request.getAccount())) {
            request.setAccount(AesUtil.encode(request.getAccount()));
        }
        if (StringUtils.isNotBlank(request.getPhone())) {
            request.setPhone(AesUtil.encode(request.getPhone()));

        }
        if (StringUtils.isNotBlank(request.getIdNumber())) {
            request.setIdNumber(AesUtil.encode(request.getIdNumber()));

        }


    }

    @Override
    public User updateUserOrg(UserOrgUpdateRequest request) {
        encodeInfo(request);
        User user = new User();
        BeanUtils.copyProperties(request, user);
        InnerSystemUserUpdateDTO innerSystemUserUpdateDTO = BeanUtil.copyProperties(user, InnerSystemUserUpdateDTO.class);
        Object remoteData = remoteUnionSystemEmployeeUserService.innerUpdate(innerSystemUserUpdateDTO).getRemoteData();
        if (remoteData == null) {
            throw new ServiceException("统一认证服务失败");
        }
        updateById(user);
        return user;
    }

    @Override
    public User saveUserOrg(UserOrgAddRequest request) {
        User user = new User();
        BeanUtils.copyProperties(request, user);
        user.setId(request.getUserId());
        User use=userInnerService.getById(request.getUserId());
        if(use!=null){
            updateById(user);
            return user;
        }
        save(user);
        return user;
    }

    private SystemEmployeeUserAddResultDTO addRemoteUnionSystemEmployee(InnerEmployeeUserAddDTO innerEmployeeUserAddDTO, User user, UserOrgAddRequest request) {
        innerEmployeeUserAddDTO.setAppCode(UnionAppCodeEnum.LOGISTICS.getCode());
        SystemEmployeeUserAddResultDTO remoteData = remoteUnionSystemEmployeeUserService.innerAdd(innerEmployeeUserAddDTO).getRemoteData();
        if (remoteData == null) {
            throw new ServiceException("统一服务失败");
        }
        return remoteData;
    }
}

