package cn.qbs.wa.teach.course.standard.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.course.standard.mapper.CourseCommentMapper;
import cn.qbs.wa.teach.course.common.entity.CourseComment;
import cn.qbs.wa.teach.course.standard.pojo.dto.EvaluationInfoDTO;
import cn.qbs.wa.teach.course.standard.service.CourseCommentService;
import cn.qbs.wa.teach.course.standard.pojo.coursecomment.*;
import cn.qbs.wa.teach.course.standard.service.CourseStatisticOverviewService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 【课程评价】(CourseComment)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */
@Slf4j
@Service("courseCommentService")
public class CourseCommentServiceImpl extends ServiceImpl<CourseCommentMapper, CourseComment> implements CourseCommentService {

    @Resource
    private CourseStatisticOverviewService courseStatisticOverviewService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean add(CourseCommentAddRequest params) {
        CourseComment courseComment = new CourseComment();
        BeanUtils.copyProperties(params, courseComment);
        boolean save = this.save(courseComment);
        if (save) {
            // 课程评分人数 +1 评分 +n
            courseStatisticOverviewService.incrEvaluationInfo(new EvaluationInfoDTO(params.getCourseId(), 1, params.getScore()));
        }
        return save;
    }

    @Override
    public IPage<CourseCommentPageResponse> page(CourseCommentPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public CourseCommentDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(CourseCommentUpdateRequest params) {
        CourseComment courseComment = new CourseComment();
        BeanUtils.copyProperties(params, courseComment);
        return this.updateById(courseComment);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteByIds(List<Long> idList) {
        List<CourseComment> commentList = this.listByIds(idList);
        if (commentList.isEmpty()) {
            return false;
        }
        Long courseId = commentList.get(0).getCourseId();
        boolean remove = this.removeByIds(idList);
        if (remove) {
            BigDecimal totalScore = commentList.stream().map(CourseComment::getScore).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            // 课程评分人数 -n 评分 -totalScore
            courseStatisticOverviewService.incrEvaluationInfo(new EvaluationInfoDTO(courseId, -commentList.size(), totalScore.negate()));
        }
        return remove;
    }

    @Override
    public boolean deleteLogicByIds(List<Long> idList) {
        if (CollUtil.isEmpty(idList)) {
            return false;
        }
        List<CourseComment> comments = idList.stream().map(id -> {
            CourseComment courseComment = new CourseComment();
            courseComment.setId(id);
            courseComment.setDeleted(1);
            return courseComment;
        }).collect(Collectors.toList());
        return this.updateBatchById(comments);
    }
}

