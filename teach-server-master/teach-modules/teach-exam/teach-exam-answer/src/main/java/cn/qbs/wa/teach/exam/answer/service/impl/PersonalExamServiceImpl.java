package cn.qbs.wa.teach.exam.answer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.exception.exam.ExamServiceException;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.common.redis.service.RedisService;
import cn.qbs.wa.teach.exam.answer.mapper.ExamMapper;
import cn.qbs.wa.teach.exam.answer.pojo.*;
import cn.qbs.wa.teach.exam.answer.pojo.center.ExamCommonRequest;
import cn.qbs.wa.teach.exam.answer.pojo.exam.ExamAnswerSubmitRequest;
import cn.qbs.wa.teach.exam.answer.service.*;
import cn.qbs.wa.teach.exam.common.constant.Topics;
import cn.qbs.wa.teach.exam.common.entity.*;
import cn.qbs.wa.teach.exam.common.enumerate.*;
import cn.qbs.wa.teach.exam.common.util.RedisKeyUtil;
import cn.qbs.wa.teach.exam.common.util.RedisLockKeyUtil;
import cn.qbs.wa.teach.question.api.pojo.DTO.paper.PaperDetailDTO;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.qbs.tdmq.producer.TdmqProducerTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 考试表(Exam)表服务实现类
 *
 * @author zcm
 * @since 2021-12-14 11:40:51
 */
@Slf4j
@Service("personalExamService")
@RequiredArgsConstructor
public class PersonalExamServiceImpl implements PersonalExamService {

    private final PaperService paperService;

    private final ExamMapper examMapper;

    private final ExamineeService examineeService;

    private final ExamineeRecordService examineeRecordService;

    private final ExamineeRecordQuestionService examineeRecordQuestionService;

    private final ExamUserVisibleService examUserVisibleService;

    private final NoticeBeforeExamService noticeBeforeExamService;

    private final TdmqProducerTemplate tdmqProducerTemplate;

    private final RedisService redisService;

    private final RedissonClient redissonClient;

    @Resource
    private RuleService ruleService;


    @Override
    public ExamVO getExam(Long examId) {
        // 根据考试ID查询考试信息以及考试规则
        ExamVO exam = this.examMapper.selectExamById(examId);
        if (exam == null) {
            throw new ExamServiceException("考试不存在或已删除！");
        }
        // 校验考试状态
        Exam examPO = BeanUtil.copyProperties(exam, Exam.class);
        checkExamIsStartAndNotEnd(examPO);

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

        Examinee examinee = examineeService.getExaminee(examId, userId);
        // 查询考生最后1次考试
        ExamineeRecord lastExamineeRecord = null;
        // 是否高级模式
        if (ExamModeEnum.ADVANCED.getMode() == exam.getExamMode()) {
            // 高级模式下，虽然考试还未结束，但是在考试入场截止时间前仍未进入过考试的，不再允许进入
            if (LocalDateTime.now().isAfter(exam.getAdmissionEndTime())) {
                if (examinee == null) {
                    throw new ExamServiceException("已过考试入场截止时间，不能再进入考试！");
                }

                // 如果最后这次考试已结束，也不能再进入了
                /*lastExamineeRecord = examineeRecordService.getLastExamineeRecord(examinee.getId());
                if (lastExamineeRecord.getStatus().compareTo(ExamineeRecordStatusEnum.TO_BE_CORRECTED.getStatus()) >= 0) {
                    throw new ExamServiceException("已过考试入场截止时间，不能再进入考试！");
                }*/
            }
        }

        // 先给个默认值
        exam.setSubmitPaper(false);

        if (examinee != null) {
            log.info("考生已创建");
            exam.setRemainingTimes(examinee.getRemainingTimes());
            Long examineeId = examinee.getId();
            // 如果是考级模式，上面可能已经查过了，这里就有可能不为空，需要判断一下
            if (lastExamineeRecord == null) {
                lastExamineeRecord = examineeRecordService.getLastExamineeRecord(examineeId);
            }
            if (lastExamineeRecord != null) {
                // 设置考试记录ID
                exam.setExamineeRecordId(lastExamineeRecord.getId());
                // 当考试已完成的情况下，则重新考试；否则继续考试
                if (lastExamineeRecord.getStatus() >= ExamineeRecordStatusEnum.TO_BE_CORRECTED.getStatus()) {
                    Integer examLimitCount = exam.getLimitCount();
                    if (examLimitCount != null && examLimitCount > 0) {
                        Integer remainingTimes = examinee.getRemainingTimes();
                        if (remainingTimes != null && remainingTimes == 0) {
                            throw new ExamServiceException("你的考试次数已经用完！");
                        }
                    }
                    // 重新创建考试记录，重新考试
                    exam.setSubmitPaper(true);
                }
            }

        } else {
            // 没有考生记录，说明当前学员第一次进行该考试，可进行考生创建
            try {
                examinee = getExaminee(examPO, SecurityContextHolder.getEmployeeId(), userId);
            } catch (Exception e) {
                log.error("考试创建失败！", e);
                throw new ServiceException("考试创建失败！");
            }
            Long orgId = SecurityContextHolder.getOrgId();
            // 异步创建学员考题
            log.info("调用学员创建考题");
            sendAddExamQuestionsMsg(examId, userId, RedisKeyUtil.getAddExamQuestionsRetKey(examId, userId), examinee.getId(), orgId);
        }
        exam.setExamineeId(examinee.getId());

        // 获取试卷试题总数
        PaperDetailDTO paperDetail = paperService.getPaperDetail(exam.getPaperId());
        if (paperDetail == null) {
            throw new ServiceException("查不到考试试卷！");
        }
        exam.setQuestionCount(paperDetail.getQuestionCount());

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

        return exam;
    }

    @Override
    public ExamVO examPreview(Long examId) {
        // 根据考试ID查询考试信息以及考试规则
        ExamVO exam = this.examMapper.selectExamById(examId);
        if (exam == null) {
            throw new ExamServiceException("考试不存在或已删除！");
        }
        // 校验考试状态
        Exam examPO = BeanUtil.copyProperties(exam, Exam.class);
        checkExamIsEnd(examPO);
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

        // 获取试卷试题总数
        PaperDetailDTO paperDetail = paperService.getPaperDetail(exam.getPaperId());
        if (paperDetail == null) {
            throw new ServiceException("查不到考试试卷！");
        }
        exam.setQuestionCount(paperDetail.getQuestionCount());

        List<ExamVO.ExamRule> examRuleList = exam.getExamRuleList();
        // 查看是否存在考试规则说明
        if (CollectionUtils.isNotEmpty(examRuleList)) {
            List<String> ruleCodeList = examRuleList.stream()
                    .filter(examRule -> examRule.getType().equals(1))
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
        // 先查询考生
//        Examinee examinee = examineeService.getExaminee(examId, userId);
//        if (examinee != null) {
//            exam.setExamineeId(examinee.getId());
//        }
//        else {
//            // 没有考生记录，说明当前学员第一次进行该考试，可进行考生创建
//            try {
//                examinee = getExaminee(examPO, SecurityContextHolder.getEmployeeId(), userId);
//            } catch (Exception e) {
//                log.error("考试创建失败！", e);
//                throw new ServiceException("考试创建失败！");
//            }
//            exam.setExamineeId(examinee.getId());
//        }
        return exam;
    }

    @Override
    public R<ExamWithQuestions> getExamWithQuestions(ExamCommonRequest request) {
        Long examId = request.getId();
        Exam exam = examMapper.selectById(examId);
        checkExamIsStartAndNotEnd(exam);

        Long examineeId = request.getExamineeId();
        Long examineeRecordId = request.getExamineeRecordId();

        ExamWithQuestions examWithQuestions = new ExamWithQuestions();
        // 默认考题正在添加中
        examWithQuestions.setExamQuestionStatus(ExamQuestionStatusEnum.ADDING.getStatus());
        ExamineeRecord examineeRecord = null;
        if (examineeRecordId == null) {
            Long userId = SecurityContextHolder.getUserId();
            // 获取添加试题结果
            Integer addExamWithQuestionsRet = redisService.getCacheObject(RedisKeyUtil.getAddExamQuestionsRetKey(examId, userId));
            // addExamWithQuestionsRet 结果为null 或者 其它值时，有可能是未添加考题或者缓存标记已过期，需要从数据库中查询
            if (addExamWithQuestionsRet != null && addExamWithQuestionsRet.equals(ExamQuestionStatusEnum.ADDING.getStatus())) {
                return R.ok(examWithQuestions);
            }
            examineeRecord = examineeRecordService.getLastExamineeRecord(examineeId);
            // 如果没有考试记录，说明有异常情况，记录日志
            if (examineeRecord == null) {
                log.info("考生考试记录缺失，examineeId：{}，发送消息添加考题。", examineeId);
                sendAddExamQuestionsMsg(examId, userId, RedisKeyUtil.getAddExamQuestionsRetKey(examId, userId), examineeId, SecurityContextHolder.getOrgId());
                return R.ok(examWithQuestions);
            }
            if (examineeRecord.getStatus() != ExamineeRecordStatusEnum.UNFINISH.getStatus()) {
                throw new ExamServiceException("考试已交卷！");
            }
            examineeRecordId = examineeRecord.getId();
        } else {
            examineeRecord = examineeRecordService.getById(examineeRecordId);
            if (examineeRecord == null) {
                throw new ExamServiceException("查不到考试记录！");
            }
        }

        PaperDetailDTO paperDetail = paperService.getPaperDetail(exam.getPaperId());
        if (paperDetail == null) {
            throw new ServiceException("查不到考试试卷！");
        }

        // 组成试卷
        List<QuestionType> questionTypeList = this.makeUpQuestion(examineeRecordId, paperDetail);
        // 获取试卷试题总数
        examWithQuestions.setQuestionCount(paperDetail.getQuestionCount());
        // 如果没有考试记录或者最后1次考试已完成，则开始新的1次考试，发送消息，通过消息队列添加考题
        if (CollUtil.isEmpty(questionTypeList)) {
            // 考题正在添加中
            examWithQuestions.setExamQuestionStatus(ExamQuestionStatusEnum.ADDING.getStatus());
            return R.ok(examWithQuestions);
        }
        // 以下步骤获取考试试题
        BeanUtil.copyProperties(exam, examWithQuestions);
        examWithQuestions.setExamId(examId);
        examWithQuestions.setExamineeId(examineeId);
        examWithQuestions.setExamineeRecordId(examineeRecordId);
        examWithQuestions.setQuestionTypeList(questionTypeList);
        examWithQuestions.setExamQuestionStatus(ExamQuestionStatusEnum.ADDED.getStatus());
        examWithQuestions.setStartAnswerTime(examineeRecord.getStartTime());
        examWithQuestions.setAttachmentName(examineeRecord.getAttachmentName());
        examWithQuestions.setAttachmentUrl(examineeRecord.getAttachmentUrl());

        Rule supportUploadAttachmentRule = this.ruleService.selectOneByExamIdAndCode(exam.getId(), RuleEnum.SUPPORT_UPLOAD_ATTACHMENT.getCode());
        examWithQuestions.setSupportUploadAttachment(supportUploadAttachmentRule != null);

        return R.ok(examWithQuestions);
    }

    /**
     * 组成试卷
     * @param examineeRecordId 考试记录ID
     * @param paperDetail 试卷详情
     * @return 试卷考题
     */
    private List<QuestionType> makeUpQuestion(Long examineeRecordId, PaperDetailDTO paperDetail) {
        if (examineeRecordId == null || paperDetail == null) {
            return null;
        }
        // 查询试题
        List<ExamineeRecordQuestion> examineeRecordQuestionList = examineeRecordQuestionService.lambdaQuery()
                .eq(ExamineeRecordQuestion::getExamineeRecordId, examineeRecordId)
                .list();
        if (examineeRecordQuestionList.isEmpty()) {
            return null;
        }

        Map<Long, ExamineeRecordQuestion> recordQuestionMap = examineeRecordQuestionList.stream().collect(Collectors.toMap(ExamineeRecordQuestion::getQuestionId, o -> o));

        // 组装试卷内容
        List<QuestionType> questionTypeList = paperDetail.getQuestionTypeList().stream()
                .map(item -> BeanUtil.copyProperties(item, QuestionType.class))
                .peek(qt -> {
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
                    qt.setQuestionList(questionList.stream().sorted(Comparator.comparing(Question::getSortNum)).collect(Collectors.toList()));
                }).collect(Collectors.toList());
        return questionTypeList;
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
            throw new ExamServiceException("考试未发布或已下架！");
        }
        if (exam.getStatus() == ExamStatusEnum.NOT_START.getStatus() || exam.getStartTime().isAfter(LocalDateTime.now())) {
            throw new ExamServiceException("考试未开始！");
        }
        if (exam.getStatus() == ExamStatusEnum.EXAM_ENDED.getStatus() || exam.getEndTime().isBefore(LocalDateTime.now())) {
            throw new ExamServiceException("考试已结束！");
        }
        if (exam.getStatus() != ExamStatusEnum.DURING_EXAM.getStatus()) {
            throw new ServiceException("考试状态异常！");
        }
    }

    /**
     * 检查考试是否未结束
     * @param exam
     */
    private void checkExamIsEnd(Exam exam) {
        if (exam == null) {
            throw new ExamServiceException("考试不存在或已删除！");
        }
        if (exam.getShelfStatus() == ShelfStatusEnum.OFF.getStatus()) {
            throw new ExamServiceException("考试未发布或已下架！");
        }
        if (exam.getStatus() == ExamStatusEnum.EXAM_ENDED.getStatus() || LocalDateTime.now().isAfter(exam.getEndTime())) {
            throw new ExamServiceException("考试已结束！");
        }
    }

    /**
     * 检查考试是否已开始并且未结束
     * @param exam
     */
    private void checkExamIsStartAndNotEnd(Exam exam) {
        this.checkExamIsEnd(exam);
        if (exam.getStatus() == ExamStatusEnum.NOT_START.getStatus() || LocalDateTime.now().isBefore(exam.getStartTime())) {
            throw new ExamServiceException("考试未开始！");
        }
    }

    private void sendAddExamQuestionsMsg(Long examId, Long userId, String addExamQuestionsRetKey, Long examineeId, Long orgId) {
        Map<String, Object> data = new HashMap<>(4);
        data.put("examId", examId);
        data.put("examineeId", examineeId);
        data.put("orgId", orgId);
        data.put("userId", userId);
        RLock lock = redissonClient.getLock(RedisLockKeyUtil.getAddExamQuestionsLockKey(examId, userId));
        try {
            if (lock.tryLock()) {
                tdmqProducerTemplate.sendAsyncStringMsg(Topics.ADD_EXAM_QUESTIONS, JSON.toJSONString(data));
                // 添加缓存标记
                redisService.setCacheObject(addExamQuestionsRetKey, ExamQuestionStatusEnum.ADDING.getStatus(), 5L, TimeUnit.MINUTES);
            }
        } finally {
            if (lock != null && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private Examinee getExaminee(Exam exam, Long employeeId, Long userId) {
        // 查询考生
        Long examId = exam.getId();
        Examinee examinee = examineeService.getExaminee(examId, userId);
        if (examinee == null) {
            examinee = new Examinee();
            examinee.setExamId(examId);
            examinee.setUserId(userId);
            examinee.setEmployeeId(employeeId);
            examinee.setRemainingTimes(exam.getLimitCount());
            examinee.setStudentId(SecurityContextHolder.getStudentId());
            examineeService.save(examinee);

            // 2022-02-11 讨论无需在此步骤进行锁定编辑，试卷已经在上架后不允许编辑
            //updateExamEdited(exam);
        }
        return examinee;
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
    @Transactional(rollbackFor = Exception.class)
    public Long reCreateExamRecord(Long examineeId, Long lastExamRecordId) {

        // 查询考生最后1次考试
        ExamineeRecord lastExamineeRecord = examineeRecordService.getLastExamineeRecord(examineeId);
        if (lastExamineeRecord != null && !lastExamineeRecord.getId().equals(lastExamRecordId)
            && lastExamineeRecord.getStatus().equals(ExamineeRecordStatusEnum.UNFINISH.getStatus())) {
            return lastExamineeRecord.getId();
        }

        ExamineeRecord examineeRecord = new ExamineeRecord();
        examineeRecord.setExamineeId(examineeId);
        examineeRecord.setStatus(ExamineeRecordStatusEnum.UNFINISH.getStatus());
        examineeRecord.setCalculated(Boolean.FALSE);
        examineeRecord.setStartTime(LocalDateTime.now());
        examineeRecordService.save(examineeRecord);

        List<ExamineeRecordQuestion> examineeRecordQuestionList = examineeRecordQuestionService.lambdaQuery()
                .select(ExamineeRecordQuestion::getQuestionId, ExamineeRecordQuestion::getQuestionScore, ExamineeRecordQuestion::getSortNum)
                .eq(ExamineeRecordQuestion::getExamineeRecordId, lastExamRecordId)
                .list();
        Long examRecordId = examineeRecord.getId();
        for (ExamineeRecordQuestion recordQuestion : examineeRecordQuestionList) {
            recordQuestion.setExamineeRecordId(examRecordId);
        }

        examineeRecordQuestionService.saveBatch(examineeRecordQuestionList);

        return examRecordId;
    }

    public void decrRemainingTimes(Long examineeId) {
        examineeService.decrRemainingTimes(examineeId);
    }

}

