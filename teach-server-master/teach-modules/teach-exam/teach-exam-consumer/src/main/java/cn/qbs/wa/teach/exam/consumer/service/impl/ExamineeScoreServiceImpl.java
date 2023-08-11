package cn.qbs.wa.teach.exam.consumer.service.impl;

import cn.qbs.wa.teach.exam.common.entity.ExamineeRecord;
import cn.qbs.wa.teach.exam.common.entity.ExamineeScore;
import cn.qbs.wa.teach.exam.consumer.mapper.ExamineeScoreMapper;
import cn.qbs.wa.teach.exam.consumer.service.ExamineeScoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 考生成绩(ExamineeScore)表服务实现类
 *
 * @author zcm
 * @version 1.0
 * @date 2022-05-24 16:29:31
 */
@Slf4j
@Service("examineeScoreService")
public class ExamineeScoreServiceImpl extends ServiceImpl<ExamineeScoreMapper, ExamineeScore> implements ExamineeScoreService {

    /**
     * 添加或更新成绩
     * @param examineeRecord
     */
    @Override
    public void addOrUpdateExamineeScore(ExamineeRecord examineeRecord) {
        if (examineeRecord != null) {
            Long examineeId = examineeRecord.getExamineeId();
            Long examineeRecordId = examineeRecord.getId();
            log.info("添加或更新成绩 {examineeId: {}, examineeRecordId: {}}", examineeId, examineeRecordId);
            ExamineeScore examineeScore = this.lambdaQuery().eq(ExamineeScore::getExamineeId, examineeId).last("limit 1").one();
            if (examineeScore == null) {
                log.info("添加成绩 {examineeId: {}, examineeRecordId: {}}", examineeId, examineeRecordId);
                examineeScore = new ExamineeScore();
                examineeScore.setOrgId(examineeRecord.getOrgId());
                examineeScore.setExamineeId(examineeId);
                examineeScore.setExamineeRecordId(examineeRecordId);
                examineeScore.setScore(examineeRecord.getScore());
                examineeScore.setCreateBy(0L);
                this.save(examineeScore);

            } else {
                log.info("更新成绩 {examineeId: {}, examineeRecordId: {}}", examineeId, examineeRecordId);
                examineeScore.setExamineeRecordId(examineeRecordId);
                examineeScore.setScore(examineeRecord.getScore());
                examineeScore.setUpdateBy(0L);
                this.updateById(examineeScore);
            }
        }
    }

}

