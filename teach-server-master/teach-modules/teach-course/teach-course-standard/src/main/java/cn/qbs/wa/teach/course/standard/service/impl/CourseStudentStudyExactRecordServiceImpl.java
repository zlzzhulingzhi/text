//package cn.qbs.wa.teach.course.standard.service.impl;
//
//import cn.hutool.core.bean.BeanUtil;
//import cn.hutool.core.util.NumberUtil;
//import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
//import cn.qbs.wa.teach.common.core.domain.R;
//import cn.qbs.wa.teach.common.core.utils.DateFormatUtils;
//import cn.qbs.wa.teach.common.core.utils.StringUtils;
//import cn.qbs.wa.teach.course.common.entity.CourseStudentStudyExactRecord;
//import cn.qbs.wa.teach.course.common.enums.ComponentTypeEnum;
//import cn.qbs.wa.teach.course.live.api.pojo.DTO.live.LiveResultDTO;
//import cn.qbs.wa.teach.course.standard.constants.CourseConstants;
//import cn.qbs.wa.teach.course.standard.mapper.CourseStudentStudyExactRecordMapper;
//import cn.qbs.wa.teach.course.standard.pojo.coursecomponent.CourseComponentDetailResponse;
//import cn.qbs.wa.teach.course.standard.pojo.dto.PlaybackCourseStudyingDTO;
//import cn.qbs.wa.teach.course.standard.pojo.playback.PlaybackStatistic;
//import cn.qbs.wa.teach.course.standard.pojo.playback.WatchPlaybackStudentPageRequest;
//import cn.qbs.wa.teach.course.standard.pojo.playback.WatchPlaybackStudentPageResponse;
//import cn.qbs.wa.teach.course.standard.service.CourseComponentService;
//import cn.qbs.wa.teach.course.standard.service.CourseStudentStudyExactRecordService;
//import cn.qbs.wa.teach.course.standard.service.RemoteService;
//import cn.qbs.wa.teach.organization.api.RemoteStudentService;
//import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDTO;
//import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentSearchDTO;
//import com.alibaba.fastjson.JSON;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
///**
// * 【学员课程学习精准记录】(CourseStudentStudyExactRecord)表服务实现类
// *
// * @author zcm
// * @version 1.0
// * @date 2022-06-23 17:02:30
// */
//@Slf4j
//@Service("courseStudentStudyExactRecordService")
//public class CourseStudentStudyExactRecordServiceImpl extends ServiceImpl<CourseStudentStudyExactRecordMapper, CourseStudentStudyExactRecord> implements CourseStudentStudyExactRecordService {
//
//    @Resource
//    private CourseComponentService courseComponentService;
//
//    @Resource
//    private RemoteStudentService remoteStudentService;
//
//    @Resource
//    private RemoteService remoteService;
//
//
//    @Override
//    public CourseStudentStudyExactRecord addOrUpdate(PlaybackCourseStudyingDTO params) {
//        log.info("前端回放进度传参{}", JSON.toJSONString(params));
//        // 先计算该视频的播放进度
//        Long componentId = params.getComponentId();
//        CourseComponentDetailResponse component = courseComponentService.detail(componentId);
//        if (component == null) {
//            log.error("未找到对应的课程学习视频, courseId: {}, componentId: {}", params.getCourseId(), componentId);
//            return null;
//        }
//
//        Integer resourceFileDuration = component.getResourceFileDuration();
//        // 若为直播课，当直播总时长为空，尝试远程获取直播的总时长
//        if (ComponentTypeEnum.ZB.getCode().equals(component.getComponentTypeCode())) {
//            if (resourceFileDuration == null || resourceFileDuration <= 0) {
//                LiveResultDTO liveResultDTO = remoteService.remoteLive(component.getResourceFileId());
//                if (liveResultDTO != null && liveResultDTO.getTotalDuration() != null) {
//                    resourceFileDuration = Math.toIntExact(liveResultDTO.getTotalDuration());
//                }
//            }
//        }
//        Integer playbackPosition = params.getPlaybackPosition();
//        if (resourceFileDuration == null || resourceFileDuration <= 0 || playbackPosition > resourceFileDuration) {
//            log.error("课程学习视频数据不合法, courseId: {}, componentId: {}, resourceFileDuration: {}, playbackPosition: {}",
//                    params.getCourseId(), componentId, resourceFileDuration, playbackPosition);
//            return null;
//        }
//
//        CourseStudentStudyExactRecord courseStudentStudyExactRecord = BeanUtil.copyProperties(params, CourseStudentStudyExactRecord.class);
//        courseStudentStudyExactRecord.setUserId(SecurityContextHolder.getUserId());
//
//        // 只要开始学习后就算 1% 的课程学习进度
//        int progress = CourseConstants.STUDY_PROGRESS_BEGIN;
//        Long courseStudentStudyExactRecordId = courseStudentStudyExactRecord.getId();
//        if (courseStudentStudyExactRecordId == null) {
//            courseStudentStudyExactRecord.setPlaybackProgress(progress);
//            save(courseStudentStudyExactRecord);
//        } else {
//            CourseStudentStudyExactRecord dbCourseStudentStudyExactRecord = this.getById(courseStudentStudyExactRecordId);
//            if (dbCourseStudentStudyExactRecord == null) {
//                log.error("回放记录为空, {courseStudentStudyExactRecordId: {}}", courseStudentStudyExactRecordId);
//                return null;
//            }
//
//            Long learningDuration = Optional.ofNullable(dbCourseStudentStudyExactRecord.getLearningDuration()).orElse(0) + params.getLearningDuration();
//            if (resourceFileDuration - learningDuration <= CourseConstants.STUDY_PROGRESS_OFFSET) {
//                progress = CourseConstants.STUDY_PROGRESS_COMPLETED;
//            } else {
//                int calc = NumberUtil.parseInt(NumberUtil.div(learningDuration, resourceFileDuration, 2).multiply(BigDecimal.valueOf(CourseConstants.STUDY_PROGRESS_COMPLETED)).toString());
//                progress = NumberUtil.max(calc, progress);
//            }
//            courseStudentStudyExactRecord.setPlaybackProgress(progress);
//            courseStudentStudyExactRecord.setLearningDuration(learningDuration.intValue());
//            updateById(courseStudentStudyExactRecord);
//        }
//        return courseStudentStudyExactRecord;
//    }
//
//    @Override
//    public PlaybackStatistic getStatisticData(Long basicLiveId) {
//        if (basicLiveId != null) {
//            PlaybackStatistic basicLiveStatistic = getBaseMapper().selectStatistic(basicLiveId);
//            return basicLiveStatistic;
//        }
//        return null;
//    }
//
//    @Override
//    public IPage<WatchPlaybackStudentPageResponse> watchPlaybackStudentPage(WatchPlaybackStudentPageRequest params) {
//        String studentName = params.getStudentName();
//        Map<Long, StudentDTO> userIdStudentMap = null;
//        if (StringUtils.isNotBlank(studentName)) {
//            StudentSearchDTO searchDTO = new StudentSearchDTO();
//            searchDTO.setRealName(studentName);
//            searchDTO.setOrgId(SecurityContextHolder.getOrgId());
//            R<List<StudentDTO>> r = remoteStudentService.list(searchDTO);
//            if (r != null && r.isOk()) {
//                List<StudentDTO> studentDTOList = r.getData();
//                if (CollectionUtils.isNotEmpty(studentDTOList)) {
//                    userIdStudentMap = studentDTOList.stream().collect(Collectors.toMap(StudentDTO::getUserId, Function.identity()));
//                }
//            }
//
//            if (userIdStudentMap == null) {
//                return new Page(params.getCurrent(), params.getSize(), 0L);
//            } else {
//                params.setUserIdList(userIdStudentMap.keySet());
//            }
//        }
//
//        IPage<WatchPlaybackStudentPageResponse> page = getBaseMapper().page(params.createMpPage(), params);
//        List<WatchPlaybackStudentPageResponse> watchLiveStudentList = page.getRecords();
//        if (CollectionUtils.isNotEmpty(watchLiveStudentList)) {
//            if (userIdStudentMap == null) {
//                List<Long> userIdList = watchLiveStudentList.stream().map(WatchPlaybackStudentPageResponse::getUserId).collect(Collectors.toList());
//                StudentSearchDTO searchDTO = new StudentSearchDTO();
//                searchDTO.setUserIds(userIdList);
//                searchDTO.setOrgId(SecurityContextHolder.getOrgId());
//                R<List<StudentDTO>> r = remoteStudentService.list(searchDTO);
//                if (r != null && r.isOk()) {
//                    List<StudentDTO> studentDTOList = r.getData();
//                    if (CollectionUtils.isNotEmpty(studentDTOList)) {
//                        userIdStudentMap = studentDTOList.stream().collect(Collectors.toMap(StudentDTO::getUserId, Function.identity()));
//                    }
//                }
//            }
//
//            if (userIdStudentMap != null) {
//                for (WatchPlaybackStudentPageResponse watchLiveStudent : watchLiveStudentList) {
//                    StudentDTO studentDTO = userIdStudentMap.get(watchLiveStudent.getUserId());
//                    if (studentDTO != null) {
//                        watchLiveStudent.setStudentName(studentDTO.getRealName());
//                        watchLiveStudent.setPhone(studentDTO.getPhone());
//                    }
//                }
//            }
//
//            // 格式化累计观看时长
//            watchLiveStudentList.forEach(i -> {
//                if (StringUtils.isNotEmpty(i.getCumulativeWatchTime())) {
//                    i.setCumulativeWatchTime(DateFormatUtils.secondsFormat(Integer.valueOf(i.getCumulativeWatchTime())));
//                }
//            });
//        }
//        return page;
//    }
//
//
//}
//
