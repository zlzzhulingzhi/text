package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.course.common.entity.WCourse;
import cn.qbs.wa.teach.course.standard.pojo.wcourse.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 插件-课程表(WCourse)表服务接口
 *
 * @author makejava
 * @since 2022-03-01 14:25:16
 */
public interface WCourseService extends IService<WCourse> {

    /**
     * 新增插件-课程表
     * @param params
     * @return
     *//*
    boolean add(WCourseAddRequest params);
*/
    /**
     * 分页查询插件-课程表
     * @param params
     * @return
     */
    IPage<WCoursePageResponse> page(WCoursePageRequest params);

    IPage<WCoursePageResponse> pageV2(WCoursePageRequest params);

   /* IPage<WCoursePageResponse> search(WCoursePageRequest params);

    *//**
     * 获取详细信息
     * @param id
     * @return
     *//*
    WCourseDetailResponse detail(Serializable id);

    *//**
     * 更新插件-课程表
     * @param params
     * @return
     */
    boolean update(WCourseUpdateRequest params);

    /**
     * 删除插件-课程表
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    /*IPage<WCoursePageByChildResponse> pageByChild(WCoursePageRequest params);*/

}

