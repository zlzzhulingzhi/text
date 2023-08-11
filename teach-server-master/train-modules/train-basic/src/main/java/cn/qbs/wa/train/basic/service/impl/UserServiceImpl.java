package cn.qbs.wa.train.basic.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.constant.CacheConstants;
import cn.qbs.wa.teach.common.redis.service.RedisService;
import cn.qbs.wa.teach.out.union.admin.api.DTO.*;
import cn.qbs.wa.train.basic.entity.Role;
import cn.qbs.wa.train.basic.entity.User;
import cn.qbs.wa.train.basic.entity.UserRole;
import cn.qbs.wa.train.basic.enums.InitRoleEnum;
import cn.qbs.wa.train.basic.mapper.RoleMenuMapper;
import cn.qbs.wa.train.basic.mapper.UserMapper;
import cn.qbs.wa.train.basic.pojo.app.ApplicationFullDetailResponse;
import cn.qbs.wa.train.basic.pojo.app.ApplicationFullResponse;
import cn.qbs.wa.train.basic.pojo.menu.TreeMenuResponse;
import cn.qbs.wa.train.basic.pojo.rolemenu.RoleMenuVO;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.domain.EncodeUser;
import cn.qbs.wa.teach.common.core.domain.LoginUser;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.domain.SysUser;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.teach.common.core.utils.TreeUtil;
import cn.qbs.wa.teach.common.security.utils.SecurityUtils;
import cn.qbs.wa.teach.out.union.admin.api.RemoteUnionSystemEmployeeUserService;
import cn.qbs.wa.teach.out.union.admin.api.RemoteUnionSystemUserService;
import cn.qbs.wa.teach.out.union.admin.api.enums.UnionAppCodeEnum;
import cn.qbs.wa.train.basic.pojo.user.*;
import cn.qbs.wa.train.basic.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * (User)表服务实现类
 *
 * @author makejava
 * @since 2021-11-02 14:55:30
 */
@Slf4j
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    RoleService roleService;

    @Autowired
    RoleMenuMapper roleMenuMapper;

    @Autowired
    ApplicationService applicationService;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    RemoteUnionSystemUserService remoteUnionSystemUserService;

    @Autowired
    RemoteUnionSystemEmployeeUserService remoteUnionSystemEmployeeUserService;

    @Autowired
    RedisService redisService;

    @Override
    public LoginUser getUserInfo(UserInfoRequest request) {
        if (StrUtil.isBlank(request.getAccount()) && request.getUserId() == null) {
            throw new ServiceException("用户传参为空");
        }
        User user = this.getOne(new LambdaQueryWrapper<User>()
                .eq(StrUtil.isNotBlank(request.getAccount()), User::getAccount, AesUtil.encode(request.getAccount()))
                .eq(request.getUserId() != null, User::getId, request.getUserId())
        );
        return getLoginUser(user);
    }

    @Override
    public LoginUser getLoginUser(User user) {
        if (user == null) {
            return null;
        }
        LoginUser loginUser = initLoginUser(user);
        List<Long> roleIds = setRole(user);
        if (CollectionUtils.isNotEmpty(roleIds)) {
            loginUser.setIsAdmin(true);
            //只有运维用户,禁用状态才判断
            if (Constants.DB_FAIL.equals(user.getEnabled())) {
                throw new ServiceException("该用户已禁用");
            }
            //setPermission(roleIds, loginUser);
        }
        loginUser.setFirstLogin(userInfoService.checkFirstInfo(loginUser.getUserid()));
        return loginUser;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User saveUserAdmin(UserAdminAddRequest request) {
        InnerSystemUserAddDTO innerSystemUserAddDTO = BeanUtil.copyProperties(request, InnerSystemUserAddDTO.class);
        User user = new User();
        encodeInfo(request);
        List<User> existUser = baseMapper.checkExistUser(request.getAccount());
        // 特殊：需要根据当前用户的角色所关联的菜单是否存在"资助管理"类型，决定是否给与"资助管理"应用权限
        List<Long> roleIdList = request.getRoleIdList();
        List<Long> appTypeList = roleMenuMapper.listAppType(roleIdList);
        innerSystemUserAddDTO.setExtra(appTypeList);
        innerSystemUserAddDTO.setAppCode(UnionAppCodeEnum.LOGISTICS.getCode());
        InnerSystemUserAddResultDTO remoteData = remoteUnionSystemUserService.innerAdd(innerSystemUserAddDTO).getRemoteData();
        if (remoteData == null) {
            throw new ServiceException("统一服务失败");
        }
        if (CollUtil.isNotEmpty(existUser)) {
            user = existUser.get(0);
        } else {
            BeanUtils.copyProperties(request, user);
            user.setPhone(request.getAccount());
            user.setId(remoteData.getId());
            user.setPassword(SecurityUtils.encryptPassword(request.getPassword()));
            save(user);
        }
        removeUserRole(user.getId());
        saveUserRole(roleIdList, user);
        return user;
    }


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
    @Transactional(rollbackFor = Exception.class)
    public void updateUserAdmin(UserAdminUpdateRequest request) {
        encodeInfo(request);
        checkUpdateExistUser(request.getAccount(), request.getId());
        User user = new User();
        BeanUtils.copyProperties(request, user);
        if (StringUtils.isNotBlank(user.getPassword())) {
            user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        }
        InnerSystemUserUpdateDTO innerSystemUserUpdateDTO = BeanUtil.copyProperties(user, InnerSystemUserUpdateDTO.class);
        Object remoteData = remoteUnionSystemUserService.innerUpdate(innerSystemUserUpdateDTO).getRemoteData();
        if (remoteData == null) {
            throw new ServiceException("统一认证服务失败");
        }
        updateById(user);
        removeUserRole(user.getId());
        saveUserRole(request.getRoleIdList(), user);
    }


    @Override
    public IPage<UserPageResponse> pageUser(UserPageRequest request) {
        Page<User> page = page(new Page<>(request.getCurrent(), request.getSize()), new QueryWrapper<>());
        if (StringUtils.isNotBlank(request.getSearchContent()) && Validator.isMobile(request.getSearchContent())) {
            request.setSearchContent(AesUtil.encode(request.getSearchContent()));
        }
        return baseMapper.pageUser(page, request);
    }

    @Override
    public IPage<UserPageResponse> pageListUser(UserPageRequest request) {
        request.setEnabled(Constants.DB_TRUE);
        return baseMapper.pageListUser(request.createMpPage(), request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPasswordByAdmin(UserPasswordAdminRequest request) {
        String password = SecurityUtils.encryptPassword(Constants.INIT_PASSWORD);
        User user = new User();
        user.setId(request.getUserId());
        user.setPassword(password);
        updateById(user);
        InnerSystemUserUpdateDTO innerSystemUserUpdateDTO = new InnerSystemUserUpdateDTO();
        innerSystemUserUpdateDTO.setId(request.getUserId());
        innerSystemUserUpdateDTO.setPassword(password);
        R r = remoteUnionSystemUserService.innerUpdate(innerSystemUserUpdateDTO);
        if (!r.isOk()){
            throw new ServiceException("统一认证服务失败！");
        }
    }

    @Override
    public UserOneResponse getUserById(Long userId) {
        UserOneResponse userOneResponse = new UserOneResponse();
        User user = getById(userId);
        if (user == null) {
            throw new ServiceException("无法找到该用户");
        }
        BeanUtils.copyProperties(user, userOneResponse);
        List<UserRole> roleList = userRoleService.list(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId));
        if (CollectionUtils.isNotEmpty(roleList)) {
            List<Long> roleIdList = roleList.stream().map(UserRole::getRoleId).collect(Collectors.toList());
            userOneResponse.setRoleIdList(roleIdList);
        }
        return userOneResponse;
    }

    @Override
    public void resetPassword(UserPasswordRequest request) {
        User user = getOne(new LambdaQueryWrapper<User>().eq(User::getAccount, AesUtil.encode(request.getAccount())));
        if (user == null) {
            throw new ServiceException("不存在该用户");
        }
        User userUpdate = new User();
        userUpdate.setId(user.getId());
        userUpdate.setPassword(SecurityUtils.encryptPassword(request.getPassword()));
        updateById(userUpdate);
    }

    @Override
    public void batchEnabled(Integer flag, List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            throw new ServiceException("请选中");
        }
        List<User> users = new ArrayList<>();
        for (Long roleId : idList) {
            User user = new User();
            user.setId(roleId);
            user.setEnabled(flag);
            users.add(user);
        }
        updateBatchById(users);
    }

    @Override
    public User updateUserOrg(UserOrgUpdateRequest request) {
        encodeInfo(request);
        checkUpdateExistUser(request.getAccount(), request.getId());
        User user = new User();
        BeanUtils.copyProperties(request, user);
        if (StringUtils.isNotBlank(user.getPassword())) {
            user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        }
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

        if (StrUtil.isBlank(request.getPassword())) {
            request.setPassword(Constants.INIT_PASSWORD);
        }
        if (StrUtil.isBlank(request.getAccount())) {
            request.setAccount(request.getPhone());
        }

        InnerEmployeeUserAddDTO innerEmployeeUserAddDTO = BeanUtil.copyProperties(request, InnerEmployeeUserAddDTO.class);

        encodeInfo(request);
        //checkExistUser(request.getAccount());
        List<User> existUser = list(new LambdaQueryWrapper<User>().eq(User::getAccount, request.getAccount()));
        if (CollectionUtils.isNotEmpty(existUser)) {
            User existUserSingle = existUser.get(0);
            addRemoteUnionSystemEmployee(innerEmployeeUserAddDTO, user, request);
            existUserSingle.setRealName(request.getRealName());
            return existUserSingle;
        }
        SystemEmployeeUserAddResultDTO remoteData = addRemoteUnionSystemEmployee(innerEmployeeUserAddDTO, user, request);

        BeanUtils.copyProperties(request, user);
        user.setPhone(request.getAccount());
        user.setId(remoteData.getId());
        // 同步统一认证密码
        user.setPassword(remoteData.getPassword());
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

    @Override
    public List<UserListResponse> listUser(UserListRequest request) {
        if (StringUtils.isNotBlank(request.getSearchContent()) && Validator.isMobile(request.getSearchContent())) {
            request.setSearchContent(AesUtil.encode(request.getSearchContent()));
        }
        return baseMapper.listUser(request);
    }

    @Override
    public List<UserListResponse> listUserByField(UserListFieldRequest request) {
        if (StrUtil.isNotBlank(request.getPhone())) {
            request.setPhone(AesUtil.encode(request.getPhone()));
        }
        if (StrUtil.isNotBlank(request.getIdNumber())) {
            request.setIdNumber(AesUtil.encode(request.getIdNumber()));
        }
        return baseMapper.listUserByField(request);
    }

    @Override
    public User saveUserAdminDefault(UserAdminAddRequest request) {
        InnerSystemUserAddDTO innerSystemUserAddDTO = BeanUtil.copyProperties(request, InnerSystemUserAddDTO.class);

        List<Role> roleList = roleService.list(new LambdaQueryWrapper<Role>().eq(Role::getCode, InitRoleEnum.ADMIN.getCode()));
        if (CollUtil.isNotEmpty(roleList)) {
            request.setRoleIdList(roleList.stream().map(Role::getId).collect(Collectors.toList()));
            User user = new User();
            encodeInfo(request);
            List<User> existUser = baseMapper.checkExistUser(request.getAccount());
            if (CollUtil.isNotEmpty(existUser)) {
                user = existUser.get(0);
            } else {
                innerSystemUserAddDTO.setAppCode(UnionAppCodeEnum.LOGISTICS.getCode());
                InnerSystemUserAddResultDTO remoteData = remoteUnionSystemUserService.innerAdd(innerSystemUserAddDTO).getRemoteData();
                if (remoteData == null) {
                    throw new ServiceException("统一服务失败");
                }
                BeanUtils.copyProperties(request, user);
                user.setPhone(request.getAccount());
                user.setPassword(SecurityUtils.encryptPassword(request.getPassword()));
                user.setId(remoteData.getId());
                save(user);
            }
            removeUserRole(user.getId());
            saveUserRole(request.getRoleIdList(), user);
            return user;

        }
        return null;
    }


    private void removeUserRole(Long userId) {
        userRoleService.remove(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId));
    }


    private void saveUserRole(List<Long> roleIdList, User user) {
        if (CollUtil.isEmpty(roleIdList)) {
            return;
        }
        List<Role> list = roleService.lambdaQuery().in(Role::getId, roleIdList).eq(Role::getEnabled, Constants.DB_TRUE).list();
        // 不能选择比当前操作人员角色高的角色
        userRoleService.filterInvalid(list);
        if (CollUtil.isEmpty(list)) {
            return;
        }
        if (CollectionUtils.isNotEmpty(list)) {
            roleIdList = CollStreamUtil.toList(list, Role::getId);
            List<UserRole> userRoles = new ArrayList<>();
            for (Long roleId : roleIdList) {
                UserRole userRole = new UserRole();
                userRole.setUserId(user.getId());
                userRole.setRoleId(roleId);
                userRoles.add(userRole);
            }
            userRoleService.saveBatch(userRoles);
        }
    }


    @Override
    public User checkExistUser(String account) {
        List<User> existUser = list(new LambdaQueryWrapper<User>().eq(User::getAccount, AesUtil.encode(account)));
        if (CollectionUtils.isNotEmpty(existUser)) {
            return existUser.get(0);
        }
        return null;
    }

    private void checkUpdateExistUser(String account, Long userId) {
        List<User> existUser = list(new LambdaQueryWrapper<User>().eq(User::getAccount, account).ne(User::getId, userId));
        if (CollectionUtils.isNotEmpty(existUser)) {
            throw new ServiceException("该用户已存在");
        }
    }

    @Override
    public UserLoginPermissionResponse getUserPermission(UserLoginPermissionRequest request) {
        UserLoginPermissionResponse response = new UserLoginPermissionResponse();
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            throw new ServiceException("登录状态已过期");
        }
        User user = this.getById(loginUser.getUserid());
        if (Constants.DB_FAIL.equals(user.getEnabled())) {
            throw new ServiceException("该账号已禁用");
        }
        List<Long> roleIdList = null;
        List<UserRole> list = userRoleService.lambdaQuery().select(UserRole::getRoleId).eq(UserRole::getUserId, loginUser.getUserid()).list();
        if (!list.isEmpty()) {
            roleIdList = list.stream().map(UserRole::getRoleId).distinct().collect(Collectors.toList());
            setRole(roleIdList, loginUser);
        }
        Set<String> permissions = new HashSet<>();
        List<ApplicationFullResponse> appList = applicationService.getChildrenByAppTypeId(request.getApplicationTypeId());
        List<Long> appIdList = initAppIdList(appList);
        if (CollectionUtils.isNotEmpty(roleIdList) && CollectionUtils.isNotEmpty(appIdList)) {
            List<RoleMenuVO> fullRoleMenuVOList = roleMenuMapper.getFullRoleMenuByRoleIdList(roleIdList);
            if (CollectionUtils.isNotEmpty(fullRoleMenuVOList)) {
                List<RoleMenuVO> roleMenuVOList = fullRoleMenuVOList.stream().filter(i -> appIdList.contains(i.getAppId())).collect(Collectors.toList());
                List<TreeMenuResponse> treeMenuResponses = initTreeMenu(roleMenuVOList);
                initAppMenu(appList, treeMenuResponses);
                if (!roleMenuVOList.isEmpty()) {
                    permissions = roleMenuVOList.stream().map(RoleMenuVO::getPermission).collect(Collectors.toSet());
                }
            }

        }
        response.setAppList(filterAppList(appList));

        setContextPermission(loginUser, permissions);

        return response;
    }

    private void setContextPermission(LoginUser loginUser, Set<String> permissions) {
        if (CollUtil.isEmpty(permissions)) {
            permissions = new HashSet<>();
        }
        loginUser.setPermissions(permissions);
        String userKey = CacheConstants.LOGIN_TOKEN_KEY + loginUser.getToken();
        redisService.setCacheObject(userKey, loginUser, CacheConstants.EXPIRATION, TimeUnit.MINUTES);
    }

    private List<ApplicationFullResponse> filterAppList(List<ApplicationFullResponse> appList) {
        if (CollectionUtils.isNotEmpty(appList)) {
            //过滤掉没有菜单的应用;
            for (ApplicationFullResponse applicationFullResponse : appList) {
                List<ApplicationFullDetailResponse> applicationList = applicationFullResponse.getApplicationList();
                if (CollectionUtils.isNotEmpty(applicationList)) {
                    List<ApplicationFullDetailResponse> applicationFilterList = new ArrayList<>();
                    for (ApplicationFullDetailResponse applicationFullDetailResponse : applicationList) {
                        if (!applicationFullDetailResponse.getFilter()) {
                            applicationFilterList.add(applicationFullDetailResponse);
                        }
                    }
                    applicationFullResponse.setApplicationList(applicationFilterList);
                }
            }
            return appList.stream().filter(i -> CollectionUtils.isNotEmpty(i.getApplicationList())).collect(Collectors.toList());
        }
        return appList;
    }

    private List<Long> initAppIdList(List<ApplicationFullResponse> appList) {
        if (CollectionUtils.isNotEmpty(appList)) {
            List<Long> appIdList = new ArrayList<>();
            for (ApplicationFullResponse applicationFullResponse : appList) {
                List<ApplicationFullDetailResponse> applicationList = applicationFullResponse.getApplicationList();
                if (CollectionUtils.isNotEmpty(applicationList)) {
                    appIdList.addAll(applicationList.stream().map(ApplicationFullDetailResponse::getAppId).collect(Collectors.toList()));
                }

            }
            return appIdList;
        }
        return null;
    }


    private void initAppMenu(List<ApplicationFullResponse> appList, List<TreeMenuResponse> treeMenuResponses) {
        if (CollectionUtils.isNotEmpty(appList)) {
            for (ApplicationFullResponse applicationFullResponse : appList) {
                List<ApplicationFullDetailResponse> applicationList = applicationFullResponse.getApplicationList();
                setMenuList(applicationList, treeMenuResponses);

            }
        }

    }

    private void setMenuList(List<ApplicationFullDetailResponse> applicationList, List<TreeMenuResponse> treeMenuResponses) {
        if (CollectionUtils.isNotEmpty(applicationList) && CollectionUtils.isNotEmpty(treeMenuResponses)) {
            for (ApplicationFullDetailResponse applicationFullDetailResponse : applicationList) {
                List<TreeMenuResponse> menuList = new ArrayList<>();
                for (TreeMenuResponse treeMenuResponse : treeMenuResponses) {
                    if (applicationFullDetailResponse.getAppId().equals(treeMenuResponse.getAppId())) {
                        menuList.add(treeMenuResponse);
                    }
                }
                applicationFullDetailResponse.setMenuList(menuList);
            }
        }

    }

    private List<TreeMenuResponse> initTreeMenu(List<RoleMenuVO> roleMenuVOList) {
        if (CollectionUtils.isNotEmpty(roleMenuVOList)) {
            List<TreeMenuResponse> treeMenuResponses = new ArrayList<>();
            for (RoleMenuVO roleMenuVO : roleMenuVOList) {
                TreeMenuResponse treeMenuResponse = new TreeMenuResponse();
                BeanUtils.copyProperties(roleMenuVO, treeMenuResponse);
                treeMenuResponses.add(treeMenuResponse);
            }
            return (List<TreeMenuResponse>) TreeUtil.tree(treeMenuResponses);
        }
        return null;
    }

    private LoginUser initLoginUser(User user) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserid(user.getId());
        // 由于员工姓名独立，取微信昵称
        loginUser.setNickName(user.getNickName());
        loginUser.setOrgId(Constants.INIT_ORG);
        loginUser.setHeadImgUrl(user.getHeadImgUrl());
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(user, sysUser);
        sysUser.setUserName(user.getRealName());
        sysUser.setUserId(user.getId());
        sysUser.setOrgId(Constants.INIT_ORG);
        sysUser.setPhonenumber(AesUtil.decode(user.getAccount()));
        loginUser.setSysUser(sysUser);
        return loginUser;
    }

    private List<Long> setRole(User user) {
        List<Long> roleIds = new ArrayList<>();
        List<UserRole> userRoleList = userRoleService.list(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, user.getId()));
        if (CollectionUtils.isNotEmpty(userRoleList)) {
            roleIds.addAll(userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toList()));
        }

        return roleIds;
    }

    private void setRole(List<Long> roleIds, LoginUser loginUser) {
        if (CollectionUtils.isNotEmpty(roleIds)) {
            List<Role> roleList = roleService.list(new LambdaQueryWrapper<Role>().select(Role::getId, Role::getCode).eq(Role::getEnabled, Constants.DB_TRUE).in(Role::getId, roleIds));
            if (CollectionUtils.isNotEmpty(roleList)) {
                loginUser.setRoles(roleList.stream().filter(r -> StrUtil.isNotBlank(r.getCode())).map(Role::getCode).collect(Collectors.toSet()));
                loginUser.setRoleIdList(roleIds);
            }
        }
    }

}

