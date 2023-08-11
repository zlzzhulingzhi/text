package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.course.common.entity.CourseStatisticOverview;
import cn.qbs.wa.teach.course.standard.pojo.coursestatisticoverview.*;
import cn.qbs.wa.teach.course.standard.pojo.dto.EvaluationInfoDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 【课程统计信息】(CourseStatisticOverview)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:39
 */
public interface CourseStatisticOverviewService extends IService<CourseStatisticOverview> {

    /**
     * 新增【课程统计信息】
     * @param params
     * @return
     */
    boolean add(CourseStatisticOverviewAddRequest params);

    /**
     * 分页查询【课程统计信息】
     * @param params
     * @return
     */
    IPage<CourseStatisticOverviewPageResponse> page(CourseStatisticOverviewPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    CourseStatisticOverviewDetailResponse detail(Serializable id);

    CourseStatisticOverview getByCourseId(Long courseId);

    /**
     * 更新【课程统计信息】
     * @param params
     * @return
     */
    boolean update(CourseStatisticOverviewUpdateRequest params);

    /**
     * 删除【课程统计信息】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    /**
     * 增加评分信息
     * @param evaluationInfoDTO 评分信息
     * @return 操作结果
     */
    boolean incrEvaluationInfo(EvaluationInfoDTO evaluationInfoDTO);

    /**
     * 增加购买人数
     *
     * @param courseId 课程ID
     * @param num      数量
     * @return 操作结果
     */
    int incrBuyerNum(Long courseId, int num);

    /**
     * 增加报名人数
     *
     * @param courseId 课程ID
     * @param num      数量
     * @return 操作结果
     */
    int incrSignUpNum(Long courseId, int num);

    /**
     * 增加学已完成人数
     *
     * @param courseId 课程ID
     * @param num      数量
     * @return 操作结果
     */
    int incrStudyCompletedNum(Long courseId, int num);

    /**
     * 增加学习人数
     *
     * @param courseId 课程ID
     * @param num      数量
     * @return 操作结果
     */
    int incrStudyTotalNum(Long courseId, int num);

    /**
     * 增加课程课时数
     *
     * @param courseId 课程ID
     * @param num      数量
     * @return 操作结果
     */
    int incrLessonNum(Long courseId, int num);

    /**
     * 增加课程访问数
     *
     * @param courseId 课程ID
     * @param num      数量
     * @return 操作结果
     */
    int incrViews(Long courseId, int num);

    void refreshStatistic(Long courseId);

    void refreshLessonNum(Long courseId);
}

