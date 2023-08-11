package cn.qbs.wa.teach.course.standard.service.impl;

import cn.qbs.wa.teach.course.common.entity.CourseStudentStudySnapshot;
import cn.qbs.wa.teach.course.standard.mapper.CourseStudentStudySnapshotMapper;
import cn.qbs.wa.teach.course.standard.pojo.dto.StudySnapshotDTO;
import cn.qbs.wa.teach.course.standard.service.CourseStudentStudySnapshotService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 【学员课程学习快照】(CourseStudentStudySnapshot)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2021-12-24 15:39:26
 */
@Slf4j
@Service("courseStudentStudySnapshotService")
public class CourseStudentStudySnapshotServiceImpl extends ServiceImpl<CourseStudentStudySnapshotMapper, CourseStudentStudySnapshot> implements CourseStudentStudySnapshotService {

    @Override
    public CourseStudentStudySnapshot getStudentCourse(Long userId, Long courseId) {
        return this.lambdaQuery().eq(CourseStudentStudySnapshot::getUserId, userId).eq(CourseStudentStudySnapshot::getCourseId, courseId).one();
    }

    @Override
    public List<StudySnapshotDTO> getStudyLesson(Long userId, List<Long> courseIdList) {
        return this.baseMapper.getStudyLesson(userId, courseIdList);
    }
}

