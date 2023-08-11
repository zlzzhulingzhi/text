package cn.qbs.wa.teach.course.standard.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.constant.SecurityConstants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.course.common.entity.Course;
import cn.qbs.wa.teach.course.common.entity.CourseComponent;
import cn.qbs.wa.teach.course.common.entity.CourseLiveLink;
import cn.qbs.wa.teach.course.common.entity.CourseStudent;
import cn.qbs.wa.teach.course.common.enums.CourseTypeEnum;
import cn.qbs.wa.teach.course.common.enums.SectionEnum;
import cn.qbs.wa.teach.course.standard.mapper.CourseComponentMapper;
import cn.qbs.wa.teach.course.standard.mapper.CourseLiveLinkMapper;
import cn.qbs.wa.teach.course.standard.mapper.CourseMapper;
import cn.qbs.wa.teach.course.standard.mapper.CourseStudentMapper;
import cn.qbs.wa.teach.course.standard.pojo.course.CourseDetailResponse;
import cn.qbs.wa.teach.course.standard.pojo.coursecomponentattachment.CourseComponentAttachmentDetailResponse;
import cn.qbs.wa.teach.course.standard.pojo.coursestudent.*;
import cn.qbs.wa.teach.course.standard.pojo.dto.*;
import cn.qbs.wa.teach.course.standard.service.CourseStatisticOverviewService;
import cn.qbs.wa.teach.course.standard.service.CourseStudentLessonService;
import cn.qbs.wa.teach.course.standard.service.CourseStudentService;
import cn.qbs.wa.teach.course.standard.service.RemoteService;
import cn.qbs.wa.teach.notification.api.RemoteNotificationService;
import cn.qbs.wa.teach.notification.api.enums.NotificationBusinessEnum;
import cn.qbs.wa.teach.notification.api.pojo.DTO.notification.SendNotificationDTO;
import cn.qbs.wa.teach.notification.api.pojo.DTO.notification.SendNotificationUserInfoDTO;
import cn.qbs.wa.teach.organization.api.RemoteStudentService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentGroupListDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentSearchDTO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 【课程学员】(CourseStudent)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:40
 */
@Slf4j
@Service("courseStudentService")
public class CourseStudentServiceImpl extends ServiceImpl<CourseStudentMapper, CourseStudent> implements CourseStudentService {

    /**
     * 单次增加的学员人数
     */
    private static final int SINGLE_INCREASE_STUDENT_COUNT = 1;

    @Resource
    private RemoteService remoteService;

    @Resource
    private CourseStatisticOverviewService courseStatisticOverviewService;

    @Resource
    private CourseStudentLessonService courseStudentLessonService;

    //@Resource
    //private RemoteLiveShowService remoteLiveShowService;

    @Resource
    private CourseLiveLinkMapper courseLiveLinkMapper;

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private CourseComponentMapper courseComponentMapper;

    @Resource
    private RemoteNotificationService remoteNotificationService;

    @Resource
    private RemoteStudentService remoteStudentService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean add(CourseStudentAddRequest params) {
        CourseStudent courseStudent = new CourseStudent();
        BeanUtils.copyProperties(params, courseStudent);
        // 报名时间记为当前的时间
        courseStudent.setSignUpTime(LocalDateTime.now());
        boolean saveResult = this.save(courseStudent);

        // 添加成功增加对应的报名人数和购买人数
        if (saveResult) {
            Long courseId = params.getCourseId();
            //this.courseStatisticOverviewService.incrSignUpNum(courseId, SINGLE_INCREASE_STUDENT_COUNT);
            //if (!params.getAssign()){
            //    //不是通过后台手动指派报名，需要增加购买人数
            //    this.courseStatisticOverviewService.incrBuyerNum(courseId, SINGLE_INCREASE_STUDENT_COUNT);
            //}
            // 记录学员报名时课程所需要学习的课次信息
            this.courseStudentLessonService.addByCourseIdAndUser(courseId, params.getUserId());
        }
        return saveResult;
    }

    @Override
    public IPage<CourseStudentPageResponse> page(CourseStudentPageRequest params) {
        StudentSearchDTO searchDTO = new StudentSearchDTO();
        if ("real_name".equals(params.getSortField())) {
            searchDTO.setStudentSortField("realName");
            searchDTO.setStudentSortOrder(params.getSortOrder());
            // 避免SQL报错，置空
            params.setSortField(null);
            params.setSortOrder(null);
        }
        BeanUtil.copyProperties(params, searchDTO);
        R<List<StudentDTO>> studentList = remoteStudentService.list(searchDTO);
        if (CollectionUtils.isNotEmpty(studentList.getData())){
            params.setUserIds(studentList.getData().stream().map(StudentDTO::getUserId).collect(Collectors.toList()));
        }else if (StringUtils.isNotEmpty(params.getPhone()) || StringUtils.isNotEmpty(params.getRealName())
                || CollectionUtils.isNotEmpty(params.getDeptIdList()) || CollectionUtils.isNotEmpty(params.getGroupIdList())){
            return new Page<>(params.getCurrent(), params.getSize(), 0L);
        }
        IPage<CourseStudentPageResponse> courseStudentPage = this.baseMapper.page(params.createMpPage(), params);
        List<CourseStudentPageResponse> responses = courseStudentPage.getRecords();
        for (CourseStudentPageResponse courseStudentPageResponse:responses) {
            courseStudentPageResponse.setSignUpTime(courseStudentPageResponse.getCreateTime());
        }
        this.requestRemoteUserMsg(responses);
        return courseStudentPage;
    }

    @Override
    public IPage<CourseStudentPageResponse> pageV2(CourseStudentPageRequest params) {
        StudentSearchDTO searchDTO = new StudentSearchDTO();
        if ("real_name".equals(params.getSortField())) {
            searchDTO.setStudentSortField("realName");
            searchDTO.setStudentSortOrder(params.getSortOrder());
            // 避免SQL报错，置空
            params.setSortField(null);
            params.setSortOrder(null);
        }
        BeanUtil.copyProperties(params, searchDTO);
        R<List<StudentDTO>> studentList = remoteStudentService.list(searchDTO);
        if (CollectionUtils.isNotEmpty(studentList.getData())){
            params.setUserIds(studentList.getData().stream().map(StudentDTO::getUserId).collect(Collectors.toList()));
        }else if (StringUtils.isNotEmpty(params.getPhone()) || StringUtils.isNotEmpty(params.getRealName())
                || CollectionUtils.isNotEmpty(params.getDeptIdList()) || CollectionUtils.isNotEmpty(params.getGroupIdList())){
            return new Page<>(params.getCurrent(), params.getSize(), 0L);
        }
        IPage<CourseStudentPageResponse> courseStudentPage = this.baseMapper.pageV2(params.createMpPage(), params);
        List<CourseStudentPageResponse> responses = courseStudentPage.getRecords();
        for (CourseStudentPageResponse courseStudentPageResponse:responses) {
            courseStudentPageResponse.setSignUpTime(courseStudentPageResponse.getCreateTime());
            if (CollectionUtils.isNotEmpty(studentList.getData())){
                for (StudentDTO studentDTO:studentList.getRemoteData()) {
                    if(courseStudentPageResponse.getStudentId().equals(studentDTO.getId())){
                        courseStudentPageResponse.setRealName(studentDTO.getRealName());
                        courseStudentPageResponse.setPhone(studentDTO.getPhone());
                    }
                }
            }
        }
        //this.requestRemoteUserMsg(responses);
        return courseStudentPage;
    }

    @Override
    public CourseStudentDetailResponse detail(Serializable id) {
        return this.baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(CourseStudentUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        CourseStudent courseStudent = new CourseStudent();
        BeanUtils.copyProperties(params, courseStudent);
        return this.updateById(courseStudent);
    }

    @Override
    public boolean manualRemoveByIds(List<Long> idList) {
        List<CourseStudent> courseStudents = this.listByIds(idList);
        if (courseStudents.isEmpty()) {
            return false;
        }
        boolean deleteResult = this.removeByIds(idList);
        if (deleteResult) {
            //Map<Long, Long> courseCountMap = courseStudents.stream().collect(Collectors.groupingBy(CourseStudent::getCourseId, Collectors.counting()));
            //for (Long courseId : courseCountMap.keySet()) {
            //    // 由于是删除的人数，所以要取反，直接取负值
            //    int count = -courseCountMap.get(courseId).intValue();
            //    this.courseStatisticOverviewService.incrSignUpNum(courseId, count);
            //}
            for (CourseStudent courseStudent : courseStudents) {
                // 删除学员报名时课程所需要学习的课次信息
                this.courseStudentLessonService.deleteByCourseIdAndUser(courseStudent.getCourseId(), courseStudent.getUserId());
            }
            // 刷新课程学员
            Long courseId = courseStudents.get(0).getCourseId();
            this.courseStatisticOverviewService.refreshStatistic(courseId);
        }
        return deleteResult;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteByIds(List<Long> idList) {
        List<CourseStudent> courseStudents = this.listByIds(idList);
        if (courseStudents.isEmpty()) {
            return false;
        }
        boolean deleteResult = this.removeByIds(idList);
        if (deleteResult) {
            //Map<Long, Long> courseCountMap = courseStudents.stream().collect(Collectors.groupingBy(CourseStudent::getCourseId, Collectors.counting()));
            //for (Long courseId : courseCountMap.keySet()) {
            //    // 由于是删除的人数，所以要取反，直接取负值
            //    int count = -courseCountMap.get(courseId).intValue();
            //    this.courseStatisticOverviewService.incrSignUpNum(courseId, count);
            //    this.courseStatisticOverviewService.incrBuyerNum(courseId, count);
            //}
            for (CourseStudent courseStudent : courseStudents) {
                // 删除学员报名时课程所需要学习的课次信息
                this.courseStudentLessonService.deleteByCourseIdAndUser(courseStudent.getCourseId(), courseStudent.getUserId());
            }
            // 刷新课程学员
            Long courseId = courseStudents.get(0).getCourseId();
            this.courseStatisticOverviewService.refreshStatistic(courseId);
        }
        return deleteResult;
    }


    @Override
    public List<CourseStudentExcelDTO> generateExcelData(CourseStudentPageRequest params) {
        List<CourseStudentPageResponse> courseStudentPageResponses = this.baseMapper.getCourseStudentList(params);
        for (CourseStudentPageResponse courseStudentPageResponse:courseStudentPageResponses) {
            courseStudentPageResponse.setSignUpTime(courseStudentPageResponse.getCreateTime());
        }
        this.requestRemoteUserMsg(courseStudentPageResponses);
        return CourseStudentServiceImpl.convertCourseStudentPageResponseToCourseStudentExcelDTO(courseStudentPageResponses);
    }

    /**
     * 请求远程的学员数据
     *
     * @param data 课程学员数据
     */
    private void requestRemoteUserMsg(List<CourseStudentPageResponse> data) {
        if (data.isEmpty()) {
            return;
        }
        // 有对应数据再去查远程的用户数据
        List<Long> userIdList = data.stream().map(CourseStudentPageResponse::getUserId).distinct().collect(Collectors.toList());
        Map<Long, StudentDTO> map = remoteService.remoteStudentMap(userIdList);
        for (CourseStudentPageResponse response : data) {
            Optional.ofNullable(map.get(response.getUserId())).ifPresent(m -> {
                response.setNickName(m.getNickName());
                response.setRealName(m.getRealName());
                response.setPhone(m.getPhone());
                response.setDeptName(m.getDeptName());
                response.setEnabled(m.getEnabled() == 1 ? "可用" : "不可用");
                response.setGroupNames(subString(m.getGroupList().stream().map(StudentGroupListDTO::getGroupName).collect(Collectors.toList()).toString()));
            });
        }
    }

    private String subString(String groupList){
        String groupName = groupList.substring(1, groupList.lastIndexOf("]"));
        return groupName;
    }

    /**
     * 进行数据类型转换，转换为下载的Excel类型
     *
     * @param sourceData 源数据类型
     * @return Excel数据类型
     */
    private static List<CourseStudentExcelDTO> convertCourseStudentPageResponseToCourseStudentExcelDTO(List<CourseStudentPageResponse> sourceData) {
        if (sourceData.isEmpty()) {
            return Collections.emptyList();
        }

        ArrayList<CourseStudentExcelDTO> targetData = new ArrayList<>(sourceData.size());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm");
        for (CourseStudentPageResponse response : sourceData) {
            CourseStudentExcelDTO courseStudentExcelDTO = new CourseStudentExcelDTO();
            String nickName = response.getNickName();
            String realName = response.getRealName();
            // 真实姓名为空，则用昵称，否则用真实姓名
            courseStudentExcelDTO.setUsername(StringUtils.isNotBlank(realName) ? realName : nickName);
            courseStudentExcelDTO.setPhoneNum(StringUtils.trimToEmpty(response.getPhone()));
            //courseStudentExcelDTO.setLearningRate(StringUtils.trimToEmpty(String.valueOf(response.getLearningRate())+"%"));
            courseStudentExcelDTO.setEnabled(response.getEnabled());
           // courseStudentExcelDTO.setDeptName(response.getDeptName());
            courseStudentExcelDTO.setGroupNames(response.getGroupNames());

            if (response.getSignUpTime() != null) {
                courseStudentExcelDTO.setSignUpTime(response.getSignUpTime().format(formatter));
            }
            targetData.add(courseStudentExcelDTO);
        }

        return targetData;
    }

    @Override
    public boolean checkCourseStudent(Long courseId) {
        Long count = this.lambdaQuery().eq(CourseStudent::getCourseId, courseId).count();
        return count != null && count > 0;
    }

    @Override
    public List<CourseLessonSimpleResponseDTO> detail(List<MyCourseDetailRequestDTO> params) {

        List<CourseLessonSimpleResponseDTO> lastCourseStudyRecordById = this.baseMapper.getLastCourseStudyRecordById(params, SecurityContextHolder.getUserId());

            /*for (MyCourseDetailRequestDTO param : params) {
                for (CourseLessonSimpleResponseDTO courseLessonSimpleResponseDTO : lastCourseStudyRecordById) {
                    courseLessonSimpleResponseDTO.setShelfStatus(courseMapper.selectById(param.getCourseId()).getShelfStatus());
                }
            }*/

        return lastCourseStudyRecordById;
    }

    @Override
    public List<CourseDetailResponse> info(List<MyCourseDetailRequestDTO> params) {
        if (CollUtil.isNotEmpty(params)) {
            List<Course> courseList = courseMapper.selectBatchIds(params.stream().map(MyCourseDetailRequestDTO::getCourseId).collect(Collectors.toList()));
            List<CourseDetailResponse> courseDetailResponseList = new ArrayList<>();
            //BeanUtil.copyToList(courseList, CourseDetailResponse.class);
            for (Course course : courseList) {
                CourseDetailResponse courseDetailResponse = new CourseDetailResponse();
                BeanUtil.copyProperties(course, courseDetailResponse);
                courseDetailResponseList.add(courseDetailResponse);
            }
            return courseDetailResponseList;
        }
        return null;
    }

    @Override
    public IPage<MyCoursePageDTO> pageMyCourse(MyCoursePageSearchDTO params) {
        IPage<MyCoursePageDTO> page = this.baseMapper.pageMyCourse(params.createMpPage(), params);
        for (MyCoursePageDTO myCourse : page.getRecords()) {
            // 查询课程的最后学习记录
            myCourse.setLesson(this.baseMapper.getLastCourseStudyRecord(params.getUserId(), myCourse.getId()));
            if (myCourse.getLesson() == null) {
                CourseComponent component = courseComponentMapper.selectOne(Wrappers.<CourseComponent>lambdaQuery().eq(CourseComponent::getCourseId, myCourse.getId()).orderByAsc(CourseComponent::getId).last("LIMIT 1"));
                if (component != null) {
                    CourseLessonSimpleDTO courseLessonSimpleDTO = new CourseLessonSimpleDTO();
                    courseLessonSimpleDTO.setComponentId(component.getId());
                    courseLessonSimpleDTO.setLessonId(component.getLessonId());
                    myCourse.setLesson(courseLessonSimpleDTO);
                }
            }
            //查询直播是否开始,//只有直播和综合课可以关联直播
            if (CourseTypeEnum.LIVE.getCode().equals(myCourse.getCourseType()) || CourseTypeEnum.MIX.getCode().equals(myCourse.getCourseType())) {
                //未开章节
                if (SectionEnum.OFF.getCode().toString().equals(myCourse.getCourseManage().toString())) {

                    CourseLiveLink courseLiveLink = courseLiveLinkMapper.selectOne(new QueryWrapper<CourseLiveLink>().eq("course_id", myCourse.getId()));
                    extracted(myCourse, courseLiveLink.getLiveId());
                } else {
                    //开启章节后的课程
                    List<CourseComponentAttachmentDetailResponse> myCoursePageDTOList = baseMapper.getLiveId(myCourse);
                    if (StringUtils.isNotEmpty(myCoursePageDTOList)) {
                        for (CourseComponentAttachmentDetailResponse courseComponentAttachmentDetailResponse : myCoursePageDTOList) {
                            extracted(myCourse, courseComponentAttachmentDetailResponse.getResourceFileId());
                        }
                    }
                }
            }
        }
        return page;
    }


    private void extracted(MyCoursePageDTO myCourse, Long basicLiveId) {
        //BasicLiveShowInfoDTO basicLiveShowInfoDTO = new BasicLiveShowInfoDTO();
        //basicLiveShowInfoDTO.setBasicLiveId(basicLiveId);
        //BasicLiveShowInfoResultDTO data = remoteLiveShowService.getInfo(basicLiveShowInfoDTO).getData();
        //if (StringUtils.isNotNull(data)) {
        //    if (data.getStatus() == null) {
        //        myCourse.setLiveStatus(LiveStatusEnum.NOT.getNumber());
        //    } else if (LiveStatusEnum.LIVE.getCode().equals(data.getStatus().toString())) {
        //        //只要在直播中, 就默认值
        //        myCourse.setLiveStatus(LiveStatusEnum.LIVE.getNumber());
        //        return;
        //    } else {
        //        myCourse.setLiveStatus(data.getStatus());
        //    }
        //}
    }

    @Override
    public CourseStudent getStudentCourse(Long userId, Long courseId) {
        return this.lambdaQuery().eq(CourseStudent::getUserId, userId).eq(CourseStudent::getCourseId, courseId).one();
    }

    @Override
    public List<CourseStudent> listCourseByUserId(Long userId) {
        return this.lambdaQuery().eq(CourseStudent::getUserId, userId).list();
    }

    @Override
    public void dropOut(Long courseId, Long studentId) {
        CourseStudent courseStudent = this.lambdaQuery().eq(CourseStudent::getCourseId, courseId).eq(CourseStudent::getStudentId, studentId).one();
        if (courseStudent != null) {
            List<Long> ids = new ArrayList<>(1);
            ids.add(courseStudent.getId());
            this.deleteByIds(ids);
        }
    }

    @Override
    public boolean isSignUp(Long courseId, Long studentId) {
        if (studentId == null) {
            return false;
        }
        Long count = this.lambdaQuery().eq(CourseStudent::getCourseId, courseId).eq(CourseStudent::getStudentId, studentId).count();
        if (count != null && count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean addCourseStudent(List<Long> courseIdList, List<StudentDTO> studentDTOList) {
        if (CollectionUtils.isNotEmpty(studentDTOList)){
            //先拼装所有的课程学员表数据
            List<CourseStudent> addStudentList = new ArrayList<>();
            LocalDateTime now = LocalDateTime.now();
            for (Long courseId : courseIdList){
                for (StudentDTO student : studentDTOList){
                    CourseStudent courseStudent = new CourseStudent();
                    courseStudent.setStudentId(student.getId());
                    courseStudent.setSignUpTime(now);
                    courseStudent.setUserId(student.getUserId());
                    courseStudent.setCourseId(courseId);
                    addStudentList.add(courseStudent);
                }
            }
            //把需要添加课程的学员筛选出来
            addStudentList = addStudentList.stream().filter(i -> {
                Long count = this.count(Wrappers.<CourseStudent>lambdaQuery().eq(CourseStudent::getCourseId, i.getCourseId()).eq(CourseStudent::getUserId, i.getUserId()));
                return count <= 0;
            }).collect(Collectors.toList());
            this.saveBatch(addStudentList);

            //课程增加报名人数
            Map<Long, Long> courseCountMap = addStudentList.stream().collect(Collectors.groupingBy(CourseStudent::getCourseId, Collectors.counting()));
            for (Long courseId : courseCountMap.keySet()){
                //Integer count = courseCountMap.get(courseId).intValue();
                //this.courseStatisticOverviewService.incrSignUpNum(courseId, count);

                for (CourseStudent courseStudent : addStudentList){
                    if (courseStudent.getCourseId().equals(courseId)){
                        // 记录学员报名时课程所需要学习的课次信息
                        this.courseStudentLessonService.addByCourseIdAndUser(courseId, courseStudent.getUserId());
                    }
                }
            }

            //开启新的线程发送通知
            Map<Long, StudentDTO> studentMap = studentDTOList.stream().collect(Collectors.toMap(StudentDTO::getUserId, o -> o));
            List<CourseStudent> sendStudentList = addStudentList;
            CompletableFuture.runAsync(() -> {
                sendNotification(sendStudentList, studentMap, SecurityContextHolder.getOrgId());
            }).exceptionally(e -> {
                log.error("通知发送异常：sendStudentList:{}-studentMap:{}", sendStudentList, studentMap);
                return null;
            });

        }
        return true;
    }

    private void sendNotification(List<CourseStudent> sendStudentList, Map<Long, StudentDTO> studentMap, Long orgId){
        for (CourseStudent courseStudent : sendStudentList){
            SendNotificationDTO sendNotificationDTO = new SendNotificationDTO();
            SecurityContextHolder.setSelectOrgId(orgId.toString());
            SecurityContextHolder.setOrgId(orgId.toString());
            sendNotificationDTO.setOrgId(orgId);
            sendNotificationDTO.setBusinessId(courseStudent.getCourseId());
            sendNotificationDTO.setBusinessType(NotificationBusinessEnum.CLASSROOM);

            List<SendNotificationUserInfoDTO> sendNotificationUserInfoDTOList = new ArrayList<>();
            SendNotificationUserInfoDTO sendNotificationUserInfoDTO = new SendNotificationUserInfoDTO();
            sendNotificationUserInfoDTO.setUserId(courseStudent.getUserId());
            sendNotificationUserInfoDTO.setName(studentMap.get(courseStudent.getUserId()).getRealName());
            sendNotificationUserInfoDTO.setPhone(studentMap.get(courseStudent.getUserId()).getPhone());
            sendNotificationUserInfoDTOList.add(sendNotificationUserInfoDTO);
            sendNotificationDTO.setUserInfo(sendNotificationUserInfoDTOList);

            Map<String,String> templateKey = new HashMap<>(2);
            templateKey.put("courseName", courseMapper.selectById(courseStudent.getCourseId()).getCourseName());
            sendNotificationDTO.setTemplateKey(templateKey);
            remoteNotificationService.sendNotification(sendNotificationDTO, SecurityConstants.INNER);
        }
    }

    @Override
    public List<CourseStudent> queryStudentByPlat(CourseStudentQuery query) {
        return baseMapper.queryStudentByPlat(query);
    }
}

