package cn.qbs.wa.teach.exam.admin.service;

import cn.qbs.wa.teach.exam.admin.pojo.exam.ExamAddRequest;
import cn.qbs.wa.teach.exam.admin.pojo.exam.ExamUpdateRequest;
import cn.qbs.wa.teach.exam.common.entity.Exam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 考试表(Exam)表服务接口
 *
 * @author zcm
 * @since 2021-12-14 11:40:51
 */
public interface ComExamService extends IService<Exam> {


    void add(ExamAddRequest params);

    void update(ExamUpdateRequest params);

    /**
     * 发布考试
     * @param examId
     * @param manual
     */
    void release(Long examId, boolean manual);

    void start(Long examId, boolean manual);

    /**
     * 批量发布考试
     * @param examIdList
     * @param manual
     * @return
     */
    Boolean release(List<Long> examIdList, boolean manual);

    /**
     * 撤销发布考试
     * @param examId
     * @param manual
     */
    void revokeRelease(Long examId, boolean manual);

    /**
     * 批量撤销发布考试
     * @param examIdList
     * @param manual
     * @return
     */
    Boolean revokeRelease(List<Long> examIdList, boolean manual);


    void end(Long examId, boolean manual);

    boolean delete(Long examId);

    void autoCorrect(Long examId);

}

