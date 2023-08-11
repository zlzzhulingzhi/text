package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.course.standard.pojo.dto.CourseInfoDTO;

public interface CommonService {
    /**
     * 查询课程可见权限
     * @param info 课程信息
     * @param userId 用户ID
     */
    void setCourseVisible(CourseInfoDTO info, Long userId);
}
