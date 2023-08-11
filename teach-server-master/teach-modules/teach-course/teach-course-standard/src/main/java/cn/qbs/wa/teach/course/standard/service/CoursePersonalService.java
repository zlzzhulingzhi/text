package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.course.standard.pojo.dto.LiveCourseStudyingDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.MyCourseStudyingDTO;

import java.util.List;

/**
 * @author yjx
 */
public interface CoursePersonalService {

    /**
     * 课程学习情况收集
     */
    void recording(MyCourseStudyingDTO params);

    /**
     * 课程学习情况收集
     */
    void studying(MyCourseStudyingDTO params);

    /**
     * 直播课程学习情况收集
     */
    List<MyCourseStudyingDTO> LiveRecording(List<LiveCourseStudyingDTO> params);
}
