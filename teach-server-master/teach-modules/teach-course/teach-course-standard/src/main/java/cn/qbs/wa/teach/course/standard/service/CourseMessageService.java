package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.course.common.entity.CourseMessage;
import cn.qbs.wa.teach.course.standard.pojo.coursemessage.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 【课程留言】(CourseMessage)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:39
 */
public interface CourseMessageService extends IService<CourseMessage> {

    /**
     * 新增【课程留言】
     * @param params
     * @return
     */
    boolean add(CourseMessageAddRequest params);

    /**
     * 分页查询【课程留言】
     * @param params
     * @return
     */
    IPage<CourseMessagePageResponse> page(CourseMessagePageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    CourseMessageDetailResponse detail(Serializable id);

    /**
     * 更新【课程留言】
     * @param params
     * @return
     */
    boolean update(CourseMessageUpdateRequest params);

    /**
     * 删除【课程留言】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

}

