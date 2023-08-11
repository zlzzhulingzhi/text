package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.course.standard.entity.TCourse;
import cn.qbs.wa.teach.course.standard.pojo.tcourse.TCoursePageRequest;
import cn.qbs.wa.teach.course.standard.pojo.tcourse.TCoursePageResponse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 课程-报名分享(TCourse)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-24 19:09:01
 */
public interface TCourseService extends IService<TCourse> {

    List<TCoursePageResponse> listCourse(TCoursePageRequest params);
}

