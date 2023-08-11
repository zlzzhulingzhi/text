package cn.qbs.wa.train.basic.api.factory;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.LoginUser;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.basic.api.RemoteTrainUserService;
import cn.qbs.wa.train.basic.api.pojo.DTO.user.*;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 15:03
 */
public class RemoteTrainUserFallbackFactory implements FallbackFactory<RemoteTrainUserService> {
    @Override
    public RemoteTrainUserService create(Throwable cause) {
        return new RemoteTrainUserService() {

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

            @Override
            public R<UserOneResultDTO> innerUpdateOrgUser(UserOrgUpdateDTO userOrgUpdateDTO) {
                return null;
            }
        };
    }
}
