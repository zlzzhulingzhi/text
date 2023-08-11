package cn.qbs.wa.teach.basic.service;

import cn.qbs.wa.teach.basic.pojo.user.*;
import cn.qbs.wa.teach.common.core.domain.LoginUser;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.qbs.wa.teach.basic.entity.User;

import java.util.List;

/**
 * (User)表服务接口
 *
 * @author makejava
 * @since 2021-11-02 14:55:30
 */
public interface UserService extends IService<User> {

    LoginUser getUserInfo(UserInfoRequest userInfoRequest);

    User checkExistUser(String account);

    UserLoginPermissionResponse getUserPermission(UserLoginPermissionRequest request);

    LoginUser getLoginUser(User user);

    User saveUserAdmin(UserAdminAddRequest request);

    void updateUserAdmin(UserAdminUpdateRequest request);

    IPage<UserPageResponse> pageUser(UserPageRequest request);

    void resetPasswordByAdmin(UserPasswordAdminRequest request);

    UserOneResponse getUserById(Long userId);

    void resetPassword(UserPasswordRequest request);

    void batchEnabled(Integer flag, List<Long> idList);

    User updateUserOrg(UserOrgUpdateRequest request);

    User saveUserOrg(UserOrgAddRequest request);

    List<UserListResponse> listUser(UserListRequest request);

    List<UserListResponse> listUserByField(UserListFieldRequest request);

    User saveUserAdminDefault(UserAdminAddRequest request);
}

