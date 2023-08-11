package cn.qbs.wa.teach.course.standard.service.impl;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.course.standard.mapper.CategoryDistributionMapper;
import cn.qbs.wa.teach.course.common.entity.CategoryDistribution;
import cn.qbs.wa.teach.course.standard.service.CategoryDistributionService;
import cn.qbs.wa.teach.course.standard.pojo.categorydistribution.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 【课程分类分布】(CategoryDistribution)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:36
 */
@Slf4j
@Service("categoryDistributionService")
public class CategoryDistributionServiceImpl extends ServiceImpl<CategoryDistributionMapper, CategoryDistribution> implements CategoryDistributionService {

    @Override
    public boolean add(CategoryDistributionAddRequest params) {
        CategoryDistribution categoryDistribution = new CategoryDistribution();
        BeanUtils.copyProperties(params, categoryDistribution);
        return this.save(categoryDistribution);
    }

    @Override
    public IPage<CategoryDistributionPageResponse> page(CategoryDistributionPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public CategoryDistributionDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(CategoryDistributionUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        CategoryDistribution categoryDistribution = new CategoryDistribution();
        BeanUtils.copyProperties(params, categoryDistribution);
        return this.updateById(categoryDistribution);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public int incrCourseCount(Long categoryId, int count) {
        if (count <= 0) {
            return 0;
        }
        return this.baseMapper.incrCourseCount(categoryId, count);
    }

    @Override
    public int decrCourseCount(Long categoryId, int count) {
        if (count <= 0) {
            return 0;
        }
        return this.baseMapper.decrCourseCount(categoryId, count);
    }
}

