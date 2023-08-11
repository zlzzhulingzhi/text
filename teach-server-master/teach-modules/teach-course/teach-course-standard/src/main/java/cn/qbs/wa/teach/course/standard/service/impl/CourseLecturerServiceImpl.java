package cn.qbs.wa.teach.course.standard.service.impl;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.course.standard.mapper.CourseLecturerMapper;
import cn.qbs.wa.teach.course.common.entity.CourseLecturer;
import cn.qbs.wa.teach.course.standard.service.CourseLecturerService;
import cn.qbs.wa.teach.course.standard.pojo.courselecturer.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 【课程讲师】(CourseLecturer)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:38
 */
@Slf4j
@Service("courseLecturerService")
public class CourseLecturerServiceImpl extends ServiceImpl<CourseLecturerMapper, CourseLecturer> implements CourseLecturerService {

    @Override
    public boolean add(CourseLecturerAddRequest params) {
        CourseLecturer courseLecturer = new CourseLecturer();
        BeanUtils.copyProperties(params, courseLecturer);
        return this.save(courseLecturer);
    }

    @Override
    public IPage<CourseLecturerPageResponse> page(CourseLecturerPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public CourseLecturerDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(CourseLecturerUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        CourseLecturer courseLecturer = new CourseLecturer();
        BeanUtils.copyProperties(params, courseLecturer);
        return this.updateById(courseLecturer);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public List<CourseLecturer> listByCourseId(Long courseId) {
        return baseMapper.listByCourseId(courseId);
    }

    @Override
    public List<CourseLecturer> listByCourseName(String courseName) {
        return baseMapper.listByCourseName(courseName);
    }
}

