package cn.qbs.wa.teach.exam.answer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.exception.exam.ExamServiceException;
import cn.qbs.wa.teach.common.redis.service.RedisService;
import cn.qbs.wa.teach.exam.answer.mapper.ExamMapper;
import cn.qbs.wa.teach.exam.answer.mapper.ExamineeRecordMapper;
import cn.qbs.wa.teach.exam.answer.pojo.*;
import cn.qbs.wa.teach.exam.answer.pojo.center.ExamineRecordResponse;
import cn.qbs.wa.teach.exam.answer.pojo.center.PageRequest;
import cn.qbs.wa.teach.exam.answer.pojo.center.PageResponse;
import cn.qbs.wa.teach.exam.answer.pojo.exam.ExamAnswerSubmitRequest;
import cn.qbs.wa.teach.exam.answer.service.*;
import cn.qbs.wa.teach.exam.common.constant.Topics;
import cn.qbs.wa.teach.exam.common.entity.*;
import cn.qbs.wa.teach.exam.common.enumerate.*;
import cn.qbs.wa.teach.common.core.utils.DateFormatUtils;
import cn.qbs.wa.teach.exam.common.util.RedisKeyUtil;
import cn.qbs.wa.teach.exam.common.util.RedisLockKeyUtil;
import cn.qbs.wa.teach.question.api.pojo.DTO.paper.PaperDetailDTO;
import cn.qbs.wa.teach.question.api.pojo.DTO.question.QuestionDetailsDTO;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qbs.tdmq.producer.TdmqProducerTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
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

    private final RedisService redisService;

    private final ExamUserVisibleService examUserVisibleService;

    private final ExamineeService examineeService;

    private final ExamineeRecordService examineeRecordService;

    private final ExamineeRecordQuestionService examineeRecordQuestionService;

    private final TdmqProducerTemplate tdmqProducerTemplate;

    private final JobService jobService;

    private final ExamineeRecordMapper examineeRecordMapper;

    private final NoticeBeforeExamService noticeBeforeExamService;

    private final ExamMapper examMapper;

    private final RedissonClient redissonClient;


    @Override
    public ExamVO getExam(Long examId) {
        // 根据考试ID查询考试信息以及考试规则
        ExamVO exam = this.baseMapper.selectExamById(examId);
        if (exam == null) {
            throw new ExamServiceException("考试不存在或已删除！");
        }
        // 校验考试状态
        examStatusCheck(BeanUtil.copyProperties(exam, Exam.class));
        Long userId = SecurityContextHolder.getUserId();
        // 查询当前用户是否允许参加本次考试
        if (exam.getUserVisible() == 2) {
            ExamUserVisible examUserVisible = this.examUserVisibleService.lambdaQuery()
                    .eq(ExamUserVisible::getExamId, examId)
                    .eq(ExamUserVisible::getUserId, userId)
                    .last("limit 1")
                    .one();
            if (examUserVisible == null) {
                throw new ExamServiceException("您不能参加本次考试！");
            }
        }
        // 先给个默认值
        exam.setShuffleQuestion(false);
        exam.setShowAnswerDesc(false);

        List<ExamVO.ExamRule> examRuleList = exam.getExamRuleList();
        // 查看是否存在考试规则说明
        if (CollectionUtils.isNotEmpty(examRuleList)) {
            List<String> ruleCodeList = examRuleList.stream()
                    .filter(examRule -> !(RuleEnum.SHOW_ANSWER_DESC.getCode().equals(examRule.getCode())
                            || RuleEnum.SHUFFLE_QUESTION.getCode().equals(examRule.getCode())))
                    .map(ExamVO.ExamRule::getCode)
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(ruleCodeList)) {
                // 按字符升序排序
                Collections.sort(ruleCodeList);
                String ruleCodeStr = String.join("+", ruleCodeList);
                NoticeBeforeExam noticeBeforeExam = noticeBeforeExamService.getById(ruleCodeStr);
                if (noticeBeforeExam != null) {
                    exam.setNoticeBeforeExam(noticeBeforeExam.getNoticeBeforeExam());
                }
            }
        }

        // 先给个默认值
        exam.setSubmitPaper(false);
        // 查询考生最后1次考试
        // 先查询考生
        Examinee examinee = examineeService.getExaminee(examId, userId);
        if (examinee != null) {
            exam.setRemainingTimes(examinee.getRemainingTimes());
            Long examineeId = examinee.getId();
            exam.setExamineeId(examineeId);
            ExamineeRecord examineeRecord = examineeRecordService.getLastExamineeRecord(examineeId);
            // 当考试已完成的情况下，设置试卷为已提交
            if (examineeRecord != null) {
                if (examineeRecord.getStatus() >= ExamineeRecordStatusEnum.TO_BE_CORRECTED.getStatus()) {
                    exam.setSubmitPaper(true);
                }
                // 设置考试记录ID
                exam.setExamineeRecordId(examineeRecord.getId());
            }
        }

        return exam;
    }

    @Override
    public R<ExamWithQuestions> getExamWithQuestions(Long examId) {
        if (examId == null) {
            throw new ServiceException("考试ID不能为空！");
        }

        ExamWithQuestions examWithQuestions = new ExamWithQuestions();
        Long userId = SecurityContextHolder.getUserId();
        // 获取添加试题结果
        String addExamQuestionsRetKey = RedisKeyUtil.getAddExamQuestionsRetKey(examId, userId);
        Integer addExamWithQuestionsRet = redisService.getCacheObject(addExamQuestionsRetKey);
        if (addExamWithQuestionsRet != null && addExamWithQuestionsRet.equals(ExamQuestionStatusEnum.ADDING.getStatus())) {
            // 考题正在添加中
            examWithQuestions.setExamQuestionStatus(ExamQuestionStatusEnum.ADDING.getStatus());
            return R.ok(examWithQuestions);
        }

        // addExamWithQuestionsRet 结果为null 或者 其它值时，有可能是未添加考题或者缓存标记已过期，需要从数据库中查询

        Exam exam = this.getById(examId);

        // 查询考生
        Long orgId = SecurityContextHolder.getOrgId();
        Long examineeId = null;
        String lockKey = RedisLockKeyUtil.getAddExamineeLockKey(examId, userId);
        RLock rLock = redissonClient.getFairLock(lockKey);
        try {
            rLock.lock(10, TimeUnit.SECONDS);
            Examinee examinee = selectOrAddExaminee(exam, orgId, userId);
            examineeId = examinee.getId();
        } finally {
            if (rLock != null && rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }
        // 查询考生最后1次考试
        if (examineeId == null) {
            throw new ServiceException("考试创建失败！");
        }
        ExamineeRecord examineeRecord = examineeRecordService.getLastExamineeRecord(examineeId);
        // 如果没有考试记录或者最后1次考试已完成，则开始新的1次考试，发送消息，通过消息队列添加考题
        if (examineeRecord == null) {
            sendAddExamQuestionsMsg(examId, userId, addExamQuestionsRetKey, examineeId, orgId);
            // 考题正在添加中
            examWithQuestions.setExamQuestionStatus(ExamQuestionStatusEnum.ADDING.getStatus());
            return R.ok(examWithQuestions);
        }
        if (examineeRecord.getStatus() != ExamineeRecordStatusEnum.UNFINISH.getStatus()) {
            throw new ExamServiceException("考试已交卷！");
        }

        // 以下步骤获取考试试题
        BeanUtil.copyProperties(exam, examWithQuestions);
        examWithQuestions.setExamId(examId);
        Long examineeRecordId = examineeRecord.getId();
        examWithQuestions.setExamineeId(examineeId);
        examWithQuestions.setExamineeRecordId(examineeRecordId);
        examWithQuestions.setStartAnswerTime(examineeRecord.getStartTime());

        // 查询试题
        List<ExamineeRecordQuestion> examineeRecordQuestionList = examineeRecordQuestionService.lambdaQuery()
                .eq(ExamineeRecordQuestion::getExamineeRecordId, examineeRecordId)
                .list();
        Map<Long, ExamineeRecordQuestion> recordQuestionMap = examineeRecordQuestionList.stream().collect(Collectors.toMap(ExamineeRecordQuestion::getQuestionId, o -> o));

        // 查询试卷内容
        PaperDetailDTO paperDetail = paperService.getPaperDetail(exam.getPaperId());
        if (paperDetail == null) {
            throw new ServiceException("查不到考试试卷！");
        }

        List<QuestionType> questionTypeList = paperDetail.getQuestionTypeList().stream().map(item -> {
            QuestionType qt = new QuestionType();
            BeanUtil.copyProperties(item, qt, true);
            return qt;
        }).collect(Collectors.toList());
        examWithQuestions.setQuestionTypeList(questionTypeList);

        // 组装试卷内容
        examWithQuestions.getQuestionTypeList().forEach(qt -> {
            List<Question> questionList = qt.getQuestionList();
            Iterator<Question> iterator = questionList.iterator();
            while (iterator.hasNext()) {
                Question question = iterator.next();
                ExamineeRecordQuestion examineeRecordQuestion = recordQuestionMap.get(question.getId());
                if (examineeRecordQuestion != null) {
                    question.setExamineeQuestionId(examineeRecordQuestion.getId());
                    question.setQuestionScore(examineeRecordQuestion.getQuestionScore());
                    question.setAnswer(examineeRecordQuestion.getAnswer());
                    question.setSortNum(examineeRecordQuestion.getSortNum());
                } else {
                    iterator.remove();
                }
            }

            questionList = questionList.stream().sorted(Comparator.comparing(Question::getSortNum)).collect(Collectors.toList());
            qt.setQuestionList(questionList);
        });

        examWithQuestions.setExamQuestionStatus(ExamQuestionStatusEnum.ADDED.getStatus());
        return R.ok(examWithQuestions);
    }

    /**
     * 校验考试状态
     * @param exam 考试对象
     */
    private void examStatusCheck(Exam exam) {
        if (exam == null) {
            throw new ExamServiceException("考试不存在或已删除！");
        }
        if (exam.getShelfStatus() == ShelfStatusEnum.OFF.getStatus()) {
            throw new ExamServiceException("考试未发布！");
        }
        if (exam.getStatus() == ExamStatusEnum.NOT_START.getStatus() || exam.getStartTime().isAfter(LocalDateTime.now())) {
            throw new ExamServiceException("考试未开始！");
        }
        if (exam.getStatus() == ExamStatusEnum.EXAM_ENDED.getStatus() || LocalDateTime.now().isAfter(exam.getEndTime())) {
            throw new ExamServiceException("考试已结束！");
        }
        if (exam.getStatus() != ExamStatusEnum.DURING_EXAM.getStatus()) {
            throw new ServiceException("考试状态异常！");
        }
    }

    private void updateExamEdited(Exam exam) {
        if (exam == null) {
            throw new ServiceException("考试不能为空！");
        }

        if (exam.getEdited() == 1) {
            this.lambdaUpdate().eq(Exam::getId, exam.getId()).set(Exam::getEdited, 0).update();
        }
    }

    private void sendAddExamQuestionsMsg(Long examId, Long userId, String addExamQuestionsRetKey, Long examineeId, Long orgId) {
        Map<String, Object> data = new HashMap<>(4);
        data.put("examId", examId);
        data.put("examineeId", examineeId);
        data.put("orgId", orgId);
        data.put("userId", userId);
        tdmqProducerTemplate.sendAsyncStringMsg(Topics.ADD_EXAM_QUESTIONS, JSON.toJSONString(data));

        // 添加缓存标记
        redisService.setCacheObject(addExamQuestionsRetKey, ExamQuestionStatusEnum.ADDING.getStatus(), 5L, TimeUnit.MINUTES);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Examinee selectOrAddExaminee(Exam exam, Long orgId, Long userId) {
        // 查询考生
        Long examId = exam.getId();
        Examinee examinee = examineeService.getExaminee(examId, userId);
        if (examinee == null) {
            examinee = new Examinee();
            examinee.setExamId(examId);
            examinee.setUserId(userId);
            examinee.setEmployeeId(SecurityContextHolder.getEmployeeId());
            examinee.setStudentId(SecurityContextHolder.getStudentId());
            examinee.setOrgId(orgId);
            examinee.setRemainingTimes(exam.getLimitCount());
            examineeService.save(examinee);

            // 2022-02-11 讨论无需在此步骤进行锁定编辑，试卷已经在上架后不允许编辑
            //updateExamEdited(exam);
        }

        return examinee;
    }

    @Override
    public void updateStartAnswerTime(Long examineeRecordId) {
        ExamineeRecord examineeRecord = this.examineeRecordService.getById(examineeRecordId);
        if (examineeRecord == null) {
            log.error("考试记录不存在！ -> {}", examineeRecordId);
            return;
        }
        if (examineeRecord.getStartTime() == null || examineeRecord.getStartTime().equals(examineeRecord.getCreateTime())) {
            LocalDateTime now = LocalDateTime.now();
            if (now.isAfter(examineeRecord.getStartTime())) {
                // 校准一次考试开始答题时间
                examineeRecord.setStartTime(now);
                this.examineeRecordService.updateById(examineeRecord);

                Exam exam = this.examMapper.selectExamByExamineeRecordId(examineeRecordId);
                if (exam != null && ExamModeEnum.ADVANCED.getMode() == exam.getExamMode()) {
                    LocalDateTime lastSubmitTime = now.plus(exam.getDuration(), ChronoUnit.MINUTES);
                    jobService.createAutoSubmitPaperJob(examineeRecordId, lastSubmitTime, examineeRecord.getOrgId());
                }
            }
        }

    }

    @Override
    public void submitAnswer(List<SubmitAnswerRequest> submitAnswerList) {
        if (CollectionUtils.isNotEmpty(submitAnswerList)) {
            // 校验考试记录状态
            Long examineeQuestionId = submitAnswerList.get(0).getExamineeQuestionId();
            ExamineeRecord examineeRecord = this.examineeRecordMapper.selectByExamineeQuestionId(examineeQuestionId);
            if (examineeRecord == null) {
                throw new ServiceException("查不到考试记录！");
            }
            if (ExamineeRecordStatusEnum.INTERRUPT.getStatus() == examineeRecord.getStatus()) {
                throw new ExamServiceException(String.format("考试被中断，原因【%s】!", examineeRecord.getRemark()));
            }

            if (examineeRecord.getStatus() >= ExamineeRecordStatusEnum.TO_BE_CORRECTED.getStatus() || examineeRecord.getSubmitTime() != null) {
                throw new ServiceException("考试已交卷!");
            }

            Examinee examinee = examineeService.getById(examineeRecord.getExamineeId());
            if (examinee == null) {
                throw new ServiceException("考生不存在或已删除!");
            }

            Exam exam = examMapper.selectById(examinee.getExamId());
            examStatusCheck(exam);

            for (SubmitAnswerRequest submitAnswerRequest : submitAnswerList) {
                this.submitAnswer(submitAnswerRequest);
            }
        }
    }

    @Override
    public void submitAnswer(ExamAnswerSubmitRequest submitAnswer) {
        // 先校验考试是否结束
        Exam exam = examMapper.selectById(submitAnswer.getExamId());
        examStatusCheck(exam);

        // 校验考试记录状态
        ExamineeRecord examineeRecord = examineeRecordService.getById(submitAnswer.getExamineeRecordId());
        if (examineeRecord == null) {
            throw new ServiceException("查不到考试记录！");
        }
        if (ExamineeRecordStatusEnum.INTERRUPT.getStatus() == examineeRecord.getStatus()) {
            throw new ExamServiceException(String.format("考试被中断，原因【%s】!", examineeRecord.getRemark()));
        }

        if (examineeRecord.getStatus() >= ExamineeRecordStatusEnum.TO_BE_CORRECTED.getStatus() || examineeRecord.getSubmitTime() != null) {
            throw new ServiceException("考试已交卷!");
        }

        for (SubmitAnswerRequest submitAnswerRequest : submitAnswer.getSubmitAnswerList()) {
            this.submitAnswer(submitAnswerRequest);
        }
    }

    private void submitAnswer(SubmitAnswerRequest submitAnswer) {
        boolean success = this.examineeRecordQuestionService.lambdaUpdate()
                .eq(ExamineeRecordQuestion::getId, submitAnswer.getExamineeQuestionId())
                .set(ExamineeRecordQuestion::getAnswer, submitAnswer.getAnswer())
                .set(ExamineeRecordQuestion::getAnswerStatus, 1)
                .set(ExamineeRecordQuestion::getAnswerTime, LocalDateTime.now())
                .update();
        if (!success) {
            throw new ServiceException("提交答案失败！");
        }
    }

    @Override
    public void submitPaper(Long examineeRecordId) {
        ExamineeRecord examineeRecord = this.examineeRecordService.getById(examineeRecordId);
        this.submitPaper(examineeRecord);
    }

    @Override
    public void submitPaper(ExamineeRecord examineeRecord) {
        if (examineeRecord == null) {
            throw new ServiceException("考试记录不存在！");
        }
        if (examineeRecord.getSubmitTime() != null) {
//            throw new ServiceException("试卷已提交！");
            return;
        }

        examineeRecord.setSubmitTime(LocalDateTime.now());
        examineeRecord.setStatus(ExamineeRecordStatusEnum.TO_BE_CORRECTED.getStatus());
        LocalDateTime startTime = examineeRecord.getStartTime();
        if (startTime == null) {
            startTime = examineeRecord.getCreateTime();
            examineeRecord.setStartTime(startTime);
        }
        Long diffSeconds = ChronoUnit.SECONDS.between(startTime, examineeRecord.getSubmitTime());
        examineeRecord.setUseDuration(diffSeconds.intValue());
        String secondsFormat = DateFormatUtils.secondsFormat(diffSeconds);
        examineeRecord.setUseDurationFormat(secondsFormat);
        this.examineeRecordService.updateById(examineeRecord);

        tdmqProducerTemplate.sendAsyncStringMsg(Topics.SUBMIT_PAPER, String.valueOf(examineeRecord.getId()));
        //调用证书mq
        /*Map<String, Object> data = new HashMap<>(2);
        data.put("examineeId", examineeRecord.getExamineeId());
        data.put("orgId", SecurityContextHolder.getOrgId());
        tdmqProducerTemplate.sendAsyncStringMsg(Topics.ISSUING_OF_CERTIFICATES,  JSON.toJSONString(data));*/
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void againExam(Long examId) {
        Long userId = SecurityContextHolder.getUserId();
        String lockKey = RedisLockKeyUtil.getAddExamQuestionsLockKey(examId, userId);
        RLock rLock = redissonClient.getFairLock(lockKey);
        try {
            rLock.lock(10, TimeUnit.SECONDS);
            Exam exam = this.getById(examId);
            examStatusCheck(exam);

            Examinee examinee = this.examineeService.lambdaQuery().eq(Examinee::getUserId, userId)
                    .eq(Examinee::getExamId, examId).one();
            if (examinee == null) {
                throw new ExamServiceException("你没有参与此考试！");
            }

            // 查询未完成的考试记录
            Long count = this.examineeRecordService.lambdaQuery().eq(ExamineeRecord::getExamineeId, examinee.getId())
                    .eq(ExamineeRecord::getStatus, ExamineeRecordStatusEnum.UNFINISH.getStatus()).count();
            if (count != null && count > 0) {
                throw new ExamServiceException("请先完成上次考试！");
            }

            Integer examLimitCount = exam.getLimitCount();
            if (examLimitCount != null && examLimitCount > 0) {
                Integer remainingTimes = examinee.getRemainingTimes();
                if (remainingTimes != null && remainingTimes == 0) {
                    throw new ExamServiceException("你的考试次数已经用完！");
                }
            }

            PaperDetailDTO paperDetail = paperService.getPaperDetail(exam.getPaperId());
            if (paperDetail == null) {
                throw new ExamServiceException("查不到考试试卷！");
            }

            // 查询是否需要打乱试题顺序
            Boolean shuffleQuestion = examMapper.selectHasRule(examId, RuleEnum.SHUFFLE_QUESTION.getCode());
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

            addExamineeRecordAndQuestions(examinee, examineeRecordQuestionList, examLimitCount);
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
    public IPage<PageResponse> centerPage(PageRequest params) {
        IPage<PageResponse> page = this.baseMapper.centerPage(params.createMpPage(), params);
        if (page.getRecords().isEmpty()) {
            return page;
        }
        if (params.getStatus() == null) {
            params.setStatus(ExamStatusEnum.DURING_EXAM.getStatus());
        }
        ExamStatusEnum statusEnum = ExamStatusEnum.fromStatus(params.getStatus());
        if (statusEnum != null) {
            Long userId = SecurityContextHolder.getUserId();
            List<Long> examIds = page.getRecords().stream().map(PageResponse::getId).collect(Collectors.toList());
            List<ExamineRecordResponse> examineList = null;
            List<Examinee> examinees = null;
            switch (statusEnum) {
                case EXAM_ENDED:
                    // 查询最高分
                    examineList = this.baseMapper.calculatedExamRecordByExamIds(userId, examIds);
                    List<Long> existScoreId = examineList.stream().map(ExamineRecordResponse::getExamId).collect(Collectors.toList());
                    // 查询我的考试记录
                    examinees = examineeService.lambdaQuery().eq(Examinee::getUserId, userId).in(Examinee::getExamId, examIds).list();
                    if (CollectionUtils.isNotEmpty(examineList) && CollectionUtils.isNotEmpty(examinees)){
                        List<Examinee> notExistScoreExamList = examinees.stream().filter(i -> !existScoreId.contains(i.getExamId())).collect(Collectors.toList());
                        Map<Long, Examinee> notExistScoreExamMap = notExistScoreExamList.stream().collect(Collectors.toMap(Examinee::getExamId, o -> o));
                        page.getRecords().forEach(r -> {
                            if (CollectionUtils.isNotEmpty(notExistScoreExamMap) && notExistScoreExamMap.containsKey(r.getId())){
                                r.setHighestScore(null);
                                r.setRemainingTimes(notExistScoreExamMap.get(r.getId()).getRemainingTimes());
                                r.setExamineeId(notExistScoreExamMap.get(r.getId()).getId());
                                r.setStudentExamStatus(StudentExamStatusEnum.COMPLETED.getStatus());
                                r.setExamineeRecordStatus(ExamineeRecordStatusEnum.TO_BE_CORRECTED.getStatus());
                            }
                        });
                    }
                    break;
                case DURING_EXAM:
                    // 查询我的考试记录
                    examinees = examineeService.lambdaQuery().eq(Examinee::getUserId, userId).in(Examinee::getExamId, examIds).list();
                    if (!examinees.isEmpty()) {
                        examineList = examinees.stream().map(e -> {
                            ExamineRecordResponse response = null;
                            // 考试记录
                            //ExamineeRecord examineeRecord = examineeRecordService.getLastExamineeRecord(e.getId());
                            //if (examineeRecord != null) {
                            //    response = BeanUtil.copyProperties(examineeRecord, ExamineRecordResponse.class);
                            //    response.setHighestScore(examineeRecord.getScore());
                            //    response.setStudentExamStatus(examineeRecord.getStatus());
                            //}
                            List<ExamineRecordResponse> list = this.baseMapper.calculatedExamRecordByNeeId(e.getId());
                            if (!list.isEmpty()) {
                                Optional<ExamineRecordResponse> max = list.stream().max(Comparator.comparing(ExamineRecordResponse::getCreateTime));
                                if (max.isPresent()) {
                                    response = max.get();
                                }
                            }
                            if (response == null) {
                                response = new ExamineRecordResponse();
                                response.setExamineeId(e.getId());
                            }
                            response.setRemainingTimes(e.getRemainingTimes());
                            response.setExamId(e.getExamId());
                            return response;
                        }).collect(Collectors.toList());
                    }
                    break;
                default:
            }
            if (CollUtil.isNotEmpty(examineList)) {
                Map<Long, ExamineRecordResponse> responseMap = examineList.stream().collect(Collectors.toMap(ExamineRecordResponse::getExamId, o -> o, (a, b) -> a));
                page.getRecords().forEach(r ->
                    Optional.ofNullable(responseMap.get(r.getId())).ifPresent(e -> {
                        r.setHighestScore(e.getHighestScore());
                        r.setRemainingTimes(e.getRemainingTimes());
                        r.setExamineeId(e.getExamineeId());
                        r.setStudentExamStatus(e.getStudentExamStatus());
                        r.setExamineeRecordStatus(ExamineeRecordStatusEnum.CORRECTED.getStatus());
                    })
                );
            }
        }
        return page;
    }

    @Override
    public IPage<PageResponse> recordPage(PageRequest params) {
        IPage<PageResponse> page = this.baseMapper.recordPage(params.createMpPage(), params);
        if (page.getRecords().isEmpty()) {
            return page;
        }
        if (params.getStatus() == null) {
            params.setStatus(ExamStatusEnum.DURING_EXAM.getStatus());
        }
        ExamStatusEnum statusEnum = ExamStatusEnum.fromStatus(params.getStatus());
        if (statusEnum != null) {
            Long userId = SecurityContextHolder.getUserId();
            List<Long> examIds = page.getRecords().stream().map(PageResponse::getId).collect(Collectors.toList());
            List<ExamineRecordResponse> examineList = null;
            switch (statusEnum) {
                case EXAM_ENDED:
                    // 查询最高分
                    examineList = this.baseMapper.calculatedExamRecordByExamIds(userId, examIds);
                    break;
                case DURING_EXAM:
                    // 查询我的考试记录
                    List<Examinee> examinees = examineeService.lambdaQuery().eq(Examinee::getUserId, userId).in(Examinee::getExamId, examIds).list();
                    if (!examinees.isEmpty()) {
                        examineList = examinees.stream().map(e -> {
                            ExamineRecordResponse response = null;
                            // 考试记录
                            ExamineeRecord examineeRecord = examineeRecordService.getLastExamineeRecord(e.getId());
                            if (examineeRecord != null) {
                                response = BeanUtil.copyProperties(examineeRecord, ExamineRecordResponse.class);
                                response.setHighestScore(examineeRecord.getScore());
                                response.setStudentExamStatus(examineeRecord.getStatus());
                                response.setExamId(e.getExamId());
                            }
                            List<ExamineRecordResponse> list = this.baseMapper.calculatedExamRecordByNeeId(e.getId());
                            if (!list.isEmpty()) {
                                Optional<ExamineRecordResponse> opt = list.stream().filter(o -> Boolean.TRUE.equals(o.getCalculated())).findFirst();
                                if (opt.isPresent()) {
                                    response = opt.get();
                                } else {
                                    Optional<ExamineRecordResponse> max = list.stream().max(Comparator.comparing(ExamineRecordResponse::getCreateTime));
                                    if (max.isPresent()) {
                                        response = max.get();
                                    }
                                }
                            }
                            if (response == null) {
                                response = new ExamineRecordResponse();
                                response.setExamineeId(e.getId());
                                response.setExamId(e.getExamId());
                            }
                            response.setRemainingTimes(e.getRemainingTimes());
                            response.setExamId(e.getExamId());
                            return response;
                        }).collect(Collectors.toList());
                    }
                    break;
                default:
            }
            if (CollUtil.isNotEmpty(examineList)) {
                Map<Long, ExamineRecordResponse> responseMap = examineList.stream().collect(Collectors.toMap(ExamineRecordResponse::getExamId, o -> o, (a, b) -> a));
                page.getRecords().forEach(r ->
                        Optional.ofNullable(responseMap.get(r.getId())).ifPresent(e -> {
                            r.setHighestScore(e.getHighestScore());
                            r.setRemainingTimes(e.getRemainingTimes());
                            r.setExamineeId(e.getExamineeId());
                            r.setExamId(e.getExamId());
                            r.setStudentExamStatus(e.getStudentExamStatus());
                        })
                );
            }
        }
        return page;
    }
}

