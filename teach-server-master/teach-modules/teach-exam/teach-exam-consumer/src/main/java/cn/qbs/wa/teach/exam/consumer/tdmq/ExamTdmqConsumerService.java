package cn.qbs.wa.teach.exam.consumer.tdmq;

import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.exam.common.constant.Topics;
import cn.qbs.wa.teach.exam.common.util.DateUtil;
import cn.qbs.wa.teach.exam.consumer.handler.ExamCertHandler;
import cn.qbs.wa.teach.exam.consumer.service.CorrectService;
import cn.qbs.wa.teach.exam.consumer.service.ExamService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qbs.tdmq.annotation.TdmqConsumer;
import com.qbs.tdmq.constant.Serialization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 考试TDMQ消费者类
 * @Author zcm
 * @Date 2021-12-22 14:32
 * @Version 1.0
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "tdmq.consumerOn", havingValue = "true")
@RequiredArgsConstructor
public class ExamTdmqConsumerService {

    private final ExamService examService;

    private final CorrectService correctService;

    private final ExamCertHandler examCertHandler;

    @TdmqConsumer(topic= Topics.ADD_EXAM_QUESTIONS, serialization = Serialization.STRING, subscriptionName = "exam")
    public void onAddExamQuestions(String msg) throws Exception {
        log.info("调用学员创建考题成功");
        log.info("{}-{} TDMQ收到添加考题消息 msg: {}", Topics.ADD_EXAM_QUESTIONS, DateUtil.nowDateStr(), msg);
        try {
            JSONObject jsonObject = JSON.parseObject(msg);
            Long examId = jsonObject.getLong("examId");
            Long examineeId = jsonObject.getLong("examineeId");
            Long orgId = jsonObject.getLong("orgId");
            Long userId = jsonObject.getLong("userId");
//            if (examId.equals(132L)) return;
            examService.addExamQuestions(examId, examineeId, orgId, userId);
        } catch (Exception e) {
            log.error("添加考题消息处理失败: {}", msg, e);
            throw new ServiceException(String.format("TDMQ消费失败 msg: %s", msg));
        }
    }


    @TdmqConsumer(topic= Topics.SUBMIT_PAPER, serialization = Serialization.STRING, subscriptionName = "exam")
    public void onSubmitPaper(String msg) throws Exception {
        log.info("{}-{} TDMQ收到交卷消息 msg: {}", Topics.SUBMIT_PAPER, DateUtil.nowDateStr(), msg);
        try {
            Long examineeRecordId = Long.parseLong(msg);
            correctService.correct(examineeRecordId);
        } catch (Exception e) {
            log.error("交卷消息处理失败: {}", msg);
            log.error("交卷消息处理失败:", e);
            e.printStackTrace();
            throw new ServiceException(String.format("TDMQ消费失败 msg: %s", msg));
        }
    }


    /**
     * 人工批改处理
     * @param msg
     * @throws Exception
     */
    @TdmqConsumer(topic= Topics.MANUAL_CORRECT, serialization = Serialization.STRING, subscriptionName = "exam")
    public void onManualCorrect(String msg) throws Exception {
        log.info("{}-{} TDMQ收到人工批改消息 msg: {}", Topics.MANUAL_CORRECT, DateUtil.nowDateStr(), msg);
        try {
            JSONObject jsonObject = JSON.parseObject(msg);
            Long examineeRecordQuestionId = jsonObject.getLong("examineeRecordQuestionId");
            Long orgId = jsonObject.getLong("orgId");
            correctService.afterManualCorrect(examineeRecordQuestionId, orgId);
        } catch (Exception e) {
            log.error("人工批改消息处理失败: {}", msg);
            log.error("人工批改消息处理失败:", e);
            e.printStackTrace();
            throw new ServiceException(String.format("TDMQ消费失败 msg: %s", msg));
        }
    }

    /**
     * 自动颁发证书
     */
    @TdmqConsumer(topic= Topics.ISSUING_OF_CERTIFICATES, serialization = Serialization.STRING, subscriptionName = "exam")
    public void awardCert(String msg) throws Exception {
        log.info("{}-{} TDMQ收到考试模块的证书颁发消息 msg: {}", Topics.ISSUING_OF_CERTIFICATES, DateUtil.nowDateStr(), msg);
        try {
            JSONObject jsonObject = JSON.parseObject(msg);
            Long examineeId = jsonObject.getLong("examineeId");
            Long orgId = jsonObject.getLong("orgId");
            SecurityContextHolder.setSelectOrgId(String.valueOf(orgId));
            examCertHandler.awardCert(examineeId,orgId);
        } catch (Exception e) {
            log.error("人工批改消息处理失败: {}", msg);
            log.error("人工批改消息处理失败: ", e);
            e.printStackTrace();
            throw new ServiceException(String.format("TDMQ消费失败 msg: %s", msg));
        }
    }

//    @PostConstruct
//    public void test() {
//        log.info("ExamTdmqConsumerService 初始化完成...");
//        correctService.afterManualCorrect(6570L, 66L);
//    }
}
