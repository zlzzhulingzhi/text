package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.course.common.entity.CourseStudentStudyExactRecord;
import cn.qbs.wa.teach.course.standard.pojo.dto.PlaybackCourseStudyingDTO;
import cn.qbs.wa.teach.course.standard.pojo.playback.PlaybackStatistic;
import cn.qbs.wa.teach.course.standard.pojo.playback.WatchPlaybackStudentPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.playback.WatchPlaybackStudentPageResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * 【学员课程学习精准记录】(CourseStudentStudyExactRecord)表服务接口
 *
 * @author zcm
 * @version 1.0
 * @date 2022-06-23 17:02:30
 */
public interface CourseStudentStudyExactRecordService extends IService<CourseStudentStudyExactRecord> {

    CourseStudentStudyExactRecord addOrUpdate(PlaybackCourseStudyingDTO params);

    /**
     * 统计回放观看数据
     * @param basicLiveId
     * @return
     */
    PlaybackStatistic getStatisticData(Long basicLiveId);

    /**
     * 分页查询观看回放学员
     * @param params
     * @return
     */
    IPage<WatchPlaybackStudentPageResponse> watchPlaybackStudentPage(WatchPlaybackStudentPageRequest params);

}

