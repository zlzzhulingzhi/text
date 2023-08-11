package cn.qbs.wa.teach.exam.consumer.service;

import cn.qbs.wa.teach.exam.common.entity.ExamineeRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 考生考试记录表(ExamineeRecord)表服务接口
 *
 * @author zcm
 * @since 2021-12-14 11:43:32
 */
public interface ExamineeRecordService extends IService<ExamineeRecord> {

    /**
     * 查询考生考试记录，将最高分记录更新为纳入计算，其它记录更新为不纳入计算
     * @param examineeId 考生ID
     */
    void updateCalculated(Long examineeId);

    Long selectCountByExamineeId(Long examineeId);
}

