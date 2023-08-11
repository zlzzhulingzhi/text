package cn.qbs.wa.teach.basic.api.factory;

import cn.qbs.wa.teach.basic.api.RemoteUserService;
import cn.qbs.wa.teach.basic.api.pojo.DTO.user.*;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.LoginUser;
import cn.qbs.wa.teach.common.core.domain.R;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 15:03
 */
public class RemoteUserFallbackFactory implements FallbackFactory<RemoteUserService> {
    @Override
    public RemoteUserService create(Throwable cause) {
        return new RemoteUserService() {

            @Override
            public R<?> registerUser(UserDTO userDTO) {
                return null;
            }

            @Override
            public R<LoginUser> getLoginInfo(UserInfoDTO userInfoDTO) {
                return null;
            }

            @Override
            public R<?> resetPassword(UserPasswordDTO userPasswordDTO) {
                return null;
            }

            @Override
            public R<?> resetPasswordByAdmin(UserPasswordAdminDTO userPasswordAdminDTO) {
                return null;
            }

            @Override
            public R<UserAddResultDTO> add(UserAddDTO userAddDTO) {
                return null;
            }

            @Override
            public R updateOrgUser(UserOrgUpdateDTO userOrgUpdateDTO) {
                return null;
            }

            @Override
            public R<UserOneResultDTO> details(IdRequest request) {
                return null;
            }

            @Override
            public R<UserOneResultDTO> checkExistUser(UserInfoDTO userInfoDTO) {
                return null;
            }

            @Override
            public R<List<UserListResultDTO>> listUser(UserListDTO userListDTO) {
                return null;
            }

            @Override
            public R<List<UserListResultDTO>> listUserByField(UserListFieldDTO userListFieldDTO) {
                return null;
            }

            @Override
            public R addUserAdmin(UserAdminAddDTO request) {
                return null;
            }

            @Override
            public R<UserInnerAddResultDTO> innerAdd(UserAddDTO userAddDTO) {
                return null;
            }
        };
    }
}
