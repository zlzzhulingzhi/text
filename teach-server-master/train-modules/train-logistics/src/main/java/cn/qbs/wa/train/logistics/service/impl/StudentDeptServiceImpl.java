package cn.qbs.wa.train.logistics.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
/*import cn.qbs.wa.teach.course.api.RemoteCourseDeptService;
import cn.qbs.wa.teach.course.api.pojo.DTO.coursedept.CourseDeptAddRequestDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.coursedept.CourseDeptDetailResponseDTO;
import cn.qbs.wa.teach.exam.api.RemoteExamService;
import cn.qbs.wa.teach.exam.api.pojo.DTO.ExamDeptAddRequestDTO;
import cn.qbs.wa.teach.exam.api.pojo.DTO.ExamDeptBatchAddRequestDTO;
import cn.qbs.wa.teach.exam.api.pojo.DTO.ExamDeptDTO;*/
import cn.qbs.wa.train.logistics.entity.StudentDept;
import cn.qbs.wa.train.logistics.mapper.StudentDeptMapper;
import cn.qbs.wa.train.logistics.pojo.student.StudentIdAndUserIdRequest;
import cn.qbs.wa.train.logistics.pojo.studentdept.*;
import cn.qbs.wa.train.logistics.service.StudentDeptService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 学员部门表(StudentDept)表服务实现类
 *
 * @author makejava
 * @since 2022-05-09 15:15:30
 */
@Slf4j
@Service("studentDeptService")
public class StudentDeptServiceImpl extends ServiceImpl<StudentDeptMapper, StudentDept> implements StudentDeptService {

    //@Autowired
    //TdmqProducerTemplate tdmqProducerTemplate;


    @Override
    public boolean batchAdd(StudentDeptBatchAddRequest params) {
        List<StudentDept> studentDeptList = params.getStudentIdList().stream().map(i -> {
            StudentDept studentDept = new StudentDept();
            studentDept.setStudentId(i.getStudentId());
            studentDept.setDeptId(params.getDeptId());
            return studentDept;
        }).collect(Collectors.toList());
        remove(new LambdaQueryWrapper<StudentDept>().in(StudentDept::getStudentId, params.getStudentIdList().stream().map(StudentIdAndUserIdRequest::getStudentId).collect(Collectors.toList())));
        saveBatch(studentDeptList);

        //MqTTaskerBatchAddDTO mqTTaskerBatchAddDTO = new MqTTaskerBatchAddDTO();
        //Long orgId = SecurityContextHolder.getOrgId();
        //List<MqTTaskerAddDTO> tTaskerAddDTOList = params.getStudentIdList().stream().map(i -> {
        //    MqTTaskerAddDTO mqTTaskerAddDTO = new MqTTaskerAddDTO();
        //    mqTTaskerAddDTO.setDeptId(params.getDeptId());
        //    mqTTaskerAddDTO.setStudentId(i.getStudentId());
        //    mqTTaskerAddDTO.setOrgId(orgId);
        //    mqTTaskerAddDTO.setUserId(i.getUserId());
        //    return mqTTaskerAddDTO;
        //}).collect(Collectors.toList());
        //mqTTaskerBatchAddDTO.setTaskerList(tTaskerAddDTOList);
        //tdmqProducerTemplate.sendAsyncStringMsg(Topics.TASKER_ADD_DEPT, JSON.toJSONString(mqTTaskerBatchAddDTO));
        //courseAddStudent(params.getDeptId());
        //examAddStudent(params.getDeptId());
        return true;
    }

    @Override
    public boolean examAddStudent(Long deptId){
       /* R<List<ExamDeptDTO>> examDeptList = remoteExamService.examByDeptId(new IdRequest(deptId));
        if (CollectionUtils.isEmpty(examDeptList.getData())){
            return true;
        }
        List<Long> examIdList = examDeptList.getData().stream().map(ExamDeptDTO::getExamId).distinct().collect(Collectors.toList());
        ExamDeptBatchAddRequestDTO examDeptBatchAddRequestDTO = new ExamDeptBatchAddRequestDTO();
        List<ExamDeptAddRequestDTO> examDeptAddRequestDTOList = new ArrayList<>();
        for (Long examId : examIdList){
            ExamDeptAddRequestDTO examDeptAddRequestDTO = new ExamDeptAddRequestDTO();
            examDeptAddRequestDTO.setExamId(examId);
            examDeptAddRequestDTO.setDeptId(deptId);
            examDeptAddRequestDTO.setOrgId(SecurityContextHolder.getOrgId());
            examDeptAddRequestDTOList.add(examDeptAddRequestDTO);
        }
        examDeptBatchAddRequestDTO.setExamDeptList(examDeptAddRequestDTOList);
        remoteExamService.addExamUserByDept(examDeptBatchAddRequestDTO);*/
        return true;
    }

    @Override
    public boolean courseAddStudent(Long deptId){
        /*R<List<CourseDeptDetailResponseDTO>> courseDeptList = remoteCourseDeptService.listByDeptId(new IdRequest(deptId));
        if (CollectionUtils.isEmpty(courseDeptList.getData())){
            return true;
        }
        List<Long> courseIdList = courseDeptList.getData().stream().map(CourseDeptDetailResponseDTO::getCourseId).distinct().collect(Collectors.toList());
        List<CourseDeptAddRequestDTO> courseDeptAddRequestDTOList = new ArrayList<>();
        for (Long courseId : courseIdList){
            CourseDeptAddRequestDTO courseDeptAddRequestDTO = new CourseDeptAddRequestDTO();
            courseDeptAddRequestDTO.setCourseId(courseId);
            courseDeptAddRequestDTO.setDeptId(deptId);
            courseDeptAddRequestDTO.setOrgId(SecurityContextHolder.getOrgId());
            courseDeptAddRequestDTOList.add(courseDeptAddRequestDTO);
        }
        remoteCourseDeptService.addCourseStudentByDept(courseDeptAddRequestDTOList);*/
        return true;
    }

    @Override
    public boolean add(StudentDeptSingleAddRequest params) {
        StudentDept studentDept = BeanUtil.copyProperties(params, StudentDept.class);
        remove(new LambdaQueryWrapper<StudentDept>().eq(StudentDept::getStudentId, params.getStudentId()));
        save(studentDept);

        //
        //MqTTaskerBatchAddDTO mqTTaskerBatchAddDTO = new MqTTaskerBatchAddDTO();
        //MqTTaskerAddDTO mqTTaskerAddDTO = new MqTTaskerAddDTO();
        //mqTTaskerAddDTO.setDeptId(studentDept.getDeptId());
        //mqTTaskerAddDTO.setStudentId(studentDept.getStudentId());
        //mqTTaskerAddDTO.setOrgId(SecurityContextHolder.getOrgId());
        //mqTTaskerBatchAddDTO.setTaskerList(ListUtil.toList(mqTTaskerAddDTO));
        //tdmqProducerTemplate.sendAsyncStringMsg(Topics.TASKER_ADD_DEPT, JSON.toJSONString(mqTTaskerBatchAddDTO));
        return true;
    }


    @Override
    public IPage<StudentDeptPageResponse> page(StudentDeptPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public StudentDeptDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(StudentDeptUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        StudentDept studentDept = new StudentDept();
        BeanUtils.copyProperties(params, studentDept);
        return this.updateById(studentDept);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

}

