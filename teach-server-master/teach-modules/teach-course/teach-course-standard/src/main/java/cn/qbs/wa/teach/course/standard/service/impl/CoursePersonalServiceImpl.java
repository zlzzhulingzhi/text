//package cn.qbs.wa.teach.course.standard.service.impl;
//
//import cn.hutool.core.bean.BeanUtil;
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.util.NumberUtil;
//import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
//import cn.qbs.wa.teach.common.core.exception.ServiceException;
//import cn.qbs.wa.teach.course.common.entity.CourseStudentLesson;
//import cn.qbs.wa.teach.course.common.entity.CourseStudentStudyRecord;
//import cn.qbs.wa.teach.course.common.entity.CourseStudentStudySnapshot;
//import cn.qbs.wa.teach.course.common.enums.ComponentTypeEnum;
//import cn.qbs.wa.teach.course.standard.constants.CourseConstants;
//import cn.qbs.wa.teach.course.standard.listener.event.StudentCourseStatEvent;
//import cn.qbs.wa.teach.course.standard.listener.event.StudentLearningDurationEvent;
//import cn.qbs.wa.teach.course.standard.pojo.coursecomponent.CourseComponentDetailResponse;
//import cn.qbs.wa.teach.course.standard.pojo.dto.LiveCourseStudyingDTO;
//import cn.qbs.wa.teach.course.standard.pojo.dto.MyCourseStudyingDTO;
//import cn.qbs.wa.teach.course.standard.service.*;
//import com.alibaba.fastjson.JSON;
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.ApplicationContext;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
///**
// * @author yjx
// */
//@Slf4j
//@Service
//public class CoursePersonalServiceImpl implements CoursePersonalService {
//
//    @Resource
//    private CourseStudentLessonService studentLessonService;
//
//    @Resource
//    private CourseStudentStudyRecordService studentStudyRecordService;
//    @Resource
//    private CourseStudentStudySnapshotService studentStudySnapshotService;
//    @Resource
//    private CourseComponentService courseComponentService;
//    @Resource
//    private ApplicationContext applicationContext;
//    @Resource
//    private CourseStudentLessonService courseStudentLessonService;
//    @Resource
//    private RemoteService remoteService;
//
//    @Override
//    public void recording(MyCourseStudyingDTO params) {
//        log.info("前端学习进度传参{}", JSON.toJSONString(params));
//        // 先计算该视频的播放进度
//        Long componentId = params.getComponentId();
//        CourseComponentDetailResponse component = courseComponentService.detail(componentId);
//        if (component == null) {
//            log.error("未找到对应的课程学习视频, courseId: {}, componentId: {}", params.getCourseId(), componentId);
//            return;
//        }
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
//        if (resourceFileDuration == null || resourceFileDuration <= 0
//                || playbackPosition > resourceFileDuration) {
//            log.error("课程学习视频数据不合法, courseId: {}, componentId: {}, resourceFileDuration: {}, playbackPosition: {}",
//                    params.getCourseId(), componentId, resourceFileDuration, playbackPosition);
//            return;
//        }
//        CourseStudentLesson studentCourseLesson = this.studentLessonService.getStudentCourseLesson(params.getUserId(), componentId);
//        if (studentCourseLesson == null) {
//            log.error("未找到学员课程章节信息, orgId: {}, userId: {}, componentId: {}", SecurityContextHolder.getOrgId(), params.getUserId(), componentId);
//            throw new ServiceException("未找到学员课程章节信息");
//        }
//        // 只要开始学习后就算 1% 的课程学习进度
//        int progress = CourseConstants.STUDY_PROGRESS_BEGIN;
//        boolean isCompeted = false;
//        Long learningDuration = Optional.ofNullable(studentCourseLesson.getLearningDuration()).orElse(0L) + params.getLearningDuration();
//        if (ComponentTypeEnum.ZB.getCode().equals(component.getComponentTypeCode())) {
//            // 观看时长 >= 总时长 = 完成
//            if (learningDuration >= resourceFileDuration) {
//                isCompeted = true;
//                progress = CourseConstants.STUDY_PROGRESS_COMPLETED;
//            } else {
//                int calc = NumberUtil.parseInt(NumberUtil.div(learningDuration, resourceFileDuration, 2).multiply(BigDecimal.valueOf(CourseConstants.STUDY_PROGRESS_COMPLETED)).toString());
//                progress = NumberUtil.max(calc, progress);
//            }
//        } else {
//            //2022.5.20 录播视频规则跟直播规则一样,不管进度条,播了多少就多少,重复也没有关系
//            // 偏差范围内算做已完成
//            if (resourceFileDuration - learningDuration <= CourseConstants.STUDY_PROGRESS_OFFSET) {
//                isCompeted = true;
//                progress = CourseConstants.STUDY_PROGRESS_COMPLETED;
//            } else {
//                int calc = NumberUtil.parseInt(NumberUtil.div(learningDuration, resourceFileDuration, 2).multiply(BigDecimal.valueOf(CourseConstants.STUDY_PROGRESS_COMPLETED)).toString());
//                progress = NumberUtil.max(calc, progress);
//            }
//
//        }
//
//        CourseStudentLesson update = new CourseStudentLesson();
//        update.setId(studentCourseLesson.getId());
//        update.setComponentId(componentId);
//        update.setPlaybackPosition(params.getPlaybackPosition());
//        update.setLearningDuration(learningDuration);
//        // 进度不能超过100
//        update.setPlaybackProgress(NumberUtil.min(CourseConstants.STUDY_PROGRESS_COMPLETED, progress));
//        if (isCompeted && 0 == studentCourseLesson.getLearningCompletion()) {
//            update.setLearningCompletion(1);
//            update.setLearningCompletionTime(LocalDateTime.now());
//        }
//        // 更新我的课程讲次学习情况
//        this.studentLessonService.updateById(update);
//
//        // 异步更新学员课程总体情况，任务相关的课程统计
//        this.applicationContext.publishEvent(new StudentCourseStatEvent(params));
//
//        // 记录学习记录
//        CourseStudentStudyRecord courseStudentStudyRecord = BeanUtil.copyProperties(params, CourseStudentStudyRecord.class);
//        courseStudentStudyRecord.setPlaybackProgress(progress);
//        this.studentStudyRecordService.save(courseStudentStudyRecord);
//
//        // 更新最近一次学习课程讲次
//        CourseStudentStudySnapshot studySnapshot = this.studentStudySnapshotService.getStudentCourse(params.getUserId(), params.getCourseId());
//        if (studySnapshot == null) {
//            studySnapshot = BeanUtil.copyProperties(params, CourseStudentStudySnapshot.class);
//        } else {
//            BeanUtil.copyProperties(params, studySnapshot, "id");
//            studySnapshot.setCreateTime(LocalDateTime.now());
//        }
//        this.studentStudySnapshotService.saveOrUpdate(studySnapshot);
//
//        if (params.getLearningDuration() > 0) {
//            // 异步更新学员学习时长
//            this.applicationContext.publishEvent(new StudentLearningDurationEvent(params));
//        }
//    }
//
//    @Override
//    public void studying(MyCourseStudyingDTO params) {
//
//    }
//
//    @Override
//    public List<MyCourseStudyingDTO> LiveRecording(List<LiveCourseStudyingDTO> params) {
//        //这里的是以一个回放视频生成后发过来的,所以componentId也必然一样.
//        //目前直播只在同个机构所以orgId必然一样.
//        //这里回来的都是已经统计好的时长不用再叠加
//        if (CollUtil.isNotEmpty(params)) {
//            Long orgId = params.get(0).getOrgId();
//            Long componentId = params.get(0).getComponentId();
//            SecurityContextHolder.setSelectOrgId(orgId.toString());
//            List<Long> userIdList = params.stream().map(LiveCourseStudyingDTO::getUserId).collect(Collectors.toList());
//            Map<Long, LiveCourseStudyingDTO> userMap = params.stream().collect(Collectors.toMap(LiveCourseStudyingDTO::getUserId, t -> t));
//            List<CourseStudentLesson> existCourseStudentLessonList = courseStudentLessonService.list(new LambdaQueryWrapper<CourseStudentLesson>().eq(CourseStudentLesson::getComponentId, componentId).in(CourseStudentLesson::getUserId, userIdList));
//            if (CollUtil.isNotEmpty(existCourseStudentLessonList)) {
//                List<CourseStudentLesson> updateStudentLessonList = getLiveRecordUpdateCourseStudentList(existCourseStudentLessonList, userMap);
//                if (CollUtil.isNotEmpty(updateStudentLessonList)) {
//                    //courseStudentLessonService.updateBatchById(updateStudentLessonList);
//                    //学习完进度再统计整体进度
//                    BeanUtil.copyToList(updateStudentLessonList, MyCourseStudyingDTO.class).forEach(this::recording);
//                }
//            }
//        }
//
//        return null;
//    }
//
//    private List<CourseStudentLesson> getLiveRecordUpdateCourseStudentList(List<CourseStudentLesson> existCourseStudentLessonList, Map<Long, LiveCourseStudyingDTO> userMap) {
//        for (CourseStudentLesson courseStudentLesson : existCourseStudentLessonList) {
//            if (userMap.containsKey(courseStudentLesson.getUserId())) {
//                LiveCourseStudyingDTO liveCourseStudyingDTO = userMap.get(courseStudentLesson.getUserId());
//                courseStudentLesson.setLearningDuration(liveCourseStudyingDTO.getLearningDuration());
//            }
//        }
//        return existCourseStudentLessonList.stream().filter(i -> i.getLearningDuration() != null).collect(Collectors.toList());
//    }
//}
