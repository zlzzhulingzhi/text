package cn.qbs.wa.teach.exam.consumer.service.impl;

import cn.qbs.wa.teach.exam.common.enumerate.CorrectStatusEnum;
import cn.qbs.wa.teach.exam.consumer.mapper.ExamineeRecordQuestionMapper;
import cn.qbs.wa.teach.exam.consumer.service.ExamineeRecordQuestionService;
import cn.qbs.wa.teach.exam.common.entity.ExamineeRecordQuestion;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 考生答题记录表(ExamineeRecordQuestion)表服务实现类
 *
 * @author zcm
 * @since 2021-12-14 11:43:33
 */
@Slf4j
@Service("examineeRecordQuestionService")
public class ExamineeRecordQuestionServiceImpl extends ServiceImpl<ExamineeRecordQuestionMapper, ExamineeRecordQuestion> implements ExamineeRecordQuestionService {

    @Override
    public Long selectUncorrectedQuestionCount(Long examineeRecordId) {
        return this.lambdaQuery().eq(ExamineeRecordQuestion::getExamineeRecordId, examineeRecordId)
                .eq(ExamineeRecordQuestion::getCorrectStatus, CorrectStatusEnum.UNCORRECTED.getStatus()).count();
    }

}

