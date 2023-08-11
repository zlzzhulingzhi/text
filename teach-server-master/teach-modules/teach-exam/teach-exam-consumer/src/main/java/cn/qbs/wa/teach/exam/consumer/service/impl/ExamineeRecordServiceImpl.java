package cn.qbs.wa.teach.exam.consumer.service.impl;

import cn.qbs.wa.teach.exam.common.entity.ExamineeRecord;
import cn.qbs.wa.teach.exam.common.enumerate.ExamineeRecordStatusEnum;
import cn.qbs.wa.teach.exam.consumer.mapper.ExamineeRecordMapper;
import cn.qbs.wa.teach.exam.consumer.service.ExamineeRecordService;
import cn.qbs.wa.teach.exam.consumer.service.ExamineeScoreService;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

/**
 * 考生考试记录表(ExamineeRecord)表服务实现类
 *
 * @author zcm
 * @since 2021-12-14 11:43:32
 */
@Slf4j
@Service("examineeRecordService")
public class ExamineeRecordServiceImpl extends ServiceImpl<ExamineeRecordMapper, ExamineeRecord> implements ExamineeRecordService {

    @Resource
    private ExamineeScoreService examineeScoreService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCalculated(Long examineeId) {
        log.info("updateCalculated {examineeId: {}}", examineeId);
        if (examineeId != null) {
            List<ExamineeRecord> examineeRecordList = this.lambdaQuery()
                    .eq(ExamineeRecord::getExamineeId, examineeId)
                    .ge(ExamineeRecord::getStatus, ExamineeRecordStatusEnum.CORRECTED.getStatus())
                    .list();
            if (CollectionUtils.isNotEmpty(examineeRecordList)) {
                if (examineeRecordList.size() == 1) {
                    ExamineeRecord examineeRecord = examineeRecordList.get(0);
                    examineeRecord.setCalculated(true);
                    this.updateById(examineeRecord);
                    // 添加或更新成绩
                    examineeScoreService.addOrUpdateExamineeScore(examineeRecord);
                    return;
                }

                BigDecimal maxScore = examineeRecordList.stream().map(ExamineeRecord::getScore).max(BigDecimal::compareTo).get();
                Iterator<ExamineeRecord> iterator = examineeRecordList.iterator();
                while (iterator.hasNext()) {
                    ExamineeRecord examineeRecord = iterator.next();
                    if (maxScore.compareTo(examineeRecord.getScore()) == 0) {
                        examineeRecord.setCalculated(true);
                        this.updateById(examineeRecord);
                        // 添加或更新成绩
                        examineeScoreService.addOrUpdateExamineeScore(examineeRecord);
                        iterator.remove();
                        break;
                    }
                }

                examineeRecordList.forEach(i -> i.setCalculated(false));
                this.updateBatchById(examineeRecordList);
            }
        }
    }

    @Override
    public Long selectCountByExamineeId(Long examineeId) {
        Long count = this.lambdaQuery().eq(ExamineeRecord::getExamineeId, examineeId)
                .notIn(ExamineeRecord::getStatus,ExamineeRecordStatusEnum.UNFINISH.getStatus()).count();
        return count;
    }
}

