package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.course.common.entity.CourseStudentStudyRecord;
import cn.qbs.wa.teach.course.standard.pojo.coursestudentstudyrecord.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 【课程学习记录】(CourseStudentStudyRecord)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:40
 */
public interface CourseStudentStudyRecordService extends IService<CourseStudentStudyRecord> {

    /**
     * 新增【课程学习记录】
     * @param params
     * @return
     */
    boolean add(CourseStudentStudyRecordAddRequest params);

    /**
     * 分页查询【课程学习记录】
     * @param params
     * @return
     */
    IPage<CourseStudentStudyRecordPageResponse> page(CourseStudentStudyRecordPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    CourseStudentStudyRecordDetailResponse detail(Serializable id);

    /**
     * 更新【课程学习记录】
     * @param params
     * @return
     */
    boolean update(CourseStudentStudyRecordUpdateRequest params);

    /**
     * 删除【课程学习记录】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

}

