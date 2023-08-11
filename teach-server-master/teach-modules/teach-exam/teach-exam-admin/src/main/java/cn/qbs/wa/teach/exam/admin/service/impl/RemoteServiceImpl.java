package cn.qbs.wa.teach.exam.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.qbs.wa.teach.basic.api.RemoteUserService;
import cn.qbs.wa.teach.basic.api.pojo.DTO.user.UserListDTO;
import cn.qbs.wa.teach.basic.api.pojo.DTO.user.UserListResultDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.exam.admin.service.RemoteService;
import cn.qbs.wa.teach.organization.api.RemoteLecturerService;
import cn.qbs.wa.teach.organization.api.RemoteStudentService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.lecturer.LecturerDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.lecturer.LecturerSearchDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentSearchDTO;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        searchDTO.setEnabled(1);
        List<StudentDTO> studentList = unboxingRemoteCallData(remoteStudentService.list(searchDTO));
        if (CollUtil.isNotEmpty(studentList)) {
            studentMap = studentList.stream().collect(Collectors.toMap(StudentDTO::getUserId, o -> o, (o1, o2) -> o1));
        }
        return studentMap;
    }

    @Override
    public List<LecturerDTO> listLecturers (LecturerSearchDTO params) {
       return unboxingRemoteCallData(remoteLecturerService.listLecturers(params));
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
