package cn.qbs.wa.teach.course.standard.service.impl;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.course.common.entity.CourseStudent;
import cn.qbs.wa.teach.course.common.entity.CourseUserVisible;
import cn.qbs.wa.teach.course.standard.mapper.CourseUserDeptVisibleMapper;
import cn.qbs.wa.teach.course.common.entity.CourseUserDeptVisible;
import cn.qbs.wa.teach.course.standard.service.CourseUserDeptVisibleService;
import cn.qbs.wa.teach.course.standard.pojo.courseuserdeptvisible.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
 * 【课程可见学员部门】(CourseUserDeptVisible)表服务实现类
 *
 * @author makejava
 * @since 2022-05-09 16:06:54
 */
@Slf4j
@Service("courseUserDeptVisibleService")
public class CourseUserDeptVisibleServiceImpl extends ServiceImpl<CourseUserDeptVisibleMapper, CourseUserDeptVisible> implements CourseUserDeptVisibleService {

    @Override
    public boolean add(CourseUserDeptVisibleAddRequest params) {
        CourseUserDeptVisible courseUserDeptVisible = new CourseUserDeptVisible();
        BeanUtils.copyProperties(params, courseUserDeptVisible);
        return this.save(courseUserDeptVisible);
    }

    @Override
    public IPage<CourseUserDeptVisiblePageResponse> page(CourseUserDeptVisiblePageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public CourseUserDeptVisibleDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(CourseUserDeptVisibleUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        CourseUserDeptVisible courseUserDeptVisible = new CourseUserDeptVisible();
        BeanUtils.copyProperties(params, courseUserDeptVisible);
        return this.updateById(courseUserDeptVisible);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public void deleteByCourseId(Long courseId) {
        this.remove(Wrappers.<CourseUserDeptVisible>lambdaQuery().eq(CourseUserDeptVisible::getCourseId, courseId));
    }

    @Override
    public List<CourseUserDeptVisible> getByCourseId(Long courseId) {
        return this.lambdaQuery().eq(CourseUserDeptVisible::getCourseId, courseId).list();
    }

    @Override
    public boolean checkCourseDept(Long courseId) {
        Long count = this.lambdaQuery().eq(CourseUserDeptVisible::getCourseId, courseId).count();
        return count != null && count > 0;
    }

    @Override
    public List<CourseUserDeptVisible> listByCourseId(Long courseId) {
        return this.baseMapper.listByCourseId(courseId);
    }
}

