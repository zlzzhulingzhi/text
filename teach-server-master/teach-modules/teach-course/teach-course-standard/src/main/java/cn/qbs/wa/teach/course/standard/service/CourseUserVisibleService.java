package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.course.common.entity.CourseUserVisible;
import cn.qbs.wa.teach.course.standard.pojo.courseuservisible.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 【课程可见用户】(CourseUserVisible)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:40
 */
public interface CourseUserVisibleService extends IService<CourseUserVisible> {

    /**
     * 新增【课程可见用户】
     * @param params
     * @return
     */
    boolean add(CourseUserVisibleAddRequest params);

    /**
     * 分页查询【课程可见用户】
     * @param params
     * @return
     */
    IPage<CourseUserVisiblePageResponse> page(CourseUserVisiblePageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    CourseUserVisibleDetailResponse detail(Serializable id);

    /**
     * 更新【课程可见用户】
     * @param params
     * @return
     */
    boolean update(CourseUserVisibleUpdateRequest params);

    /**
     * 删除【课程可见用户】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    /**
     * 根据课程ID 获取 可见用户列表
     * @param courseId 课程ID
     * @return 可见用户列表
     */
    List<CourseUserVisible> listByCourseId(Long courseId);

    void deleteByCourseId(Long courseId);

    List<CourseUserVisible> getByCourseId(Long courseId);
}

