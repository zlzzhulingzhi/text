package cn.qbs.wa.train.logistics.service.impl;

import cn.qbs.wa.teach.common.core.domain.IdStudentRequest;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
/*import cn.qbs.wa.teach.course.api.RemoteCourseGroupService;
import cn.qbs.wa.teach.course.api.pojo.DTO.coursegroup.CourseGroupAddRequestDTO;
import cn.qbs.wa.teach.course.api.pojo.DTO.coursegroup.CourseGroupDetailResponseDTO;
import cn.qbs.wa.teach.exam.api.RemoteExamService;
import cn.qbs.wa.teach.exam.api.pojo.DTO.ExamGroupAddRequestDTO;
import cn.qbs.wa.teach.exam.api.pojo.DTO.ExamGroupBatchAddRequestDTO;
import cn.qbs.wa.teach.exam.api.pojo.DTO.ExamGroupDTO;*/
import cn.qbs.wa.train.logistics.entity.StudentGroup;
import cn.qbs.wa.train.logistics.mapper.StudentGroupMapper;
import cn.qbs.wa.train.logistics.pojo.student.StudentIdAndUserIdRequest;
import cn.qbs.wa.train.logistics.pojo.studentgroup.*;
import cn.qbs.wa.train.logistics.service.StudentGroupService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 学员-分组(StudentGroup)表服务实现类
 *
 * @author makejava
 * @since 2022-03-28 16:07:14
 */
@Slf4j
@Service("studentGroupService")
public class StudentGroupServiceImpl extends ServiceImpl<StudentGroupMapper, StudentGroup> implements StudentGroupService {

    @Resource
    StudentGroupMapper studentGroupMapper;

    //@Autowired
    //TdmqProducerTemplate tdmqProducerTemplate;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(StudentGroupAddRequest params) {
        List<StudentGroup> studentGroups = new ArrayList<>();
        for (Long groupId : params.getGroupIds()) {
            StudentGroup studentGroup = new StudentGroup();
            studentGroup.setStudentId(params.getStudentId());
            studentGroup.setGroupId(groupId);
            int count = baseMapper.selectGroup(studentGroup).getCount();
            if (count == 0) {
                studentGroups.add(studentGroup);
            }
        }
        saveBatch(studentGroups);
        //Long orgId = SecurityContextHolder.getOrgId();
        //MqTTaskerBatchAddDTO mqTTaskerBatchAddDTO = new MqTTaskerBatchAddDTO();
        //List<MqTTaskerAddDTO> taskerList = BeanUtil.copyToList(studentGroups, MqTTaskerAddDTO.class);
        //taskerList.forEach(i -> {
        //    i.setOrgId(orgId);
        //    i.setUserId(params.getUserId());
        //});
        //mqTTaskerBatchAddDTO.setTaskerList(taskerList);
        //tdmqProducerTemplate.sendAsyncStringMsg(Topics.TASKER_ADD_GROUP, JSON.toJSONString(mqTTaskerBatchAddDTO));
        //courseAddStudent(studentGroups.stream().map(StudentGroup::getGroupId).collect(Collectors.toList()));
        //examAddStudent(studentGroups.stream().map(StudentGroup::getGroupId).collect(Collectors.toList()));
        return true;
    }

    @Override
    public boolean addList(StudentGroupAddRequest params) {
        List<StudentGroup> studentGroups = new ArrayList<>();
        for (StudentIdAndUserIdRequest studentId : params.getStudentIdList()) {
            for (Long groupId : params.getGroupIds()) {
                StudentGroup studentGroup = new StudentGroup();
                studentGroup.setStudentId(studentId.getStudentId());
                studentGroup.setGroupId(groupId);
                int count = baseMapper.selectGroup(studentGroup).getCount();
                if (count == 0) {
                    studentGroups.add(studentGroup);
                }
            }
        }
        saveBatch(studentGroups);

        //Long orgId = SecurityContextHolder.getOrgId();
        //MqTTaskerBatchAddDTO mqTTaskerBatchAddDTO = new MqTTaskerBatchAddDTO();
        //List<MqTTaskerAddDTO> taskerList = BeanUtil.copyToList(studentGroups, MqTTaskerAddDTO.class);
        //
        //Map<Long, Long> userMap = params.getStudentIdList().stream().collect(Collectors.toMap(StudentIdAndUserIdRequest::getStudentId, StudentIdAndUserIdRequest::getUserId));
        //taskerList.forEach(i -> {
        //    i.setOrgId(orgId);
        //    i.setUserId(userMap.get(i.getStudentId()));
        //});
        //mqTTaskerBatchAddDTO.setTaskerList(taskerList);
        //tdmqProducerTemplate.sendAsyncStringMsg(Topics.TASKER_ADD_GROUP, JSON.toJSONString(mqTTaskerBatchAddDTO));
        //courseAddStudent(studentGroups.stream().map(StudentGroup::getGroupId).collect(Collectors.toList()));
        //examAddStudent(studentGroups.stream().map(StudentGroup::getGroupId).collect(Collectors.toList()));
        return true;
    }

    private boolean examAddStudent(List<Long> groupIdList){
        /*for (Long groupId : groupIdList){
            IdListRequest idListRequest = new IdListRequest();
            idListRequest.setIdList(Collections.singletonList(groupId));
            R<List<ExamGroupDTO>> examGroupList = remoteExamService.examByGroupIdList(idListRequest);
            if (CollectionUtils.isEmpty(examGroupList.getData())){
                return true;
            }
            List<Long> examIdList = examGroupList.getData().stream().map(ExamGroupDTO::getExamId).distinct().collect(Collectors.toList());
            ExamGroupBatchAddRequestDTO examGroupBatchAddRequestDTO = new ExamGroupBatchAddRequestDTO();
            List<ExamGroupAddRequestDTO> examGroupAddRequestDTOList = new ArrayList<>();
            for (Long examId : examIdList){
                ExamGroupAddRequestDTO examGroupAddRequestDTO = new ExamGroupAddRequestDTO();
                examGroupAddRequestDTO.setExamId(examId);
                examGroupAddRequestDTO.setGroupId(groupId);
                examGroupAddRequestDTO.setOrgId(SecurityContextHolder.getOrgId());
                examGroupAddRequestDTOList.add(examGroupAddRequestDTO);
            }
            examGroupBatchAddRequestDTO.setExamGroupList(examGroupAddRequestDTOList);
            remoteExamService.addExamUserByGroup(examGroupBatchAddRequestDTO);
        }*/
        return true;
    }

    private boolean courseAddStudent(List<Long> groupIdList){
        /*for (Long groupId : groupIdList){
            IdListRequest request = new IdListRequest();
            request.setIdList(Collections.singletonList(groupId));
            R<List<CourseGroupDetailResponseDTO>> courseGroupList = remoteCourseGroupService.listByGroupId(request);
            if (CollectionUtils.isEmpty(courseGroupList.getData())){
                return true;
            }
            List<Long> courseIdList = courseGroupList.getData().stream().map(CourseGroupDetailResponseDTO::getCourseId).distinct().collect(Collectors.toList());
            List<CourseGroupAddRequestDTO> courseGroupAddRequestDTOList = new ArrayList<>();
            for (Long courseId : courseIdList){
                CourseGroupAddRequestDTO courseGroupAddRequestDTO = new CourseGroupAddRequestDTO();
                courseGroupAddRequestDTO.setCourseId(courseId);
                courseGroupAddRequestDTO.setGroupId(groupId);
                courseGroupAddRequestDTO.setOrgId(SecurityContextHolder.getOrgId());
                courseGroupAddRequestDTOList.add(courseGroupAddRequestDTO);
            }
            remoteCourseGroupService.addCourseStudentByGroup(courseGroupAddRequestDTOList);
        }*/
        return true;
    }

    @Override
    public IPage<StudentGroupPageResponse> page(StudentGroupPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public List<StudentGroupDetailResponse> detail(IdStudentRequest request) {
//        List<StudentGroupDetailResponse> studentGroup = new ArrayList<>();
//        List<StudentGroup> list = list();
        List<StudentGroupDetailResponse> studentGroup = studentGroupMapper.selectAll();
        if (StringUtils.isNotBlank(request.getId().toString())) {
            //单个学生标签操作,查询学生标签及所有标签
            List<StudentGroup> selectList = studentGroupMapper.selectList(new QueryWrapper<StudentGroup>().eq("student_id", request.getId()));
            if (StringUtils.isNotEmpty(selectList)) {
                for (StudentGroupDetailResponse studentGroupDetailResponse : studentGroup) {
                    for (StudentGroup studentGroup1 : selectList) {
                        if (studentGroup1.getGroupId().toString().equals(studentGroupDetailResponse.getId().toString())) {
                            //说明是已选中的标签
                            studentGroupDetailResponse.setFlag(0);
                            studentGroupDetailResponse.setStudentGroupId(studentGroup1.getId());
                            studentGroupDetailResponse.setStudentId(studentGroup1.getStudentId());
                        }
                    }
                }
            }
        }
        return studentGroup;
    }

    @Override
    public boolean update(StudentGroupUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        StudentGroup studentGroup = new StudentGroup();
        BeanUtils.copyProperties(params, studentGroup);
        return this.updateById(studentGroup);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

}

