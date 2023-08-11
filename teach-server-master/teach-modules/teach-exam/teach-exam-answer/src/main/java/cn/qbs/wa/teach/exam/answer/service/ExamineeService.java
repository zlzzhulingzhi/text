package cn.qbs.wa.teach.exam.answer.service;

import cn.qbs.wa.teach.exam.answer.pojo.exam.ExamPageRequest;
import cn.qbs.wa.teach.exam.answer.pojo.exam.ExamPageResponse;
import cn.qbs.wa.teach.exam.answer.pojo.exam.ExamRecord;
import cn.qbs.wa.teach.exam.common.entity.Examinee;
import cn.qbs.wa.teach.exam.common.entity.ExamineeRecord;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 考生表(Examinee)表服务接口
 *
 * @author zcm
 * @since 2021-12-14 11:43:32
 */
public interface ExamineeService extends IService<Examinee> {

    Examinee getExaminee(Long examId, Long userId);

    IPage<ExamPageResponse> myTestPage(ExamPageRequest examPageRequest);

    ExamRecord viewLog(ExamPageRequest examPageRequest);

    ExamineeRecord cheat(ExamPageRequest examPageRequest);

    void updateRemainingTimes(Long examineeId, Integer examLimitCount);

    void decrRemainingTimes(Long examineeId);
}

