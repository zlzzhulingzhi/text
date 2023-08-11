package cn.qbs.wa.teach.exam.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.enums.EnabledEnum;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.exam.admin.mapper.ExamMapper;
import cn.qbs.wa.teach.exam.admin.mapper.ExamineeRecordMapper;
import cn.qbs.wa.teach.exam.admin.pojo.exam.ExamAddRequest;
import cn.qbs.wa.teach.exam.admin.pojo.exam.ExamUpdateRequest;
import cn.qbs.wa.teach.exam.admin.pojo.exam.VisibleUser;
import cn.qbs.wa.teach.exam.admin.service.*;
import cn.qbs.wa.teach.exam.common.constant.Topics;
import cn.qbs.wa.teach.exam.common.entity.*;
import cn.qbs.wa.teach.exam.common.enumerate.*;
import cn.qbs.wa.teach.common.core.utils.DateFormatUtils;
import cn.qbs.wa.teach.exam.common.util.RedisLockKeyUtil;
import cn.qbs.wa.teach.exam.common.util.ValidateUtils;
import cn.qbs.wa.teach.question.api.pojo.DTO.paper.PaperDetailDTO;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qbs.tdmq.producer.TdmqProducerTemplate;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/2/18 11:14
 */
@Service("ComExamServiceImpl")
@Slf4j
public class ComExamServiceImpl extends ServiceImpl<ExamMapper, Exam> implements ComExamService {

    @Resource
    private PaperService paperService;

    @Resource
    private RuleService ruleService;

    @Resource
    private ExamRuleService examRuleService;

    @Resource
    private ExamUserVisibleService examUserVisibleService;

    @Resource
    private JobService jobService;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private ExamineeService examineeService;

    @Resource
    private ExamineeRecordMapper examineeRecordMapper;

    @Resource
    private ExamineeRecordService examineeRecordService;

    @Resource
    private TdmqProducerTemplate tdmqProducerTemplate;

    /**
     * 启动任务提前秒数（允许提前多少秒启动）
     */
    private static final long START_JOB_ADVANCE_SECONDS = 10L;

    /**
     * 启动任务默认添加延迟时间
     */
    private static final long START_JOB_DELAY_SECONDS = 30L;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(ExamAddRequest params) {
        checkParams(params);
        PaperDetailDTO paperDetailDTO = getAndCheckPaper(params.getPaperId(), params.getPassScore());

        //考试基本信息
        Exam exam = new Exam();
        BeanUtil.copyProperties(params, exam);
        exam.setPaperScore(paperDetailDTO.getTotalScore());
        this.save(exam);

        //试卷编辑状态修改
        paperService.editable(params.getPaperId(), false);

        //考试规则
        addExamRule(params.getRuleIdList(), exam.getId());

        //考试可见人
        addExamUserVisible(params.getUserVisible(), params.getVisibleUserList(), exam.getId());

        //定时任务
        addExamTimingTask(exam);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ExamUpdateRequest params) {
        Long examId = params.getId();
        Exam existExam = getById(examId);
        if (existExam == null) {
            throw new IllegalParamsException("查不到考试！");
        }

        // 规则修改：已发布考试中的考试，还可以修改可见用户
        /*if (Constants.DB_FAIL.equals(existExam.getEdited())  ) {
            throw new ServiceException("考试已发布过,不能修改！");
        }*/

        if (ExamStatusEnum.EXAM_ENDED.getStatus() == existExam.getStatus() || LocalDateTime.now().isAfter(existExam.getEndTime())) {
            throw new ServiceException("考试已结束,不能修改！");
        }

        // 已发布考试中的考试，只能修改可见用户
        // 已发布或考试中的考试，可以修改查看试卷规则： 考试结束后允许查看 或  交卷后允许查看
        if (ShelfStatusEnum.ON.getStatus() == existExam.getShelfStatus() && ExamStatusEnum.DURING_EXAM.getStatus() == existExam.getStatus()) {
            if (params.getUserVisible() == null) {
                throw new IllegalParamsException("可见用户不能为空");
            }
            this.lambdaUpdate().eq(Exam::getId, examId).set(Exam::getUserVisible, params.getUserVisible()).update();
            addExamUserVisible(params.getUserVisible(), params.getVisibleUserList(), existExam.getId());

            List<Long> ruleIdList = params.getRuleIdList();
            if (CollectionUtils.isNotEmpty(ruleIdList)) {
                // 保存查看试卷规则
                List<Rule> orgViewPaperAnswersRuleList = this.ruleService.lambdaQuery()
                        .eq(Rule::getOrgId, existExam.getOrgId())
                        .eq(Rule::getGroupCode, "view_paper_answers")
                        .list();
                List<Long> orgViewPaperAnswersRuleIdList = orgViewPaperAnswersRuleList.stream().map(Rule::getId).collect(Collectors.toList());
                this.examRuleService.lambdaUpdate().eq(ExamRule::getExamId, examId).in(ExamRule::getRuleId, orgViewPaperAnswersRuleIdList).remove();
                List<Long> viewPaperAnswersRuleIdList = ruleIdList.stream().filter(i -> orgViewPaperAnswersRuleIdList.contains(i)).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(viewPaperAnswersRuleIdList)) {
                    addExamRule(viewPaperAnswersRuleIdList, examId);
                }
            }

        } else {
            ValidateUtils.validateFailFast(params);
            checkParams(params);
            PaperDetailDTO paperDetailDTO = getAndCheckPaper(params.getPaperId(), params.getPassScore());

            //考试基本信息
            Exam exam = new Exam();
            BeanUtil.copyProperties(params, exam);
            exam.setPassScore(params.getPassScore());
            exam.setPaperScore(paperDetailDTO.getTotalScore());
            updateById(exam);

            //考试规则
            addExamRule(params.getRuleIdList(), examId);

            //考试可见人
            addExamUserVisible(params.getUserVisible(), params.getVisibleUserList(), exam.getId());

            //定时任务
            addExamTimingTask(exam);
        }

    }

    private void addExamTimingTask(Exam exam) {
        ReleaseModeEnum releaseModeEnum = ReleaseModeEnum.fromId(exam.getReleaseMode());
        switch (releaseModeEnum) {
            case CREATED_RELEASE:
                // 1: 创建时发布
                release(exam.getId(), true);
                break;
            case MANUAL_RELEASE:
                //  2 手动发布,
                break;
            case TIMED_RELEASE:
                //  3: 定时发布
                addOnShelfTimingTask(exam.getReleaseTime(), exam.getId());
                break;
            default:
                break;
        }

        //定时启动考试
        addStartExamTimingTask(exam.getStartTime(), exam.getId());

        //定时关闭考试
        jobService.addTimedEndExamTask(exam.getId(), exam.getEndTime());

    }


    @Override
    public void release(Long examId, boolean manual) {
        Exam exam = getById(examId);
        if (exam == null) {
            if (exam == null ) {
                log.info("考试发布({})，查不到考试", examId);
                if (manual) {
                    throw new ServiceException("查不到考试！");
                }
                return;
            }
        }

        // 已经是发布状态
        if (ShelfStatusEnum.ON.getStatus() == exam.getShelfStatus()) {
            return;
        }

        // 定时任务也会执行这里的代码，所以这里要判断是否手动发布
        // 编辑考试时间时并不会删除之前的定时任务，所以还要再对比一下考试设置的发布时间
        if (!manual) {
            if (exam.getReleaseTime() != null && ChronoUnit.SECONDS.between(LocalDateTime.now(), exam.getReleaseTime()) > START_JOB_ADVANCE_SECONDS) {
                return;
            }
        }

        this.lambdaUpdate().eq(Exam::getId, exam.getId())
                .set(Exam::getShelfStatus, ShelfStatusEnum.ON.getStatus())
//              .set(Exam::getEdited, Constants.DB_FAIL)
                .set(Exam::getReleaseTime, LocalDateTime.now())
                .set(Exam::getUpdateBy, 0)
                .set(Exam::getUpdateTime, LocalDateTime.now())
                .update();
    }


    @Override
    public void start(Long examId, boolean manual) {
        log.info("考试启动({}) {manual: {}}....", examId, manual);
        if (examId == null) {
            log.error("考试启动，examId为null");
            if (manual) {
                throw new IllegalParamsException("考试ID不能为空！");
            }
            return;
        }

        Exam exam = getById(examId);
        if (exam == null ) {
            log.error("考试启动({})，查不到考试", examId);
            if (manual) {
                throw new ServiceException("查不到考试！");
            }
            return;
        }
        if (ExamStatusEnum.NOT_START.getStatus() != exam.getStatus()) {
            log.error("考试启动({})，考试状态异常-->{status: {}}", examId, exam.getStatus());
            if (manual) {
                throw new ServiceException("当前考试已经启动过！");
            }
            return;
        }

        if (!manual) {
            // 当前时间早于考试开始时间60秒，则不启动（可能是后来又更改了考试开始时间）
            long seconds = ChronoUnit.SECONDS.between(exam.getStartTime(), LocalDateTime.now());
            if (seconds <= -60) {
                log.error("考试启动({})，当前时间早于考试开始时间({}) {} s", examId, exam.getStartTime(), seconds);
                return;
            }
        }

        String lockKey = RedisLockKeyUtil.getStartExamLockKey(examId);
        RLock rLock = redissonClient.getFairLock(lockKey);
        try {
            rLock.lock(10, TimeUnit.SECONDS);
            exam = getById(examId);
            if (ExamStatusEnum.NOT_START.getStatus() != exam.getStatus()) {
                if (manual) {
                    throw new ServiceException("当前考试已经启动过！");
                }
                return;
            }

            // 如果是自动启动考试，且修改过考试开始时间，不会删除之前的定时任务，所以这里需要再次判断一下
            if (!manual && LocalDateTime.now().isBefore(exam.getStartTime())
                    && ChronoUnit.SECONDS.between(LocalDateTime.now(), exam.getStartTime()) <= START_JOB_ADVANCE_SECONDS) {
                log.error("考试启动({})，考试时间异常-->{endTime: {}, now: {}}", examId, exam.getEndTime(), LocalDateTime.now());
                return;
            }

            log.info("考试启动({})，更新考试状态-->{status: {}}", examId, ExamStatusEnum.EXAM_ENDED.getStatus());
            this.lambdaUpdate().eq(Exam::getId, exam.getId())
                    .set(Exam::getStatus, ExamStatusEnum.DURING_EXAM.getStatus())
                    .set(Exam::getStartWay, manual ? StartEndWayEnum.MANUAL.getId() : StartEndWayEnum.AUTOMATIC.getId())
                    .set(manual, Exam::getStartTime, LocalDateTime.now())
                    .update();
        } finally {
            if (rLock != null && rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }

    }

    @Override
    public Boolean release(List<Long> examIdList, boolean manual) {
        examIdList.forEach(examId -> this.release(examId, manual));
        return true;
    }

    @Override
    public void revokeRelease(Long examId, boolean manual) {
        Exam exam = getById(examId);
        if (exam == null) {
            if (exam == null ) {
                log.info("考试撤销发布({})，查不到考试", examId);
                if (manual) {
                    throw new ServiceException("查不到考试！");
                }
                return;
            }
        }

        // 已经是未发布状态
        if (ShelfStatusEnum.OFF.getStatus() == exam.getShelfStatus()) {
            return;
        }

        this.lambdaUpdate().eq(Exam::getId, exam.getId())
                .set(Exam::getShelfStatus, ShelfStatusEnum.OFF.getStatus())
                .set(Exam::getUpdateBy, 0)
                .set(Exam::getUpdateTime, LocalDateTime.now())
                .update();
    }

    @Override
    public Boolean revokeRelease(List<Long> examIdList, boolean manual) {
        examIdList.forEach(examId -> this.revokeRelease(examId, manual));
        return true;
    }

    @Override
    public void end(Long examId, boolean manual) {
        log.info("考试结束({}) {manual: {}}....", examId, manual);
        if (examId == null) {
            log.error("考试结束，examId为null");
            if (manual) {
                throw new IllegalParamsException("考试ID不能为空！");
            }
            return;
        }

        Exam exam = this.getById(examId);
        if (exam == null ) {
            log.error("考试结束({})，查不到考试", examId);
            if (manual) {
                throw new ServiceException("查不到考试！");
            }
            return;
        }
        if (ExamStatusEnum.DURING_EXAM.getStatus() != exam.getStatus()) {
            log.error("考试结束({})，考试状态异常-->{status: {}}", examId, exam.getStatus());
            if (manual) {
                throw new ServiceException("当前考试不在考试中！");
            }
            return;
        }

        if (!manual) {
            // 当前时间早于考试截至时间60秒，则不停止（可能是后来又更改了考试截至时间）
            long seconds = ChronoUnit.SECONDS.between(exam.getEndTime(), LocalDateTime.now());
            if (seconds <= -60) {
                log.error("考试结束({})，当前时间早于考试截至时间({}) {} s", examId, exam.getEndTime(), seconds);
                return;
            }
        }

        String lockKey = RedisLockKeyUtil.getStartExamLockKey(examId);
        RLock rLock = redissonClient.getFairLock(lockKey);
        try {
            rLock.lock(10, TimeUnit.SECONDS);
            exam = this.getById(examId);
            if (ExamStatusEnum.DURING_EXAM.getStatus() != exam.getStatus()) {
                log.info("考试结束({})，考试状态异常-->{status: {}}", examId, exam.getStatus());
                if (manual) {
                    throw new ServiceException("当前考试不在考试中！");
                }
                return;
            }

            // 如果后面修改了考试结束时间，不会删除之前的定时任务，所以这里需要再次判断一下
            if (!manual && LocalDateTime.now().isBefore(exam.getEndTime())) {
                log.info("考试结束({})，考试时间异常-->{endTime: {}, now: {}}", examId, exam.getEndTime(), LocalDateTime.now());
                return;
            }

            log.info("考试结束({})，更新考试状态-->{status: {}}", examId, ExamStatusEnum.EXAM_ENDED.getStatus());
            this.lambdaUpdate().eq(Exam::getId, exam.getId())
                    .set(Exam::getStatus, ExamStatusEnum.EXAM_ENDED.getStatus())
                    .set(Exam::getEndWay, manual ? StartEndWayEnum.MANUAL.getId() : StartEndWayEnum.AUTOMATIC.getId())
                    .set(manual, Exam::getEndTime, LocalDateTime.now())
                    .set(Exam::getEdited, Constants.DB_FAIL)
                    .update();

            log.info("考试结束({})，查询未交卷的考试记录....", examId);
            List<ExamineeRecord> examineeRecordList = this.examineeRecordMapper.selectUnsubmitListByExamId(examId);
            if (CollectionUtils.isNotEmpty(examineeRecordList)) {
                log.info("结束考试({})，自动交卷，examineeRecordList: {}", examId, examineeRecordList);
                for (ExamineeRecord examineeRecord : examineeRecordList) {
                    if (examineeRecord != null) {
                        log.info("考试结束，自动交卷 {}", JSON.toJSONString(examineeRecord));
                        this.submitPaper(examineeRecord);
                    }
                }
            } else {
                log.info("考试结束({})，未交卷考试查询结果为空", examId);
            }
        } finally {
            if (rLock != null && rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }
    }

    public void submitPaper(ExamineeRecord examineeRecord) {
        log.info("考试交卷: {}", JSON.toJSONString(examineeRecord));
        if (examineeRecord == null) {
            log.info("考试交卷，考试记录不存在");
            throw new ServiceException("考试记录不存在！");
        }
        Long examineeRecordId = examineeRecord.getId();
        if (examineeRecord.getSubmitTime() != null) {
            log.info("考试交卷({})，考试交卷试卷不为null", examineeRecordId);
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

        log.info("考试交卷({})，发送交卷消息", examineeRecordId);
        tdmqProducerTemplate.sendAsyncStringMsg(Topics.SUBMIT_PAPER, String.valueOf(examineeRecordId));
    }

    @Override
    public boolean delete(Long examId) {
        Exam exam = this.getById(examId);
        if (exam == null) {
            throw new ServiceException("查不到考试！");
        }
        if (EnabledEnum.DISABLE.getValue().equals(exam.getEnabled())) {
            return true;
        }

        Long examineeCount = this.examineeService.lambdaQuery().eq(Examinee::getExamId, examId).count();
        if (examineeCount != null && examineeCount > 0) {
            throw new ServiceException("当前考试已有考生参加，不能删除！");
        }

        return this.lambdaUpdate().eq(Exam::getId, examId)
                .set(Exam::getEnabled, false)
                .set(Exam::getShelfStatus, ShelfStatusEnum.OFF.getStatus())
                .set(ExamStatusEnum.DURING_EXAM.getStatus() == exam.getStatus(), Exam::getStatus, ExamStatusEnum.EXAM_ENDED.getStatus())
                .set(Exam::getUpdateBy, SecurityContextHolder.getUserId())
                .set(Exam::getUpdateTime, LocalDateTime.now())
                .update();
    }

    @Override
    public void autoCorrect(Long examId) {
        log.info("autoCorrect...");
        if (examId == null) {
            throw new IllegalParamsException("考试ID不能为空！");
        }
        Exam exam = this.getById(examId);
        if (exam == null) {
            throw new IllegalParamsException("差不到考试！");
        }
        if (ExamStatusEnum.NOT_START.getStatus() == exam.getStatus()) {
            throw new ServiceException("考试未开始，不能批阅！");
        }

        List<ExamineeRecord> examineeRecordList = examineeRecordMapper.selectByExamId(examId);
        List<Long> examineeRecordIdList = examineeRecordList.stream().map(i -> i.getId()).collect(Collectors.toList());
        log.info("examineeRecordIdList: {}", examineeRecordIdList);
        if (CollectionUtils.isNotEmpty(examineeRecordList)) {
            for (ExamineeRecord examineeRecord : examineeRecordList) {
                tdmqProducerTemplate.sendAsyncStringMsg(Topics.SUBMIT_PAPER, String.valueOf(examineeRecord.getId()));
            }
        }
    }

    private void addOnShelfTimingTask(LocalDateTime releaseTime, Long examId) {
        LocalDateTime now = LocalDateTime.now();
        if (ChronoUnit.SECONDS.between(now, releaseTime) <= START_JOB_ADVANCE_SECONDS) {
            // 现在时间距发布时间相差10秒内，立即启动，不再走xxl-job。
            jobService.addTimedOnShelfExamTask(examId, releaseTime.plusSeconds(START_JOB_DELAY_SECONDS));
        } else {
            // 创建定时任务
            jobService.addTimedOnShelfExamTask(examId, releaseTime);
        }
    }

    private void addStartExamTimingTask(LocalDateTime startTime, Long examId) {
        LocalDateTime now = LocalDateTime.now();
        if (ChronoUnit.SECONDS.between(now, startTime) <= START_JOB_ADVANCE_SECONDS) {
            // 现在时间距开始时间相差10秒内，立即开始，不再走xxl-job。
            jobService.addTimedStartExamTask(examId, startTime.plusSeconds(START_JOB_DELAY_SECONDS));
        } else {
            // 创建定时任务
            jobService.addTimedStartExamTask(examId, startTime);
        }
    }


    private void addExamUserVisible(Integer userVisible, List<VisibleUser> visibleUserList, Long examId) {
        if (UserVisibleStatusEnum.ALL.getCode().equals(userVisible)){
            throw new ServiceException("不能设置为所有用户可见！");
        }
        if (UserVisibleStatusEnum.SOME.getCode().equals(userVisible) && CollectionUtils.isNotEmpty(visibleUserList)) {
//            if (CollectionUtils.isEmpty(visibleUserList)) {
//                throw new ServiceException("可见用户列表不能为空！");
//            }
            List<ExamUserVisible> userVisibleList = this.examUserVisibleService.list(new LambdaQueryWrapper<ExamUserVisible>().select(ExamUserVisible::getId).eq(ExamUserVisible::getExamId, examId));
            if(CollUtil.isNotEmpty(userVisibleList)){
                this.examUserVisibleService.remove(new LambdaQueryWrapper<ExamUserVisible>().in(ExamUserVisible::getId,userVisibleList.stream().map(ExamUserVisible::getId).collect(Collectors.toList())));
            }
            List<ExamUserVisible> examUserVisibleList = visibleUserList.stream().distinct().map(user -> {
                ExamUserVisible examUserVisible = new ExamUserVisible();
                examUserVisible.setExamId(examId);
                examUserVisible.setUserId(user.getUserId());
                examUserVisible.setEmployeeId(user.getEmployeeId());
                examUserVisible.setStudentId(user.getStudentId());
                return examUserVisible;
            }).collect(Collectors.toList());
            this.examUserVisibleService.saveBatch(examUserVisibleList);
        }
    }

    private void addExamRule(List<Long> ruleIdList, Long examId) {
        List<ExamRule> examRuleList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(ruleIdList)) {
            List<ExamRule> ruleList = ruleIdList.stream().distinct().map(ruleId -> {
                ExamRule examRule = new ExamRule();
                examRule.setExamId(examId);
                examRule.setRuleId(ruleId);
                return examRule;
            }).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(ruleList)) {
                examRuleList.addAll(ruleList);
            }
        }

        this.examRuleService.remove(new LambdaQueryWrapper<ExamRule>().eq(ExamRule::getExamId, examId));
        if (CollectionUtils.isNotEmpty(examRuleList)) {
            this.examRuleService.saveBatch(examRuleList);
        }

    }

    private PaperDetailDTO getAndCheckPaper(Long paperId, BigDecimal passScore) {
        PaperDetailDTO paperDetail = paperService.getPaperDetail(paperId);
        if (paperDetail == null) {
            throw new ServiceException("查不到试卷！");
        }
        BigDecimal totalScore = paperDetail.getTotalScore();
        if (passScore != null && passScore.compareTo(totalScore) > 0) {
            throw new ServiceException("当前试卷总分" + totalScore + "分，通过分数设置要小于试卷总分！");
        }
        return paperDetail;
    }


    private void checkParams(ExamAddRequest params) {
        if (ExamModeEnum.ADVANCED.getMode() == params.getExamMode()) {
            if (params.getAdmissionStartTime() == null) {
                throw new IllegalParamsException("高级模式下考试入场开始时间不能为空！");
            }
            if (params.getAdmissionEndTime() == null) {
                throw new IllegalParamsException("高级模式下考试入场截止时间不能为空！");
            }
            if (params.getAdmissionStartTime().isBefore(LocalDateTime.now())) {
                throw new IllegalParamsException("考试入场开始时间不能早于当前时间！");
            }

            if (!params.getAdmissionStartTime().isBefore(params.getAdmissionEndTime())) {
                throw new IllegalParamsException("考试入场截止时间不能早于或等于考试入场开始时间！");
            }
            if (params.getDuration() == null) {
                throw new IllegalParamsException("高级模式下考试时长不能为空！");
            }
            if (params.getDuration().compareTo(1440) > 0) {
                throw new IllegalParamsException("考试时长不能超过1440分钟！");
            }
            params.setStartTime(params.getAdmissionStartTime());
            params.setEndTime(params.getAdmissionEndTime().plusMinutes(params.getDuration()));

        } else {
            if (params.getStartTime() == null) {
                throw new IllegalParamsException("普通模式下考试开始时间不能为空！");
            }
            if (params.getEndTime() == null) {
                throw new IllegalParamsException("普通模式下考试结束时间不能为空！");
            }
            if (params.getStartTime().isBefore(LocalDateTime.now())) {
                throw new IllegalParamsException("考试开始时间不能早于当前时间！");
            }

            if (!params.getStartTime().isBefore(params.getEndTime())) {
                throw new IllegalParamsException("考试截止时间不能早于或等于开始时间！");
            }
            params.setAdmissionStartTime(params.getStartTime());
            params.setAdmissionEndTime(params.getEndTime());
        }

        if (params.getPassScore() != null && params.getPassScore().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalParamsException("考试通过分数不能小于或等于0！");
        }

        if (ExamModeEnum.STANDARD.getMode() == params.getExamMode()) {
            params.setDuration(null);
            params.setLimitCount(1);
        }

        if (ReleaseModeEnum.TIMED_RELEASE.getId() == params.getReleaseMode()) {
            LocalDateTime releaseTime = params.getReleaseTime();
            if (releaseTime == null) {
                throw new IllegalParamsException("考试定时发布，发布时间不能为空！");
            }
            if (releaseTime.isBefore(LocalDateTime.now())) {
                throw new IllegalParamsException("发布时间不能早于当前时间！");
            }
        }
    }


}
