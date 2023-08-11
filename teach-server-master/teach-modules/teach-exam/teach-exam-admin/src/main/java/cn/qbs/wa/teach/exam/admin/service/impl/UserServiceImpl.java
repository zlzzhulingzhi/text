package cn.qbs.wa.teach.exam.admin.service.impl;

import cn.qbs.wa.teach.basic.api.RemoteUserService;
import cn.qbs.wa.teach.basic.api.pojo.DTO.user.UserListDTO;
import cn.qbs.wa.teach.basic.api.pojo.DTO.user.UserListResultDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.exam.admin.service.UserService;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final RemoteUserService remoteUserService;


    @Override
    public List<UserListResultDTO> getUserList(List<Long> userIdList) {
        if (CollectionUtils.isNotEmpty(userIdList)) {
            UserListDTO userListDTO = new UserListDTO();
            userListDTO.setIdList(userIdList);
            R<List<UserListResultDTO>> r = remoteUserService.listUser(userListDTO);
            return r.getData();
        }

        return null;
    }

}
