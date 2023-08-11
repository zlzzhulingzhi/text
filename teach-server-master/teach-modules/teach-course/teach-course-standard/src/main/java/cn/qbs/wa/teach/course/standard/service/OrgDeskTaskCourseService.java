package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.common.core.domain.IdListAndUserIdRequest;
import cn.qbs.wa.teach.course.common.entity.Course;
import cn.qbs.wa.teach.course.standard.pojo.dto.orgdesk.OrgDeskTaskCourseInfoDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 【课程】(Course)表机构前台h5服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:36
 */
public interface OrgDeskTaskCourseService extends IService<Course> {

    /**
     * 获取课程信息
     * @param courseIds
     * @return 课程信息
     */
    List<OrgDeskTaskCourseInfoDTO> getCourseList(IdListAndUserIdRequest courseIds);
}

