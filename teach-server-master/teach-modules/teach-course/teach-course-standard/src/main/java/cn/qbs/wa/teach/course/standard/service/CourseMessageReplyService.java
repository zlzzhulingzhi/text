package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.course.common.entity.CourseMessageReply;
import cn.qbs.wa.teach.course.standard.pojo.coursemessagereply.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 【课程留言回复记录】(CourseMessageReply)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:39
 */
public interface CourseMessageReplyService extends IService<CourseMessageReply> {

    /**
     * 新增【课程留言回复记录】
     * @param params
     * @return
     */
    boolean add(CourseMessageReplyAddRequest params);

    /**
     * 分页查询【课程留言回复记录】
     * @param params
     * @return
     */
    IPage<CourseMessageReplyPageResponse> page(CourseMessageReplyPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    CourseMessageReplyDetailResponse detail(Serializable id);

    /**
     * 更新【课程留言回复记录】
     * @param params
     * @return
     */
    boolean update(CourseMessageReplyUpdateRequest params);

    /**
     * 删除【课程留言回复记录】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

}

