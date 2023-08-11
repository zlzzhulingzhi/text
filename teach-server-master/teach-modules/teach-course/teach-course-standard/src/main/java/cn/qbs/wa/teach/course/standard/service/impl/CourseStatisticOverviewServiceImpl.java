package cn.qbs.wa.teach.course.standard.service.impl;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.course.common.entity.CourseComponent;
import cn.qbs.wa.teach.course.common.entity.CourseStatisticOverview;
import cn.qbs.wa.teach.course.standard.constants.CourseConstants;
import cn.qbs.wa.teach.course.standard.mapper.CourseComponentMapper;
import cn.qbs.wa.teach.course.standard.mapper.CourseStatisticOverviewMapper;
import cn.qbs.wa.teach.course.standard.pojo.coursestatisticoverview.*;
import cn.qbs.wa.teach.course.standard.pojo.dto.EvaluationInfoDTO;
import cn.qbs.wa.teach.course.standard.service.CourseStatisticOverviewService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 【课程统计信息】(CourseStatisticOverview)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:39
 */
@Slf4j
@Service("courseStatisticOverviewService")
public class CourseStatisticOverviewServiceImpl extends ServiceImpl<CourseStatisticOverviewMapper, CourseStatisticOverview> implements CourseStatisticOverviewService {

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private CourseComponentMapper courseComponentMapper;

    @Override
    public boolean add(CourseStatisticOverviewAddRequest params) {
        CourseStatisticOverview courseStatisticOverview = new CourseStatisticOverview();
        BeanUtils.copyProperties(params, courseStatisticOverview);
        return this.save(courseStatisticOverview);
    }

    @Override
    public IPage<CourseStatisticOverviewPageResponse> page(CourseStatisticOverviewPageRequest params) {
        return this.baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public CourseStatisticOverviewDetailResponse detail(Serializable id) {
        return this.baseMapper.selectDetailById(id);
    }

    @Override
    public CourseStatisticOverview getByCourseId(Long courseId) {
        return this.lambdaQuery().eq(CourseStatisticOverview::getCourseId, courseId).one();
    }

    @Override
    public boolean update(CourseStatisticOverviewUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        CourseStatisticOverview courseStatisticOverview = new CourseStatisticOverview();
        BeanUtils.copyProperties(params, courseStatisticOverview);
        return this.updateById(courseStatisticOverview);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean incrEvaluationInfo(EvaluationInfoDTO evaluationInfo) {
        boolean b = this.baseMapper.incrEvaluationInfo(evaluationInfo);
        if (b) {
            CourseStatisticOverview overview = this.getByCourseId(evaluationInfo.getCourseId());
            if (overview != null && overview.getEvaluatorNum() > 0) {
                overview.setScore(overview.getTotalScore().divide(BigDecimal.valueOf(overview.getEvaluatorNum()), 2, RoundingMode.DOWN));
                this.updateById(overview);
            }
        }
        return b;
    }

    @Override
    public int incrBuyerNum(Long courseId, int num) {
        return this.baseMapper.incrBuyerNum(courseId, num);
    }

    @Override
    public int incrSignUpNum(Long courseId, int num) {
        return this.baseMapper.incrSignUpNum(courseId, num);
    }

    @Override
    public int incrStudyCompletedNum(Long courseId, int num) {
        return this.baseMapper.incrStudyCompletedNum(courseId, num);
    }

    @Override
    public int incrStudyTotalNum(Long courseId, int num) {
        return this.baseMapper.incrStudyTotalNum(courseId, num);
    }

    @Override
    public int incrLessonNum(Long courseId, int num) {
        return this.baseMapper.incrLessonNum(courseId, num);
    }

    @Override
    public int incrViews(Long courseId, int num) {
        return this.baseMapper.incrViews(courseId, num);
    }

    @Override
    public void refreshLessonNum(Long courseId) {
        Long count = courseComponentMapper.selectCount(Wrappers.<CourseComponent>lambdaQuery().eq(CourseComponent::getCourseId, courseId));
        if (count != null && count >= 0) {
            CourseStatisticOverview overview = new CourseStatisticOverview();
            overview.setLessonNum(Math.toIntExact(count));
            this.baseMapper.update(overview, Wrappers.<CourseStatisticOverview>lambdaUpdate().eq(CourseStatisticOverview::getCourseId, courseId));
        }
    }

    @Override
    public void refreshStatistic(Long courseId) {
        RLock lock = redissonClient.getLock(CourseConstants.COURSE_STATISTIC_OVERVIEW_LOCK_PREFIX + courseId);
        try {
            lock.tryLock(5L, 5L, TimeUnit.SECONDS);
            CourseStatisticOverview totalCourseView = baseMapper.totalCourseView(courseId);
            if (totalCourseView != null) {
                totalCourseView.setBuyerNum(totalCourseView.getSignUpNum());
                this.baseMapper.refreshStatistic(courseId, totalCourseView);
            }
        } catch (InterruptedException e) {
            log.error("刷新课程汇总信息锁中断", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}

