package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.course.common.entity.CourseStatisticDaily;
import cn.qbs.wa.teach.course.standard.pojo.coursestatisticdaily.*;
import cn.qbs.wa.teach.course.standard.pojo.dto.MyCourseStudyingDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 【课程学习每日统计】(CourseStatisticDaily)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:39
 */
public interface CourseStatisticDailyService extends IService<CourseStatisticDaily> {

    /**
     * 新增【课程学习每日统计】
     * @param params
     * @return
     */
    boolean add(CourseStatisticDailyAddRequest params);

    /**
     * 分页查询【课程学习每日统计】
     * @param params
     * @return
     */
    IPage<CourseStatisticDailyPageResponse> page(CourseStatisticDailyPageRequest params);

    /**
     * 查询【课程学习每日统计】
     * @param params
     * @return
     */
    List<CourseStatisticDailyPageResponse> list(CourseStatisticDailyListRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    CourseStatisticDailyDetailResponse detail(Serializable id);

    /**
     * 更新【课程学习每日统计】
     * @param params
     * @return
     */
    boolean update(CourseStatisticDailyUpdateRequest params);

    /**
     * 删除【课程学习每日统计】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    void refreshStatistic(MyCourseStudyingDTO source);

    List<CourseStatisticDailyPageResponse> line(CourseStatisticDailyListRequest params);
}

