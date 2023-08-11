package cn.qbs.wa.teach.course.standard.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.course.common.entity.Course;
import cn.qbs.wa.teach.course.common.entity.CourseLecturer;
import cn.qbs.wa.teach.course.standard.mapper.CourseLiveLinkMapper;
import cn.qbs.wa.teach.course.common.entity.CourseLiveLink;
import cn.qbs.wa.teach.course.standard.service.CourseLecturerService;
import cn.qbs.wa.teach.course.standard.service.CourseLiveLinkService;
import cn.qbs.wa.teach.course.standard.pojo.courselivelink.*;
import cn.qbs.wa.teach.course.standard.service.CourseService;
import cn.qbs.wa.teach.course.standard.service.RemoteService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 【课程讲义】(CourseLiveLink)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2021-12-29 14:43:50
 */
@Slf4j
@Service("courseLiveLinkService")
public class CourseLiveLinkServiceImpl extends ServiceImpl<CourseLiveLinkMapper, CourseLiveLink> implements CourseLiveLinkService {

    @Resource
    private CourseService courseService;

    @Resource
    private RemoteService remoteService;

    @Resource
    private CourseLecturerService courseLecturerService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean add(CourseLiveLinkAddRequest params) {
        CourseLiveLink courseLiveLink = this.getLiveLinkByCourseId(params.getCourseId());
        if (courseLiveLink != null) {
            courseLiveLink.setLiveId(params.getLiveId());
        } else {
            courseLiveLink = new CourseLiveLink();
            BeanUtils.copyProperties(params, courseLiveLink);
        }
        boolean exec = this.saveOrUpdate(courseLiveLink);
        if (exec) {
            Course course = courseService.getById(params.getCourseId());
            String courseName = "";
            List<Long> lecturerIds = new ArrayList<>();
            if (course != null) {
                courseName = course.getCourseName();
                List<CourseLecturer> lecturers = courseLecturerService.listByCourseId(params.getCourseId());
                if (!lecturers.isEmpty()) {
                    lecturerIds = lecturers.stream().map(CourseLecturer::getLecturerId).collect(Collectors.toList());
                }
            }
            //String msg = remoteService.remoteAssociateLive(params.getCourseId(), 1, courseName, params.getLiveId(), lecturerIds);
            //if (msg != null) {
            //    throw new ServiceException(msg);
            //}
        }
        return exec;
    }

    @Override
    public IPage<CourseLiveLinkPageResponse> page(CourseLiveLinkPageRequest params) {
        return this.baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public CourseLiveLinkDetailResponse detail(Serializable id) {
        return this.baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(CourseLiveLinkUpdateRequest params) {
        CourseLiveLink courseLiveLink = new CourseLiveLink();
        BeanUtils.copyProperties(params, courseLiveLink);
        return this.updateById(courseLiveLink);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public CourseLiveLink getLiveLinkByCourseId(Long courseId) {
        return this.lambdaQuery().eq(CourseLiveLink::getCourseId, courseId).one();
    }

    @Override
    public CourseLiveLinkDetailResponse detailByCourseId(Long courseId) {
        return this.baseMapper.detailByCourseId(courseId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteByCourseIds(List<Long> courseIds) {
        List<CourseLiveLink> list = this.lambdaQuery().in(CourseLiveLink::getCourseId, courseIds).list();
        if (CollUtil.isEmpty(list)) {
            return false;
        }
        this.removeByIds(list.stream().map(CourseLiveLink::getId).collect(Collectors.toList()));
        //for (CourseLiveLink liveLink : list) {
        //    remoteService.remoteAssociateLive(liveLink.getCourseId(), 1);
        //}
        return true;
    }
}

