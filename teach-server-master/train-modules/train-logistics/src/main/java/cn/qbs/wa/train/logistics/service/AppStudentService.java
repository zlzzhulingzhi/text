package cn.qbs.wa.train.logistics.service;

import cn.qbs.wa.train.logistics.pojo.student.app.AppSocialBindingRequest;

/**
 * 学员(Student)表服务接口
 *
 * @author makejava
 * @since 2022-02-28 10:40:52
 */
public interface AppStudentService {

    Boolean bindingSocial(AppSocialBindingRequest request);

}

