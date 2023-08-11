package cn.qbs.wa.teach.exam.consumer.service.impl;

import cn.qbs.wa.teach.exam.common.entity.Examinee;
import cn.qbs.wa.teach.exam.common.entity.ExamineeRecord;
import cn.qbs.wa.teach.exam.consumer.mapper.ExamineeMapper;
import cn.qbs.wa.teach.exam.consumer.service.ExamineeRecordService;
import cn.qbs.wa.teach.exam.consumer.service.ExamineeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 考生表(Examinee)表服务实现类
 *
 * @author zcm
 * @since 2021-12-14 11:43:32
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExamineeServiceImpl extends ServiceImpl<ExamineeMapper, Examinee> implements ExamineeService {

    private final ExamineeRecordService examineeRecordService;


    /**
     * 更新考生考试剩余次数
     * @param examineeId
     * @param examLimitCount
     */
    @Override
    public void updateRemainingTimes(Long examineeId, Integer examLimitCount) {
        Long count = this.examineeRecordService.lambdaQuery().eq(ExamineeRecord::getExamineeId, examineeId).count();
        if (count == null) {
            count = 0L;
        }
        int remainingTimes = examLimitCount - count.intValue();
        this.lambdaUpdate().eq(Examinee::getId, examineeId).set(Examinee::getRemainingTimes, remainingTimes).update();
    }

}

