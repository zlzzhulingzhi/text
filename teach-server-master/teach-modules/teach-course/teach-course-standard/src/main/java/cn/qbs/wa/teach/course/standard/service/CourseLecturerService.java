package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.course.common.entity.CourseLecturer;
import cn.qbs.wa.teach.course.standard.pojo.courselecturer.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 【课程讲师】(CourseLecturer)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:38
 */
public interface CourseLecturerService extends IService<CourseLecturer> {

    /**
     * 新增【课程讲师】
     * @param params
     * @return
     */
    boolean add(CourseLecturerAddRequest params);

    /**
     * 分页查询【课程讲师】
     * @param params
     * @return
     */
    IPage<CourseLecturerPageResponse> page(CourseLecturerPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    CourseLecturerDetailResponse detail(Serializable id);

    /**
     * 更新【课程讲师】
     * @param params
     * @return
     */
    boolean update(CourseLecturerUpdateRequest params);

    /**
     * 删除【课程讲师】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    /**
     * 根据课程ID 获取讲师列表
     * @param courseId 课程ID
     * @return 讲师列表
     */
    List<CourseLecturer> listByCourseId(Long courseId);

    /**
     * 根据课程名称 获取讲师列表
     * @param courseName 课程名称
     * @return 讲师列表
     */
    List<CourseLecturer> listByCourseName(String courseName);
}

