package cn.qbs.wa.teach.course.standard.listener;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.course.common.entity.Course;
import cn.qbs.wa.teach.course.common.entity.CourseStudent;
import cn.qbs.wa.teach.course.common.entity.CourseStudentLesson;
import cn.qbs.wa.teach.course.standard.constants.CourseConstants;
import cn.qbs.wa.teach.course.standard.listener.event.StudentCourseStatEvent;
import cn.qbs.wa.teach.course.standard.listener.event.StudentLearningDurationEvent;
import cn.qbs.wa.teach.course.standard.listener.event.StudentLessonChangeEvent;
import cn.qbs.wa.teach.course.standard.mapper.CourseMapper;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseLessonChangeDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.MyCourseStudyingDTO;
import cn.qbs.wa.teach.course.standard.service.CourseStatisticDailyService;
import cn.qbs.wa.teach.course.standard.service.CourseStatisticOverviewService;
import cn.qbs.wa.teach.course.standard.service.CourseStudentLessonService;
import cn.qbs.wa.teach.course.standard.service.CourseStudentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

/**
 * @author yjx
 */
@Slf4j
@Component
@AllArgsConstructor
public class StudentCourseListener {

    private final CourseStudentService courseStudentService;

    private final CourseMapper courseMapper;

    private final CourseStudentLessonService studentLessonService;

    private final CourseStatisticOverviewService courseStatisticOverviewService;

    private final CourseStatisticDailyService courseStatisticDailyService;

    @Async
    @Order
    @EventListener(StudentCourseStatEvent.class)
    public void listenStudentCourseStat(StudentCourseStatEvent event) {
        MyCourseStudyingDTO source = (MyCourseStudyingDTO) event.getSource();
        SecurityContextHolder.setSelectOrgId(String.valueOf(source.getOrgId()));
        SecurityContextHolder.setUserId(String.valueOf(source.getUserId()));

        // 1.统计一次学员课程相关的 最后一次学习时间、开始学课程节数、已学完课程节数、学习课程总节数、课程学习时长、学习完成率、学习完成状态
        Long courseId = source.getCourseId();
        CourseStudent studentCourse = this.courseStudentService.getStudentCourse(source.getUserId(), courseId);
        if (studentCourse == null) {
            log.error("学员课程不存在, userId: {}, courseId: {}; 当前登录用户信息, orgId: {}, userId: {}",
                    source.getUserId(), courseId, SecurityContextHolder.getOrgId(), SecurityContextHolder.getUserId());
            SecurityContextHolder.remove();
            return;
        }
        CourseStudent updateCourse = new CourseStudent();
        updateCourse.setId(studentCourse.getId());
        // 最后一次学习时间
        updateCourse.setLastStudyTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(event.getTimestamp()), ZoneId.systemDefault()));

        // 开始学课程节数、已学完课程节数、学习课程总节数、课程学习时长、学习完成率、学习完成状态
        List<CourseStudentLesson> lessons = this.studentLessonService.getUsefulCourseAllLesson(source.getUserId(), courseId);
        if (!lessons.isEmpty()) {
            // 开始学课程节数
            AtomicInteger learningBeginCount = new AtomicInteger();
            // 已学完课程节数
            AtomicInteger learningFinishCount = new AtomicInteger();
            // 课程学习时长
            LongAdder learningDuration = new LongAdder();
            // 学习完成率
            AtomicInteger learningProgress = new AtomicInteger();
            lessons.forEach(lesson -> {
                if (lesson.getPlaybackProgress() > 0 && lesson.getLearningCompletion() == 0) {
                    learningBeginCount.getAndIncrement();
                }
                if (lesson.getLearningCompletion() == 1) {
                    learningFinishCount.getAndIncrement();
                }
                learningDuration.add(lesson.getLearningDuration());
                learningProgress.getAndAdd(lesson.getPlaybackProgress());
            });
            int beginCount = learningBeginCount.get();
            int finishCount = learningFinishCount.get();
            updateCourse.setLearningCount(beginCount);
            updateCourse.setLearningFinishCount(finishCount);
            updateCourse.setLearningTotalCount(beginCount + finishCount);
            updateCourse.setLearningDuration(learningDuration.longValue());
            if (finishCount == lessons.size()) {
                // 学习完成状态 已完成
                updateCourse.setLearningCompletion(1);
                updateCourse.setLearningCompletionTime(LocalDateTime.now());
                updateCourse.setLearningRate(CourseConstants.STUDY_PROGRESS_COMPLETED);
            } else {
                int learningRate = learningProgress.get() / lessons.size();
                updateCourse.setLearningRate(Math.max(CourseConstants.STUDY_PROGRESS_BEGIN, Math.min(learningRate, CourseConstants.STUDY_PROGRESS_COMPLETED)));
            }
        }
        this.courseStudentService.updateById(updateCourse);

        // 统计课程信息
        try {
            this.courseStatisticOverviewService.refreshStatistic(courseId);
        } catch (Exception e) {
            log.error("课程概况统计 SQL 出错", e);
        }
        // 2.通知任务系统，课程学习进度
        if (updateCourse.getLearningDuration() != null) {
            updateCourse.setCourseId(courseId);
            updateCourse.setStudentId(studentCourse.getStudentId());
            Course course = courseMapper.selectById(courseId);
            String courseName = course != null ? course.getCourseName() : null;
            sendTaskMsg(source.getOrgId(), courseName, updateCourse);
        }
        SecurityContextHolder.remove();
    }

    @Async
    @Order
    @EventListener(StudentLearningDurationEvent.class)
    public void listenStudentLearningDuration(StudentLearningDurationEvent event) {
        MyCourseStudyingDTO source = (MyCourseStudyingDTO) event.getSource();
        // 统计学员学时时长，用以排行，统计相关
        // 课程每日学习人数，学习时长的统计
        SecurityContextHolder.setSelectOrgId(String.valueOf(source.getOrgId()));
        SecurityContextHolder.setUserId(String.valueOf(source.getUserId()));
        this.courseStatisticDailyService.refreshStatistic(source);
        SecurityContextHolder.remove();
    }

    @Async
    @Order
    @EventListener(StudentLessonChangeEvent.class)
    public void listenCourseLessonChange(StudentLessonChangeEvent event) {
        CourseLessonChangeDTO source = (CourseLessonChangeDTO) event.getSource();
        SecurityContextHolder.setSelectOrgId(String.valueOf(source.getOrgId()));
        SecurityContextHolder.setUserId(String.valueOf(source.getUserId()));
        List<CourseStudent> students = courseStudentService.lambdaQuery().eq(CourseStudent::getCourseId, source.getCourseId()).list();
        if (students.isEmpty()) {
            return;
        }
        if (CourseConstants.COURSE_LESSON_ADD.equals(source.getAction())) {
            for (CourseStudent student : students) {
                CourseStudentLesson courseStudentLesson = new CourseStudentLesson();
                BeanUtil.copyProperties(source, courseStudentLesson);
                courseStudentLesson.setUserId(student.getUserId());
                try {
                    studentLessonService.save(courseStudentLesson);
                } catch (Exception e) {
                    log.error("新增学员课程所需要学习的课次信息出错", e);
                }
            }
        } else if (CourseConstants.COURSE_LESSON_REMOVE.equals(source.getAction())) {
            Long componentId = source.getComponentId();
            for (CourseStudent student : students) {
                try {
                    studentLessonService.deleteByComponentIdAndUser(componentId, student.getUserId());
                } catch (Exception e) {
                    log.error("删除学员课程所需要学习的课次信息出错", e);
                }
            }
        }
        refreshStudentCourseProgress(students);
        SecurityContextHolder.remove();
    }

    private void refreshStudentCourseProgress(List<CourseStudent> students) {
        if (CollUtil.isNotEmpty(students)) {
            List<CourseStudent> updateList = students.stream().map(studentCourse -> {
                CourseStudent updateCourse = new CourseStudent();
                updateCourse.setId(studentCourse.getId());
                updateCourse.setCourseId(studentCourse.getCourseId());
                updateCourse.setStudentId(studentCourse.getStudentId());
                try {
                    List<CourseStudentLesson> lessons = this.studentLessonService.getUsefulCourseAllLesson(studentCourse.getUserId(), studentCourse.getCourseId());
                    if (!lessons.isEmpty()) {
                        // 学习完成率
                        int learningProgress = lessons.stream().filter(e -> e.getPlaybackProgress() != null).mapToInt(CourseStudentLesson::getPlaybackProgress).sum();
                        int learningRate = learningProgress / lessons.size();
                        updateCourse.setLearningRate(Math.max(CourseConstants.STUDY_PROGRESS_BEGIN, Math.min(learningRate, CourseConstants.STUDY_PROGRESS_COMPLETED)));
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
                return updateCourse;
            }).filter(e -> e.getLearningRate() != null).collect(Collectors.toList());
            if (!updateList.isEmpty()) {
                courseStudentService.updateBatchById(updateList);
                CourseStudent t = students.get(0);
                Long courseId = t.getCourseId();
                Long orgId = t.getOrgId();
                Course course = courseMapper.selectById(courseId);
                String courseName = course != null ? course.getCourseName() : null;
                for (CourseStudent courseStudent : updateList) {
                    sendTaskMsg(orgId, courseName, courseStudent);
                }
            }
        }

    }

    private void sendTaskMsg(Long orgId, String courseName, CourseStudent courseStudent) {
        //MqTaskerProcessUpdateDTO taskObj = new MqTaskerProcessUpdateDTO();
        //taskObj.setOrgId(orgId);
        //taskObj.setBusinessType(1);
        //taskObj.setBusinessId(courseStudent.getCourseId());
        //taskObj.setStudentId(courseStudent.getStudentId());
        //taskObj.setBusinessName(courseName);
        //BigDecimal rateProgress = BigDecimal.valueOf(courseStudent.getLearningRate()).divide(BigDecimal.valueOf(CourseConstants.STUDY_PROGRESS_COMPLETED), 2, RoundingMode.DOWN);
        //taskObj.setRateProgress(rateProgress);
        //tdmqProducerTemplate.sendAsyncStringMsg(Topics.LEARNED_PROGRESS_UPDATE, JSON.toJSONString(taskObj));
    }

}
