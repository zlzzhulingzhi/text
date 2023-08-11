package cn.qbs.wa.teach.course.standard.service.impl;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.course.standard.mapper.CourseCategoryMapper;
import cn.qbs.wa.teach.course.common.entity.CourseCategory;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseCategoryDTO;
import cn.qbs.wa.teach.course.standard.service.CourseCategoryService;
import cn.qbs.wa.teach.course.standard.pojo.coursecategory.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 【课程分类关联关系】(CourseCategory)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */
@Slf4j
@Service("courseCategoryService")
public class CourseCategoryServiceImpl extends ServiceImpl<CourseCategoryMapper, CourseCategory> implements CourseCategoryService {

    @Override
    public boolean add(CourseCategoryAddRequest params) {
        CourseCategory courseCategory = new CourseCategory();
        BeanUtils.copyProperties(params, courseCategory);
        return this.save(courseCategory);
    }

    @Override
    public IPage<CourseCategoryPageResponse> page(CourseCategoryPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public CourseCategoryDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(CourseCategoryUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        CourseCategory courseCategory = new CourseCategory();
        BeanUtils.copyProperties(params, courseCategory);
        return this.updateById(courseCategory);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public List<CourseCategoryDTO> listByCourseId(Long courseId) {
        return baseMapper.listByCourseId(courseId);
    }

    @Override
    public boolean hasCourse(Long id) {
        return this.count(Wrappers.<CourseCategory>lambdaQuery().eq(CourseCategory::getCategoryId, id)) > 0;
    }
}

