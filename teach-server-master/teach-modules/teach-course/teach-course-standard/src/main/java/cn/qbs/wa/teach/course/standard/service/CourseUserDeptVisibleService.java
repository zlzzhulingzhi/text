package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.course.common.entity.CourseUserDeptVisible;
import cn.qbs.wa.teach.course.common.entity.CourseUserVisible;
import cn.qbs.wa.teach.course.standard.pojo.courseuserdeptvisible.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 【课程可见学员部门】(CourseUserDeptVisible)表服务接口
 *
 * @author makejava
 * @since 2022-05-09 16:06:54
 */
public interface CourseUserDeptVisibleService extends IService<CourseUserDeptVisible> {

    /**
     * 新增【课程可见学员部门】
     * @param params
     * @return
     */
    boolean add(CourseUserDeptVisibleAddRequest params);

    /**
     * 分页查询【课程可见学员部门】
     * @param params
     * @return
     */
    IPage<CourseUserDeptVisiblePageResponse> page(CourseUserDeptVisiblePageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    CourseUserDeptVisibleDetailResponse detail(Serializable id);

    /**
     * 更新【课程可见学员部门】
     * @param params
     * @return
     */
    boolean update(CourseUserDeptVisibleUpdateRequest params);

    /**
     * 删除【课程可见学员部门】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    void deleteByCourseId(Long courseId);

    List<CourseUserDeptVisible> getByCourseId(Long courseId);

    boolean checkCourseDept(Long courseId);

    List<CourseUserDeptVisible> listByCourseId(Long id);
}

