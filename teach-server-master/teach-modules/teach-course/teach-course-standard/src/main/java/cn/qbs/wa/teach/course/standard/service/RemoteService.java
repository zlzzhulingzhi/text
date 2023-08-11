package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.basic.api.pojo.DTO.user.UserListResultDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseInfoDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.StudentRelationDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.lite.MemberFormDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.lecturer.LecturerDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.lecturer.LecturerSearchDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.organization.OrganizationListInnerResultDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.organization.OrganizationListResultDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDTO;
import cn.qbs.wa.teach.out.union.admin.api.DTO.UniMemberDTO;
import cn.qbs.wa.train.logistics.api.pojo.DTO.ClazzPageRequestDTO;
import org.springframework.web.bind.annotation.RequestBody;

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
     * 远程调用获取用户信息Map
     * @param ids 用户id数组
     * @return 用户信息
     */
    Map<Long, UserListResultDTO> remoteUserMap(List<Long> ids);

    /**
     * 远程调用获取学员信息Map
     * @param userIds 学员id数组
     * @return 学员信息
     */
    Map<Long, StudentDTO> remoteStudentMap(List<Long> userIds);

    List<UniMemberDTO> remoteMemberList(List<Long> memberIds);

    Map<Long, UniMemberDTO> remoteMemberMap(List<Long> memberIds);

    List<LecturerDTO> listLecturers(LecturerSearchDTO params);

    void setLecturers(CourseInfoDTO courseInfoDTO);

    StudentRelationDTO getStudentRelation(Long studentId);

    List<OrganizationListInnerResultDTO> listOrg(List<Long> orgIds);

    OrganizationListResultDTO org(Long orgId);

    Long registerMember(MemberFormDTO form);

    Long getCountByCourseId(ClazzPageRequestDTO params);
}
