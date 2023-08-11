package cn.qbs.wa.teach.question.service;

import cn.qbs.wa.teach.basic.api.pojo.DTO.user.UserListResultDTO;

import java.util.List;

/**
 *
 *
 * @Author zcm
 * @Date 2022-06-20 15:54
 * @Version 1.0
 */
public interface UserService {

    List<UserListResultDTO> getUserList(List<Long> userIdList);

}
