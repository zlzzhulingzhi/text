package cn.qbs.wa.teach.exam.admin.service;

import cn.qbs.wa.teach.basic.api.pojo.DTO.user.UserListResultDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.lecturer.LecturerDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.lecturer.LecturerSearchDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDTO;

import java.util.List;
import java.util.Map;

public interface RemoteService {

    /**
     * 远程调用获取用户信息List
     * @param ids 用户id数组
     * @return 用户信息
     */
    List<UserListResultDTO> remoteUserList(List<Long> ids);

    /**
     * 远程调用获取学员信息Map
     * @param userIds 学员id数组
     * @return 学员信息
     */
    Map<Long, StudentDTO> remoteStudentMap(List<Long> userIds);

    List<LecturerDTO> listLecturers(LecturerSearchDTO params);

}
