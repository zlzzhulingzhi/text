package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.course.common.entity.CourseCategory;
import cn.qbs.wa.teach.course.standard.pojo.coursecategory.*;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseCategoryDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 【课程分类关联关系】(CourseCategory)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */
public interface CourseCategoryService extends IService<CourseCategory> {

    /**
     * 新增【课程分类关联关系】
     * @param params
     * @return
     */
    boolean add(CourseCategoryAddRequest params);

    /**
     * 分页查询【课程分类关联关系】
     * @param params
     * @return
     */
    IPage<CourseCategoryPageResponse> page(CourseCategoryPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    CourseCategoryDetailResponse detail(Serializable id);

    /**
     * 更新【课程分类关联关系】
     * @param params
     * @return
     */
    boolean update(CourseCategoryUpdateRequest params);

    /**
     * 删除【课程分类关联关系】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    /**
     * 根据课程ID 获取课程分类列表
     * @param courseId 课程ID
     * @return 分类列表
     */
    List<CourseCategoryDTO> listByCourseId(Long courseId);

    /**
     * 分类下是否存在关联课程
     * @param id 分类ID
     * @return true：存在、false：不存
     */
    boolean hasCourse(Long id);
}

