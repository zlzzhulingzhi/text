package cn.qbs.wa.teach.basic.api;

import cn.qbs.wa.teach.basic.api.factory.RemoteUserFallbackFactory;
import cn.qbs.wa.teach.basic.api.pojo.DTO.user.*;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.LoginUser;
import cn.qbs.wa.teach.common.core.domain.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 15:02
 */
@FeignClient(contextId = "remoteUserService", name = "teach-basic", path = "basic/user", fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteUserService {

    @PostMapping
    R<?> registerUser(@RequestBody UserDTO userDTO);

    @PostMapping("/login-info")
    @ApiOperation("获取用户信息")
    R<LoginUser> getLoginInfo(@RequestBody UserInfoDTO userInfoDTO);

    @PostMapping("/reset-password")
    @ApiOperation("重置密码")
    R<?> resetPassword(@RequestBody UserPasswordDTO userPasswordDTO);

    @PostMapping("admin/reset-password")
    @ApiOperation("平台管理重置密码")
    R<?> resetPasswordByAdmin(@RequestBody UserPasswordAdminDTO userPasswordAdminDTO);

    @PostMapping("org/add")
    @ApiOperation("机构管理新增用户信息")
    R<UserAddResultDTO> add(@RequestBody UserAddDTO userAddDTO);

    @PostMapping("org/update")
    @ApiOperation("机构管理更新用户基础信息")
    R<UserOneResultDTO> updateOrgUser(@RequestBody UserOrgUpdateDTO userOrgUpdateDTO);


    @PostMapping("detail")
    @ApiOperation("详情")
    R<UserOneResultDTO> details(@RequestBody IdRequest idDTO);

    @PostMapping("check-exist")
    @ApiOperation("检查是否存在用户")
    R<UserOneResultDTO> checkExistUser(@RequestBody UserInfoDTO userInfoDTO) ;

    @PostMapping("list")
    @ApiOperation("列表查询")
    R<List<UserListResultDTO>> listUser(@RequestBody UserListDTO userListDTO) ;

    @PostMapping("list-field")
    @ApiOperation("列表字段查询")
    R<List<UserListResultDTO>> listUserByField(@RequestBody UserListFieldDTO userListFieldDTO) ;

    @PostMapping("inner/add")
    @ApiOperation("平台管理新增用户")
    R addUserAdmin(@RequestBody UserAdminAddDTO request);

    @PostMapping("inner/org/add")
    @ApiOperation("机构管理新增用户信息")
    R<UserInnerAddResultDTO> innerAdd(@RequestBody UserAddDTO userAddDTO);
}
