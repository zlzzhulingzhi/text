package cn.qbs.wa.teach.exam.admin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.exam.admin.mapper.ExamUserVisibleMapper;
import cn.qbs.wa.teach.exam.admin.service.ExamService;
import cn.qbs.wa.teach.exam.common.entity.ExamUserVisible;
import cn.qbs.wa.teach.exam.admin.service.ExamUserVisibleService;
import cn.qbs.wa.teach.organization.api.RemoteEmployeeService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.employee.EmployeeListResultDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.employee.EmployeeListSearchDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDTO;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 考试可见用户(ExamUserVisible)表服务实现类
 *
 * @author zcm
 * @since 2021-12-14 11:43:32
 */
@Slf4j
@Service("examUserVisibleService")
public class ExamUserVisibleServiceImpl extends ServiceImpl<ExamUserVisibleMapper, ExamUserVisible> implements ExamUserVisibleService {

    @Resource
    private RemoteEmployeeService remoteEmployeeService;

    @Lazy
    @Autowired
    private ExamService examService;

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
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

    @Override
    public boolean addUser(List<Long> examIdList, List<StudentDTO> studentDTOList) {
        if (CollectionUtils.isNotEmpty(studentDTOList)){
            //考试时间检查
            examService.checkExamTime(examIdList.get(0));
            //远程获取用户的职工id
            List<Long> userIdList = studentDTOList.stream().map(StudentDTO::getUserId).collect(Collectors.toList());
            EmployeeListSearchDTO employeeListSearchDTO = new EmployeeListSearchDTO();
            employeeListSearchDTO.setUserIdList(userIdList);
            R<List<EmployeeListResultDTO>> employeeList = remoteEmployeeService.list(employeeListSearchDTO);
            if (!employeeList.isOk()){
                throw new ServiceException("远程服务失败！");
            }
            Map<Long, Long> employeeMap = null;
            if (CollectionUtils.isNotEmpty(employeeList.getData())){
                employeeMap = employeeList.getData().stream().collect(Collectors.toMap(EmployeeListResultDTO::getUserId, EmployeeListResultDTO::getId));
            }
            //先拼装所有用户信息
            List<ExamUserVisible> addUserAll = new ArrayList<>();
            for (Long examId : examIdList){
                for (StudentDTO studentDTO : studentDTOList){
                    ExamUserVisible examUserVisible = new ExamUserVisible();
                    examUserVisible.setExamId(examId);
                    examUserVisible.setStudentId(studentDTO.getId());
                    examUserVisible.setUserId(studentDTO.getUserId());
                    examUserVisible.setEmployeeId(CollectionUtils.isNotEmpty(employeeMap) && employeeMap.containsKey(studentDTO.getUserId()) ? employeeMap.get(studentDTO.getUserId()) : null);
                    addUserAll.add(examUserVisible);
                }
            }
            //把需要添加的用户筛选出来
            addUserAll = addUserAll.stream().filter(i -> {
                Long count = this.count(Wrappers.<ExamUserVisible>lambdaQuery().eq(ExamUserVisible::getExamId, i.getExamId()).eq(ExamUserVisible::getUserId, i.getUserId()));
                return count <= 0;
            }).collect(Collectors.toList());
            this.saveBatch(addUserAll);
        }
        return true;
    }
}

