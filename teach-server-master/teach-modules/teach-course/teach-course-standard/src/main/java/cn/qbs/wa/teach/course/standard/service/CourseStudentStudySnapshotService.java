package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.course.common.entity.CourseStudentStudySnapshot;
import cn.qbs.wa.teach.course.standard.pojo.dto.StudySnapshotDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 【学员课程学习快照】(CourseStudentStudySnapshot)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2021-12-24 15:39:26
 */
public interface CourseStudentStudySnapshotService extends IService<CourseStudentStudySnapshot> {

    /**
     * 获取学员课程最近一次学习快照
     * @param userId 学员ID
     * @param courseId 课次ID
     * @return 学习快照
     */
    CourseStudentStudySnapshot getStudentCourse(Long userId, Long courseId);

    List<StudySnapshotDTO> getStudyLesson(Long userId, List<Long> courseIdList);
}

