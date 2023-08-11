package cn.qbs.wa.teach.course.standard.mapper;

import cn.qbs.wa.teach.course.common.entity.CourseStudentStudyExactRecord;
import cn.qbs.wa.teach.course.standard.pojo.playback.PlaybackStatistic;
import cn.qbs.wa.teach.course.standard.pojo.playback.WatchPlaybackStudentPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.playback.WatchPlaybackStudentPageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * 【学员课程学习精准记录】(CourseStudentStudyExactRecord)表数据库访问层
 *
 * @author zcm
 * @version 1.0
 * @date 2022-06-23 17:02:30
 */
public interface CourseStudentStudyExactRecordMapper extends BaseMapper<CourseStudentStudyExactRecord> {

    /**
     * 统计回放观看数据
     * @param basicLiveId
     * @return
     */
    PlaybackStatistic selectStatistic(@Param("basicLiveId") Long basicLiveId);

    /**
     * 分页查询观看回放学员
     * @param page
     * @param params
     * @return
     */
    IPage<WatchPlaybackStudentPageResponse> page(@Param("page") Page<Object> page, @Param("params") WatchPlaybackStudentPageRequest params);

}

