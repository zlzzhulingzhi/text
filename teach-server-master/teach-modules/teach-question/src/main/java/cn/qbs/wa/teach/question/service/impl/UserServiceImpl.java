package cn.qbs.wa.teach.question.service.impl;

import cn.qbs.wa.teach.basic.api.RemoteUserService;
import cn.qbs.wa.teach.basic.api.pojo.DTO.user.UserListDTO;
import cn.qbs.wa.teach.basic.api.pojo.DTO.user.UserListResultDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.question.service.UserService;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 *
 * @Author zcm
 * @Date 2022-06-20 15:54
 * @Version 1.0
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private RemoteUserService remoteUserService;


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
