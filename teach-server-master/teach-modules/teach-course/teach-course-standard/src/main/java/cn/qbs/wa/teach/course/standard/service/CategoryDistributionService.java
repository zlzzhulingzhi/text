package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.course.common.entity.CategoryDistribution;
import cn.qbs.wa.teach.course.standard.pojo.categorydistribution.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 【课程分类分布】(CategoryDistribution)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:36
 */
public interface CategoryDistributionService extends IService<CategoryDistribution> {

    /**
     * 新增【课程分类分布】
     * @param params
     * @return
     */
    boolean add(CategoryDistributionAddRequest params);

    /**
     * 分页查询【课程分类分布】
     * @param params
     * @return
     */
    IPage<CategoryDistributionPageResponse> page(CategoryDistributionPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    CategoryDistributionDetailResponse detail(Serializable id);

    /**
     * 更新【课程分类分布】
     * @param params
     * @return
     */
    boolean update(CategoryDistributionUpdateRequest params);

    /**
     * 删除【课程分类分布】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    /**
     * 课程分类下的课程数加一
     * @param categoryId 课程分类ID
     * @param count 数量
     * @return 影响行数
     */
    int incrCourseCount(Long categoryId, int count);

    /**
     * 课程分类下的课程数减一
     * @param categoryId 课程分类ID
     * @param count 数量
     * @return 影响行数
     */
    int decrCourseCount(Long categoryId, int count);
}

