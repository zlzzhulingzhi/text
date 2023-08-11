package cn.qbs.wa.teach.course.standard.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.qbs.wa.teach.basic.api.RemoteUserService;
import cn.qbs.wa.teach.basic.api.pojo.DTO.user.UserListDTO;
import cn.qbs.wa.teach.basic.api.pojo.DTO.user.UserListResultDTO;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdOrgRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseInfoDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseLecturerDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.StudentRelationDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.lite.MemberFormDTO;
import cn.qbs.wa.teach.course.standard.service.RemoteService;
import cn.qbs.wa.teach.organization.api.RemoteLecturerService;
import cn.qbs.wa.teach.organization.api.RemoteStudentService;
import cn.qbs.wa.teach.organization.api.RemoteTeacherOrganizationService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.lecturer.LecturerDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.lecturer.LecturerSearchDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.organization.OrganizationListInnerDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.organization.OrganizationListInnerResultDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.organization.OrganizationListResultDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentSearchDTO;
import cn.qbs.wa.teach.out.union.admin.api.DTO.UniMemberAddDTO;
import cn.qbs.wa.teach.out.union.admin.api.DTO.UniMemberDTO;
import cn.qbs.wa.teach.out.union.admin.api.RemoteUnionMemberService;
import cn.qbs.wa.train.logistics.api.RemoteTrainClazzService;
import cn.qbs.wa.train.logistics.api.pojo.DTO.ClazzPageRequestDTO;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author yjx
 */
@Slf4j
@Service
public class RemoteServiceImpl implements RemoteService {

    @Resource
    private RemoteUserService remoteUserService;
    @Resource
    private RemoteLecturerService remoteLecturerService;
    @Resource
    private RemoteStudentService remoteStudentService;
    @Resource
    private RemoteTeacherOrganizationService organizationService;
    @Resource
    private RemoteUnionMemberService remoteUnionMemberService;
    @Resource
    private RemoteTrainClazzService remoteTrainClazzService;

    @Override
    public Long getCountByCourseId(ClazzPageRequestDTO params) {
        return remoteTrainClazzService.getCountByCourseId(params).getRemoteData();
    }

    @Override
    public Map<Long, UserListResultDTO> remoteUserMap(List<Long> ids) {
        Map<Long, UserListResultDTO> userMap = new HashMap<>(ids.size());
        List<UserListResultDTO> userList = remoteUserList(ids);
        if (CollUtil.isNotEmpty(userList)) {
            userMap = userList.stream().collect(Collectors.toMap(UserListResultDTO::getId, o -> o, (o1, o2) -> o1));
        }
        return userMap;
    }

    @Override
    public List<UserListResultDTO> remoteUserList(List<Long> ids) {
        UserListDTO userListDTO = new UserListDTO();
        userListDTO.setIdList(ids);
        return unboxingRemoteCallData(remoteUserService.listUser(userListDTO));
    }

    @Override
    public Map<Long, StudentDTO> remoteStudentMap(List<Long> userIds) {
        Map<Long, StudentDTO> studentMap = new HashMap<>(userIds.size());
        StudentSearchDTO searchDTO = new StudentSearchDTO();
        searchDTO.setUserIds(userIds);
        //searchDTO.setEnabled(1);
        List<StudentDTO> studentList = unboxingRemoteCallData(remoteStudentService.list(searchDTO));
        if (CollUtil.isNotEmpty(studentList)) {
            studentMap = studentList.stream().collect(Collectors.toMap(StudentDTO::getUserId, o -> o, (o1, o2) -> o1));
        }
        return studentMap;
    }

    @Override
    public List<UniMemberDTO> remoteMemberList(List<Long> memberIds) {
        return remoteUnionMemberService.list(new IdListRequest(memberIds, null)).getRemoteData();
    }

    @Override
    public Map<Long, UniMemberDTO> remoteMemberMap(List<Long> memberIds) {
        List<UniMemberDTO> memberList = remoteMemberList(memberIds);
        return CollUtil.isEmpty(memberList) ? Collections.emptyMap() : memberList.stream().collect(Collectors.toMap(UniMemberDTO::getId, Function.identity()));
    }

    @Override
    public List<LecturerDTO> listLecturers(LecturerSearchDTO params) {
        return unboxingRemoteCallData(remoteLecturerService.listLecturers(params));
    }

    @Override
    public void setLecturers(CourseInfoDTO courseInfoDTO) {
        if (courseInfoDTO != null && CollUtil.isNotEmpty(courseInfoDTO.getLecturers())) {
            LecturerSearchDTO lecturerSearchDTO = new LecturerSearchDTO();
            lecturerSearchDTO.setOrgId(courseInfoDTO.getOrgId());
            lecturerSearchDTO.setIdList(courseInfoDTO.getLecturers().stream().map(CourseLecturerDTO::getLecturerId).collect(Collectors.toList()));
            List<LecturerDTO> lecturers = listLecturers(lecturerSearchDTO);
            if (CollUtil.isNotEmpty(lecturers)) {
                Map<Long, LecturerDTO> dtoMap = lecturers.stream().collect(Collectors.toMap(LecturerDTO::getId, o -> o, (a, b) -> a));
                courseInfoDTO.getLecturers().forEach(e -> {
                    Optional.ofNullable(dtoMap.get(e.getLecturerId())).ifPresent(m -> {
                        e.setLecturerHeadImgUrl(m.getHeadImgUrl());
                        e.setLecturerIntro(m.getIntro());
                        e.setLecturerName(m.getLecturerName());
                        e.setLecturerTitle(m.getTitle());
                    });
                });
            }
        }
    }

    @Override
    public StudentRelationDTO getStudentRelation(Long studentId) {
        StudentRelationDTO studentRelationDTO = new StudentRelationDTO();
        if (studentId == null) {
            return studentRelationDTO;
        }
        //查询学员的标签和部门
        IdOrgRequest idOrgRequest = new IdOrgRequest();
        idOrgRequest.setId(studentId);
        idOrgRequest.setOrgId(SecurityContextHolder.getOrgId());
        StudentDTO relation = unboxingRemoteCallData(remoteStudentService.details(idOrgRequest));
        if (relation != null) {
            if (relation.getDeptId() != null) {
                List<Long> deptIds = new ArrayList<>(5);
                deptIds.add(relation.getDeptId());
                if (CollUtil.isNotEmpty(relation.getDeptPidList())) {
                    deptIds.addAll(relation.getDeptPidList());
                }
                studentRelationDTO.setDeptIds(deptIds);
            }
            if (CollUtil.isNotEmpty(relation.getGroupIdList())) {
                studentRelationDTO.setGroupIds(relation.getGroupIdList());
            }
        }
        return studentRelationDTO;
    }

    @Override
    public List<OrganizationListInnerResultDTO> listOrg(List<Long> orgIds) {
        if (CollUtil.isEmpty(orgIds)) {
            return Collections.emptyList();
        }
        OrganizationListInnerDTO dto = new OrganizationListInnerDTO();
        dto.setIds(orgIds);
        return unboxingRemoteCallData(organizationService.list(dto));
    }

    @Override
    public OrganizationListResultDTO org(Long orgId) {
        if (orgId == null) {
            return null;
        }
        return unboxingRemoteCallData(organizationService.info(new IdRequest(orgId)));
    }

    @Override
    public Long registerMember(MemberFormDTO form) {
        UniMemberAddDTO uniMemberAddDTO = BeanUtil.copyProperties(form, UniMemberAddDTO.class);
        uniMemberAddDTO.setAccount(form.getPhone());
        UniMemberDTO uniMemberDTO = unboxingRemoteCallData(remoteUnionMemberService.register(uniMemberAddDTO));
        if (uniMemberDTO == null) {
            throw new ServiceException("注册学员接口异常");
        }
        return uniMemberDTO.getId();
    }

    private <T> T unboxingRemoteCallData(R<T> data) {
        log.info("远程调用接口响应结果：{}", JSON.toJSONString(data));
        T obj = data.getData();
        if (R.SUCCESS == data.getCode() && ObjectUtil.isNotEmpty(obj)) {
            return obj;
        }
        return null;
    }

    private String unboxingRemoteCallMsg(R<?> result) {
        log.info("远程调用接口响应结果：{}", JSON.toJSONString(result));
        if (R.SUCCESS == result.getCode()) {
            return null;
        }
        return result.getMsg();
    }
}
