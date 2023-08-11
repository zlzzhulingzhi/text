package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.course.common.entity.CourseLiveLink;
import cn.qbs.wa.teach.course.standard.pojo.courselivelink.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 【课程讲义】(CourseLiveLink)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2021-12-29 14:43:50
 */
public interface CourseLiveLinkService extends IService<CourseLiveLink> {

    /**
     * 新增【课程讲义】
     * @param params
     * @return
     */
    boolean add(CourseLiveLinkAddRequest params);

    /**
     * 分页查询【课程讲义】
     * @param params
     * @return
     */
    IPage<CourseLiveLinkPageResponse> page(CourseLiveLinkPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    CourseLiveLinkDetailResponse detail(Serializable id);

    /**
     * 更新【课程讲义】
     * @param params
     * @return
     */
    boolean update(CourseLiveLinkUpdateRequest params);

    /**
     * 删除【课程讲义】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    CourseLiveLink getLiveLinkByCourseId(Long courseId);

    CourseLiveLinkDetailResponse detailByCourseId(Long courseId);

    boolean deleteByCourseIds(List<Long> courseId);
}

