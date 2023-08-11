package cn.qbs.wa.teach.exam.admin.service;

import cn.qbs.wa.teach.exam.admin.pojo.invigilation.TerminateExamRequest;

/**
 * 监考 Service 接口
 * @Author zcm
 * @Date 2022/1/25 10:10
 * @Version 1.0
 */
public interface InvigilationService {

    /**
     * 终止考试
     * @param params
     * @return
     */
    boolean terminateExam(TerminateExamRequest params);

}
