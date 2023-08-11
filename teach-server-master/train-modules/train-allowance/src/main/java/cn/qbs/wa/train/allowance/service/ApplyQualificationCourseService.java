package cn.qbs.wa.train.allowance.service;

import cn.qbs.wa.train.allowance.entity.ApplyQualificationCourse;
import cn.qbs.wa.train.allowance.pojo.apply.QualificationCourseRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 资助资格申请-项目计划-课程(ApplyQualificationCourse)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2022-11-02 11:20:28
 */
public interface ApplyQualificationCourseService extends IService<ApplyQualificationCourse> {

    void updateByProjectId(Long projectId, List<QualificationCourseRequest> courseList);

    void addByProjectId(Long projectId, List<QualificationCourseRequest> requestList);
}

