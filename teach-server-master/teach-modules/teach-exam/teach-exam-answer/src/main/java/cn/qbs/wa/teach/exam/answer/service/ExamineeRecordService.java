package cn.qbs.wa.teach.exam.answer.service;

import cn.qbs.wa.teach.exam.answer.pojo.ExamResult;
import cn.qbs.wa.teach.exam.answer.pojo.exam.SaveAttachment;
import cn.qbs.wa.teach.exam.common.entity.ExamineeRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 考生考试记录表(ExamineeRecord)表服务接口
 *
 * @author zcm
 * @since 2021-12-14 11:43:32
 */
public interface ExamineeRecordService extends IService<ExamineeRecord> {

    ExamineeRecord getLastExamineeRecord(Long examineeId);

    /**
     * 考试结果
     * @param examineeRecordId
     * @return
     */
    ExamResult examResult(Long examineeRecordId);

    /**
     * 保存附件
     * @param saveAttachment
     * @return
     */
    boolean saveAttachment(SaveAttachment saveAttachment);

}

