package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.course.common.entity.CourseUserDeptVisible;
import cn.qbs.wa.teach.course.common.entity.CourseUserGroupVisible;
import cn.qbs.wa.teach.course.standard.pojo.courseusergroupvisible.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 【课程可见学员分组】(CourseUserGroupVisible)表服务接口
 *
 * @author makejava
 * @since 2022-05-09 16:07:59
 */
public interface CourseUserGroupVisibleService extends IService<CourseUserGroupVisible> {

    /**
     * 新增【课程可见学员分组】
     * @param params
     * @return
     */
    boolean add(CourseUserGroupVisibleAddRequest params);

    /**
     * 分页查询【课程可见学员分组】
     * @param params
     * @return
     */
    IPage<CourseUserGroupVisiblePageResponse> page(CourseUserGroupVisiblePageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    CourseUserGroupVisibleDetailResponse detail(Serializable id);

    /**
     * 更新【课程可见学员分组】
     * @param params
     * @return
     */
    boolean update(CourseUserGroupVisibleUpdateRequest params);

    /**
     * 删除【课程可见学员分组】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    void deleteByCourseId(Long courseId);

    List<CourseUserGroupVisible> getByCourseId(Long courseId);

    boolean checkCourseGroup(Long courseId);

    List<CourseUserGroupVisible> listByCourseId(Long id);
}

