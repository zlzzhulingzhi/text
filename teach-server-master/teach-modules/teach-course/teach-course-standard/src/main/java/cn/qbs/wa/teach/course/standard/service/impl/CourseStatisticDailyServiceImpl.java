package cn.qbs.wa.teach.course.standard.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.course.standard.constants.CourseConstants;
import cn.qbs.wa.teach.course.common.entity.CourseStatisticDaily;
import cn.qbs.wa.teach.course.standard.mapper.CourseStatisticDailyMapper;
import cn.qbs.wa.teach.course.standard.pojo.coursestatisticdaily.*;
import cn.qbs.wa.teach.course.standard.pojo.dto.MyCourseStudyingDTO;
import cn.qbs.wa.teach.course.standard.service.CourseStatisticDailyService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 【课程学习每日统计】(CourseStatisticDaily)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:39
 */
@Slf4j
@Service("courseStatisticDailyService")
public class CourseStatisticDailyServiceImpl extends ServiceImpl<CourseStatisticDailyMapper, CourseStatisticDaily> implements CourseStatisticDailyService {

    @Resource
    private RedissonClient redissonClient;

    @Override
    public boolean add(CourseStatisticDailyAddRequest params) {
        CourseStatisticDaily statisticDaily = this.lambdaQuery().eq(CourseStatisticDaily::getCourseId, params.getCourseId()).eq(CourseStatisticDaily::getStatisticDate, params.getStatisticDate()).one();
        if (statisticDaily != null) {
            throw new ServiceException(params.getStatisticDate() + "课程学习统计已存在。");
        }
        CourseStatisticDaily courseStatisticDaily = new CourseStatisticDaily();
        BeanUtils.copyProperties(params, courseStatisticDaily);
        return this.save(courseStatisticDaily);
    }

    @Override
    public IPage<CourseStatisticDailyPageResponse> page(CourseStatisticDailyPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public List<CourseStatisticDailyPageResponse> list(CourseStatisticDailyListRequest params) {
        return baseMapper.list(params);
    }

    @Override
    public List<CourseStatisticDailyPageResponse> line(CourseStatisticDailyListRequest params) {
        int days = Math.toIntExact(LocalDateTimeUtil.between(params.getStartStatisticDate().atStartOfDay(), params.getEndStatisticDate().atStartOfDay(), ChronoUnit.DAYS));
        Map<LocalDate, CourseStatisticDailyPageResponse> map = new HashMap<>(days);
        List<CourseStatisticDailyPageResponse> list = baseMapper.list(params);
        if (!list.isEmpty()) {
            map = list.stream().collect(Collectors.toMap(CourseStatisticDaily::getStatisticDate, o -> o));
        }
        List<CourseStatisticDailyPageResponse> line = new ArrayList<>(days);
        Long courseId = params.getCourseId();
        for (int i = 0; i < days; i++) {
            LocalDate date = params.getStartStatisticDate().plusDays(i);
            CourseStatisticDailyPageResponse dailyResponse = Optional.ofNullable(map.get(date)).orElseGet(() -> {
                CourseStatisticDailyPageResponse response = new CourseStatisticDailyPageResponse();
                response.setCourseId(courseId);
                response.setStatisticDate(date);
                response.setLearningDuration(0L);
                response.setLearningNum(0);
                return response;
            });
            line.add(dailyResponse);
        }
        return line;
    }

    @Override
    public CourseStatisticDailyDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(CourseStatisticDailyUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        CourseStatisticDaily courseStatisticDaily = new CourseStatisticDaily();
        BeanUtils.copyProperties(params, courseStatisticDaily);
        return this.updateById(courseStatisticDaily);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public void refreshStatistic(MyCourseStudyingDTO source) {
        Long courseId = source.getCourseId();
        if (courseId == null) {
            log.error("课程ID为空. {}", source);
            return;
        }
        // 学习时长为0，说明是刚刚进入学习页面所触发，不进行记录
        if (0L == Optional.ofNullable(source.getLearningDuration()).orElse(0L)) {
            log.error("课程学习时长为0,不进行统计. {}", source);
            return;
        }
        // redission 锁课程，避免并发
        RLock lock = redissonClient.getLock(CourseConstants.COURSE_STATISTIC_DAILY_LOCK_PREFIX + courseId);
        lock.lock(10L, TimeUnit.SECONDS);
        try {
            // 查询课程当天学习人数，累加学习时长
            LocalDate today = LocalDate.now();
            LocalDateTime begin = LocalDateTime.of(today, LocalTime.MIN);
            LocalDateTime end = LocalDateTime.of(today, LocalTime.MAX);
            int studentNum = this.baseMapper.countStudentNumByCourseId(courseId, begin, end);
            if (0 == studentNum) {
                studentNum = 1;
            }
            CourseStatisticDaily daily = this.lambdaQuery()
                    .eq(CourseStatisticDaily::getCourseId, courseId)
                    .eq(CourseStatisticDaily::getStatisticDate, today)
                    .oneOpt().orElseGet(() -> {
                        CourseStatisticDaily statisticDaily = new CourseStatisticDaily();
                        statisticDaily.setCourseId(courseId);
                        statisticDaily.setStatisticDate(today);
                        statisticDaily.setLearningNum(0);
                        statisticDaily.setLearningDuration(0L);
                        return statisticDaily;
                    });
            if (studentNum != daily.getLearningNum()) {
                daily.setLearningNum(studentNum);
            } else {
                // 人数没有变化，该字段不进行更新
                daily.setLearningNum(null);
            }
            daily.setLearningDuration(source.getLearningDuration() + Optional.ofNullable(daily.getLearningDuration()).orElse(0L));
            this.saveOrUpdate(daily);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}

