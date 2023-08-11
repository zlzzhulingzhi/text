package cn.qbs.wa.teach.course.standard.service.impl;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.course.standard.mapper.CourseUserVisibleMapper;
import cn.qbs.wa.teach.course.common.entity.CourseUserVisible;
import cn.qbs.wa.teach.course.standard.service.CourseUserVisibleService;
import cn.qbs.wa.teach.course.standard.pojo.courseuservisible.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 【课程可见用户】(CourseUserVisible)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:40
 */
@Slf4j
@Service("courseUserVisibleService")
public class CourseUserVisibleServiceImpl extends ServiceImpl<CourseUserVisibleMapper, CourseUserVisible> implements CourseUserVisibleService {

    @Override
    public boolean add(CourseUserVisibleAddRequest params) {
        CourseUserVisible courseUserVisible = new CourseUserVisible();
        BeanUtils.copyProperties(params, courseUserVisible);
        return this.save(courseUserVisible);
    }

    @Override
    public IPage<CourseUserVisiblePageResponse> page(CourseUserVisiblePageRequest params) {
        return this.baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public CourseUserVisibleDetailResponse detail(Serializable id) {
        return this.baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(CourseUserVisibleUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        CourseUserVisible courseUserVisible = new CourseUserVisible();
        BeanUtils.copyProperties(params, courseUserVisible);
        return this.updateById(courseUserVisible);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public List<CourseUserVisible> listByCourseId(Long courseId) {
        return this.baseMapper.listByCourseId(courseId);
    }

    @Override
    public void deleteByCourseId(Long courseId) {
        this.remove(Wrappers.<CourseUserVisible>lambdaQuery().eq(CourseUserVisible::getCourseId, courseId));
    }

    @Override
    public List<CourseUserVisible> getByCourseId(Long courseId) {
        return this.lambdaQuery().eq(CourseUserVisible::getCourseId, courseId).list();
    }
}

