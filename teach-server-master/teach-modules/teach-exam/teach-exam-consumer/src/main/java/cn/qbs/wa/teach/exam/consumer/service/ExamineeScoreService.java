package cn.qbs.wa.teach.exam.consumer.service;

import cn.qbs.wa.teach.exam.common.entity.ExamineeRecord;
import cn.qbs.wa.teach.exam.common.entity.ExamineeScore;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 考生成绩(ExamineeScore)表服务接口
 *
 * @author zcm
 * @version 1.0
 * @date 2022-05-24 16:29:31
 */
public interface ExamineeScoreService extends IService<ExamineeScore> {

    /**
     * 添加或更新成绩
     * @param examineeRecord
     */
    void addOrUpdateExamineeScore(ExamineeRecord examineeRecord);

}

