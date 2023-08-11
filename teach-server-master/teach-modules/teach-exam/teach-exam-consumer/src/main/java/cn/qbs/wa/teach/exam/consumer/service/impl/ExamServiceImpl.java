package cn.qbs.wa.teach.exam.consumer.service.impl;

import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.redis.service.RedisService;
import cn.qbs.wa.teach.exam.common.entity.Exam;
import cn.qbs.wa.teach.exam.common.entity.Examinee;
import cn.qbs.wa.teach.exam.common.entity.ExamineeRecord;
import cn.qbs.wa.teach.exam.common.entity.ExamineeRecordQuestion;
import cn.qbs.wa.teach.exam.common.enumerate.ExamQuestionStatusEnum;
import cn.qbs.wa.teach.exam.common.enumerate.ExamineeRecordStatusEnum;
import cn.qbs.wa.teach.exam.common.enumerate.RuleEnum;
import cn.qbs.wa.teach.exam.common.pojo.ExamAndCertDetailResponse;
import cn.qbs.wa.teach.exam.common.util.RedisKeyUtil;
import cn.qbs.wa.teach.exam.common.util.RedisLockKeyUtil;
import cn.qbs.wa.teach.exam.consumer.mapper.ExamMapper;
import cn.qbs.wa.teach.exam.consumer.service.*;
import cn.qbs.wa.teach.question.api.pojo.DTO.paper.PaperDetailDTO;
import cn.qbs.wa.teach.question.api.pojo.DTO.question.QuestionDetailsDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 考试表(Exam)表服务实现类
 *
 * @author zcm
 * @since 2021-12-14 11:40:51
 */
@Slf4j
@Service("examService")
@RequiredArgsConstructor
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam> implements ExamService {

    private final PaperService paperService;

    private final ExamineeService examineeService;

    private final ExamineeRecordService examineeRecordService;

    private final ExamineeRecordQuestionService examineeRecordQuestionService;

    private final RedisService redisService;

    private final ExamMapper examMapper;

    private final RedissonClient redissonClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addExamQuestions(Long examId, Long examineeId, Long orgId, Long userId) {
        log.info("添加考题，参数:{examId: {}, examineeId: {}, orgId: {}, userId: {}}", examId, examineeId, orgId, userId);
        String lockKey = RedisLockKeyUtil.getAddExamQuestionsLockKey(examId, userId);
        RLock rLock = redissonClient.getFairLock(lockKey);
        try {
            rLock.lock(10, TimeUnit.SECONDS);
            Exam exam = this.getById(examId);
            if (exam == null) {
                log.error("查不到考题，参数:{examId: {}}", examId);
                throw new ServiceException("考试不存在或已删除！");
            }

            Examinee examinee = this.examineeService.getById(examineeId);
            if (examinee == null) {
                throw new ServiceException(String.format("查不到考生[%s]！", examineeId));
            }

            // 查询未完成的考试记录
            Long count = this.examineeRecordService.lambdaQuery().eq(ExamineeRecord::getExamineeId, examinee.getId())
                    .eq(ExamineeRecord::getStatus, ExamineeRecordStatusEnum.UNFINISH.getStatus()).count();
            if (count != null && count > 0) {
                log.error("上次考试未完成, {examId: {}, examineeId: {}, orgId: {}, userId: {}}", examId, examineeId, orgId, userId);
                return;
            }

            Integer examLimitCount = exam.getLimitCount();
            if (examLimitCount != null && examLimitCount > 0) {
                Integer remainingTimes = examinee.getRemainingTimes();
                if (remainingTimes != null && remainingTimes == 0) {
                    log.error("考试次数已经用完, {examId: {}, examineeId: {}, orgId: {}, userId: {}, examLimitCount: {}, remainingTimes: {}}",
                            examId, examineeId, orgId, userId, examLimitCount, remainingTimes);
                    return;
                }
            }

            PaperDetailDTO paperDetail = paperService.getPaperDetail(exam.getPaperId(), orgId);
            if (paperDetail == null) {
                throw new ServiceException("查询试卷详情失败！");
            }

            // 查询是否需要打乱试题顺序
            String ruleCode = RuleEnum.SHUFFLE_QUESTION.getCode();
            Boolean shuffleQuestion = examMapper.selectHasRule(examId, ruleCode);
            Map<Long, Integer> questionSortMap = new HashMap<>();
            // 获取考题
            List<ExamineeRecordQuestion> examineeRecordQuestionList = paperDetail.getQuestionTypeList().stream()
                    .flatMap(qt -> {
                        List<QuestionDetailsDTO> list = qt.getQuestionList();
                        List<Long> questionIdList = list.stream().map(q -> q.getId()).collect(Collectors.toList());
                        Stream<ExamineeRecordQuestion> stream = list.stream().map(q -> {
                            ExamineeRecordQuestion examineeRecordQuestion = new ExamineeRecordQuestion();
                            examineeRecordQuestion.setQuestionId(q.getId());
                            examineeRecordQuestion.setQuestionScore(q.getScore());
                            return examineeRecordQuestion;
                        });

                        if (shuffleQuestion != null && shuffleQuestion) {
                            Collections.shuffle(questionIdList);
                            for (int i = 0; i < questionIdList.size(); i++) {
                                questionSortMap.put(questionIdList.get(i), i + 1);
                            }
                        }

                        return stream;
                    }).collect(Collectors.toList());

            if (shuffleQuestion != null && shuffleQuestion) {
                log.info("打乱试题顺序:");
                log.info("questionSortMap: {}", questionSortMap);
                List<Long> questionIdList = examineeRecordQuestionList.stream().map(q -> q.getQuestionId()).collect(Collectors.toList());
                examineeRecordQuestionList.forEach(q -> q.setSortNum(questionSortMap.get(q.getQuestionId())));
                log.info("questionIdList: {}", questionIdList);
            }

            addExamineeRecordAndQuestions(examinee, examineeRecordQuestionList, exam.getLimitCount());
            // 更新添加试题缓存标记结果
            String addExamQuestionsRetKey = RedisKeyUtil.getAddExamQuestionsRetKey(examId, userId);
            redisService.setCacheObject(addExamQuestionsRetKey, ExamQuestionStatusEnum.ADDED.getStatus(), 7L, TimeUnit.DAYS);
        } finally {
            if (rLock != null && rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }
    }

    /**
     * 添加考试记录和试题
     *  @param examinee
     * @param examineeRecordQuestionList
     * @param examLimitCount
     */
    private void addExamineeRecordAndQuestions(Examinee examinee, List<ExamineeRecordQuestion> examineeRecordQuestionList, Integer examLimitCount) {
        ExamineeRecord examineeRecord = new ExamineeRecord();
        Long examineeId = examinee.getId();
        examineeRecord.setExamineeId(examineeId);
        examineeRecord.setOrgId(examinee.getOrgId());
        examineeRecord.setStatus(ExamineeRecordStatusEnum.UNFINISH.getStatus());
        examineeRecord.setCalculated(false);
        examineeRecord.setStartTime(LocalDateTime.now());
        examineeRecordService.save(examineeRecord);

        examineeRecordQuestionList.forEach(q -> {
            q.setId(null);
            q.setExamineeRecordId(examineeRecord.getId());
            q.setOrgId(examinee.getOrgId());
        });

        examineeRecordQuestionService.saveBatch(examineeRecordQuestionList);

        // 更新考生剩余考试次数
        if (examLimitCount != null && examLimitCount > 0) {
            examineeService.updateRemainingTimes(examineeId, examLimitCount);
        }
    }

    @Override
    public ExamAndCertDetailResponse selectCertRuleByExamineeId(Long examineeId) {
        return baseMapper.selectCertRuleByExamineeId(examineeId);
    }
}

