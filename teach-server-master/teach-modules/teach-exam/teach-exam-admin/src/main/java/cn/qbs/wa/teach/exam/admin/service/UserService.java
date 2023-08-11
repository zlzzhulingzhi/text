package cn.qbs.wa.teach.exam.admin.service;

import cn.qbs.wa.teach.basic.api.pojo.DTO.user.UserListResultDTO;

import java.util.List;

/**
 *
 * @Author zcm
 * @Date 2021-12-24 18:36
 * @Version 1.0
 */
public interface UserService {

    List<UserListResultDTO> getUserList(List<Long> userIdList);

}
