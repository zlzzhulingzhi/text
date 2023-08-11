package cn.qbs.wa.teach.course.standard.service.impl;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.course.common.entity.CourseStudent;
import cn.qbs.wa.teach.course.common.entity.CourseUserDeptVisible;
import cn.qbs.wa.teach.course.standard.mapper.CourseUserGroupVisibleMapper;
import cn.qbs.wa.teach.course.common.entity.CourseUserGroupVisible;
import cn.qbs.wa.teach.course.standard.service.CourseUserGroupVisibleService;
import cn.qbs.wa.teach.course.standard.pojo.courseusergroupvisible.*;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 【课程可见学员分组】(CourseUserGroupVisible)表服务实现类
 *
 * @author makejava
 * @since 2022-05-09 16:07:59
 */
@Slf4j
@Service("courseUserGroupVisibleService")
public class CourseUserGroupVisibleServiceImpl extends ServiceImpl<CourseUserGroupVisibleMapper, CourseUserGroupVisible> implements CourseUserGroupVisibleService {

    @Override
    public boolean add(CourseUserGroupVisibleAddRequest params) {
        CourseUserGroupVisible courseUserGroupVisible = new CourseUserGroupVisible();
        BeanUtils.copyProperties(params, courseUserGroupVisible);
        return this.save(courseUserGroupVisible);
    }

    @Override
    public IPage<CourseUserGroupVisiblePageResponse> page(CourseUserGroupVisiblePageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public CourseUserGroupVisibleDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(CourseUserGroupVisibleUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        CourseUserGroupVisible courseUserGroupVisible = new CourseUserGroupVisible();
        BeanUtils.copyProperties(params, courseUserGroupVisible);
        return this.updateById(courseUserGroupVisible);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public void deleteByCourseId(Long courseId) {
        remove(Wrappers.<CourseUserGroupVisible>lambdaQuery().eq(CourseUserGroupVisible::getCourseId, courseId));
    }

    @Override
    public List<CourseUserGroupVisible> getByCourseId(Long courseId) {
        return this.lambdaQuery().eq(CourseUserGroupVisible::getCourseId, courseId).list();
    }

    @Override
    public boolean checkCourseGroup(Long courseId) {
        Long count = this.lambdaQuery().eq(CourseUserGroupVisible::getCourseId, courseId).count();
        return count != null && count > 0;
    }

    @Override
    public List<CourseUserGroupVisible> listByCourseId(Long courseId) {
        return this.baseMapper.listByCourseId(courseId);
    }
}

