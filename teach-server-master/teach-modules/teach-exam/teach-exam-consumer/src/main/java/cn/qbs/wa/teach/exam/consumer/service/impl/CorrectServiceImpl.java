package cn.qbs.wa.teach.exam.consumer.service.impl;

import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdOrgRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.exam.common.correct.corrector.Corrector;
import cn.qbs.wa.teach.exam.common.correct.corrector.CorrectorFactory;
import cn.qbs.wa.teach.exam.common.entity.Exam;
import cn.qbs.wa.teach.exam.common.entity.Examinee;
import cn.qbs.wa.teach.exam.common.entity.ExamineeRecord;
import cn.qbs.wa.teach.exam.common.entity.ExamineeRecordQuestion;
import cn.qbs.wa.teach.exam.common.enumerate.ExamineeRecordStatusEnum;
import cn.qbs.wa.teach.exam.consumer.handler.ExamCertHandler;
import cn.qbs.wa.teach.exam.consumer.mapper.ExamineeRecordMapper;
import cn.qbs.wa.teach.exam.consumer.service.*;
import cn.qbs.wa.teach.notification.api.RemoteNotificationService;
import cn.qbs.wa.teach.notification.api.enums.NotificationBusinessEnum;
import cn.qbs.wa.teach.notification.api.enums.RepeatCheckEnum;
import cn.qbs.wa.teach.notification.api.pojo.DTO.notification.SendNotificationDTO;
import cn.qbs.wa.teach.notification.api.pojo.DTO.notification.SendNotificationUserInfoDTO;
import cn.qbs.wa.teach.question.api.RemoteQuestionService;
import cn.qbs.wa.teach.question.api.pojo.DTO.question.QuestionDetailsDTO;
import com.alibaba.fastjson.JSON;
import com.qbs.tdmq.producer.TdmqProducerTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zcm
 * @Date 2021-12-22 17:53
 * @Version 1.0
 */
@Slf4j
@Service
@RefreshScope
@RequiredArgsConstructor
public class CorrectServiceImpl implements CorrectService {

    /**
     * 查看考试结果Url
     */
    @Value("${system.viewExamResultUrl:/#/Exam/NewResult?id=%d}")
    private String viewExamResultUrl;

    private final CorrectorFactory correctorFactory;

    private final ExamineeRecordService examineeRecordService;

    private final ExamineeRecordQuestionService examineeRecordQuestionService;

    private final RemoteQuestionService remoteQuestionService;

    private final ExamineeRecordMapper examineeRecordMapper;

    private final TdmqProducerTemplate tdmqProducerTemplate;

    private final ExamCertHandler examCertHandler;

    @Resource
    private RemoteNotificationService remoteNotificationService;

    @Resource
    private ExamineeService examineeService;

    @Resource
    private ExamService examService;


    public void correct(ExamineeRecordQuestion examineeRecordQuestion) {
        Long questionId = examineeRecordQuestion.getQuestionId();
        R<QuestionDetailsDTO> r;
        try {
            r = remoteQuestionService.questionDetail(new IdOrgRequest(questionId, examineeRecordQuestion.getOrgId()));
        } catch (Exception e) {
            log.error("调用题库服务获取试题异常！");
            e.printStackTrace();
            throw e;
        }

        QuestionDetailsDTO questionDetails = r.getData();
        if (!r.isOk() || questionDetails == null) {
            log.error("调用题库服务获取试题失败, {questionId： {}, msg: {}}", questionId, r.getMsg());
            throw new ServiceException("调用题库服务获取试题失败！");
        }

        Corrector corrector = correctorFactory.getCorrector(questionDetails.getQuestionTypeId());
        if (corrector != null) {
            BigDecimal score = corrector.correct(examineeRecordQuestion, questionDetails.getAnswer());
            if (score != null) {
                examineeRecordQuestion.setScore(score);
                examineeRecordQuestion.setCorrectStatus(1);
                examineeRecordQuestion.setCorrectBy(-1L);
                examineeRecordQuestion.setRemark("自动批改");
                // 判断是否得满分
                if (score.compareTo(examineeRecordQuestion.getQuestionScore()) == 0) {
                    // 批改结果：对
                    examineeRecordQuestion.setCorrectResult(1);
                } else {
                    // 批改结果：错
                    examineeRecordQuestion.setCorrectResult(0);
                }

                this.examineeRecordQuestionService.updateById(examineeRecordQuestion);
            }
        }
    }

    public void correct(List<ExamineeRecordQuestion> examineeRecordQuestionList) {
        for (ExamineeRecordQuestion examineeRecordQuestion : examineeRecordQuestionList) {
            this.correct(examineeRecordQuestion);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void correct(Long examineeRecordId) {
        ExamineeRecord examineeRecord = examineeRecordService.getById(examineeRecordId);
        if (examineeRecord != null && examineeRecord.getStatus().equals(1)) {
            List<ExamineeRecordQuestion> examineeRecordQuestionList = examineeRecordQuestionService.lambdaQuery()
                    .eq(ExamineeRecordQuestion::getExamineeRecordId, examineeRecordId)
                    .list();

            this.correct(examineeRecordQuestionList);

            Long uncorrectedQuestionCount = this.examineeRecordQuestionService.selectUncorrectedQuestionCount(examineeRecordId);
            if (uncorrectedQuestionCount == null || uncorrectedQuestionCount == 0) {
                // 已批改
                examineeRecord.setStatus(ExamineeRecordStatusEnum.CORRECTED.getStatus());
                BigDecimal score = examineeRecordMapper.selectScore(examineeRecordId);
                examineeRecord.setScore(score);
                this.examineeRecordService.updateById(examineeRecord);

                // 发送站内信
                sendExamCorrectNotification(examineeRecord);
            }

            this.examineeRecordService.updateCalculated(examineeRecord.getExamineeId());
            awardCert(examineeRecord.getExamineeId(),examineeRecord.getOrgId());
            /*Map<String, Object> data = new HashMap<>(2);
            data.put("examineeId", examineeRecord.getExamineeId());
            data.put("orgId", SecurityContextHolder.getOrgId());
            tdmqProducerTemplate.sendAsyncStringMsg(Topics.ISSUING_OF_CERTIFICATES,  JSON.toJSONString(data));*/
        }
    }

    private void sendExamCorrectNotification(ExamineeRecord examineeRecord) {
        log.info("发送考试批阅完成站内信: {examineeRecord: {}}", examineeRecord);
        if (examineeRecord != null) {
            Examinee examinee = examineeService.getById(examineeRecord.getExamineeId());
            Exam exam = examService.getById(examinee.getExamId());

            SendNotificationDTO params = new SendNotificationDTO();
            Long orgId = examineeRecord.getOrgId();
            params.setOrgId(orgId);
            params.setBusinessType(NotificationBusinessEnum.CORRECT_EXAM);
            Long examineeRecordId = examineeRecord.getId();
            params.setBusinessId(examineeRecordId);
            Map<String, String> templateKey = new HashMap<>(2);
            templateKey.put("examName", exam.getExamName());
            templateKey.put("url", String.format(viewExamResultUrl, examineeRecordId));
            params.setTemplateKey(templateKey);
            List<SendNotificationUserInfoDTO> userInfoList = new ArrayList<>();
            SendNotificationUserInfoDTO sendNotificationUserInfoDTO = new SendNotificationUserInfoDTO();
            sendNotificationUserInfoDTO.setUserId(examinee.getUserId());
            userInfoList.add(sendNotificationUserInfoDTO);
            params.setUserInfo(userInfoList);
            params.setRepeatCheckEnum(RepeatCheckEnum.OPEN);
            log.info("发送考试批阅完成站内信参数: {}", JSON.toJSONString(params));
            new Thread(() -> remoteNotificationService.sendCommonNotification(params, "inner")).start();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void afterManualCorrect(Long examineeRecordQuestionId, Long orgId) {
        SecurityContextHolder.setSelectOrgId(String.valueOf(orgId));
        ExamineeRecord examineeRecord = this.examineeRecordMapper.selectByExamineeRecordQuestionId(examineeRecordQuestionId);
        if (examineeRecord == null) {
            log.error("查不到考试记录: {examineeRecordQuestionId: {}, orgId: {}}", examineeRecordQuestionId, orgId);
            return;
        }
        // 查询未批改的试题数量
        Long examineeRecordId = examineeRecord.getId();
        Long uncorrectedQuestionCount = this.examineeRecordQuestionService.selectUncorrectedQuestionCount(examineeRecordId);
        if (uncorrectedQuestionCount != null && uncorrectedQuestionCount > 0) {
            log.info("还有{}道试题未批改，不计算得分！", uncorrectedQuestionCount);
            return;
        }

        // 已批改
        examineeRecord.setStatus(ExamineeRecordStatusEnum.CORRECTED.getStatus());
        BigDecimal score = examineeRecordMapper.selectScore(examineeRecordId);
        examineeRecord.setScore(score);
        this.examineeRecordService.updateById(examineeRecord);
        this.examineeRecordService.updateCalculated(examineeRecord.getExamineeId());
        awardCert(examineeRecord.getExamineeId(),orgId);

        // 发送站内信
        sendExamCorrectNotification(examineeRecord);

        /*Map<String, Object> data = new HashMap<>(2);
        data.put("examineeId", examineeRecord.getExamineeId());
        data.put("orgId", orgId);
        tdmqProducerTemplate.sendAsyncStringMsg(Topics.ISSUING_OF_CERTIFICATES,  JSON.toJSONString(data));*/
    }

    @Async
    void awardCert(Long examineeId, Long orgId) {
        //调用证书
        examCertHandler.awardCert(examineeId,orgId);
    }

}
