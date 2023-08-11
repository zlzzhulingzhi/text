package cn.qbs.wa.teach.course.standard.mapper;

import cn.qbs.wa.teach.course.common.entity.CourseStudentStudySnapshot;
import cn.qbs.wa.teach.course.standard.pojo.dto.StudySnapshotDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 【学员课程学习快照】(CourseStudentStudySnapshot)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-12-24 15:39:26
 */
public interface CourseStudentStudySnapshotMapper extends BaseMapper<CourseStudentStudySnapshot> {

    List<StudySnapshotDTO> getStudyLesson(@Param("userId") Long userId, @Param("courseIdList") List<Long> courseIdList);
}

