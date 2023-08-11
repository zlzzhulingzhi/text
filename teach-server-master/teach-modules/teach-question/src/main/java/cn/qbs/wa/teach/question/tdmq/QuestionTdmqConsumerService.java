package cn.qbs.wa.teach.question.tdmq;

import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.question.service.QuestionSearchService;
import cn.qbs.wa.teach.question.service.QuestionService;
import cn.qbs.wa.teach.question.util.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.qbs.tdmq.annotation.TdmqConsumer;
import com.qbs.tdmq.constant.Serialization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 试题TDMQ消费者类
 * @Author zcm
 * @Date 2021-12-09 13:56
 * @Version 1.0
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "tdmq.consumerOn", havingValue = "true")
@RequiredArgsConstructor
public class QuestionTdmqConsumerService {

    private final QuestionService questionService;

    private final QuestionSearchService questionSearchService;


    @TdmqConsumer(topic= Topics.QUESTION_UPDATE, serialization = Serialization.STRING, subscriptionName = "question")
    public void questionUpdate(String msg) throws Exception {
        log.info("{}-{} TDMQ收到试题更新消息 msg: {}", Topics.QUESTION_UPDATE, DateUtil.nowDateStr(), msg);
        try {
            JSONObject jsonObject = JSONObject.parseObject(msg);
            Long questionId = jsonObject.getLong("questionId");
            Long orgId = jsonObject.getLong("orgId");
            log.info("questionId: {}", questionId);
            questionService.saveEsIndex(questionId, orgId);

        } catch (Exception e) {
            log.error("试题更新消息处理失败: " + msg, e);
            e.printStackTrace();
            throw new ServiceException(String.format("TDMQ消费失败 msg: %s", msg));
        }
    }


    @TdmqConsumer(topic= Topics.QUESTION_DELETE, serialization = Serialization.STRING, subscriptionName = "question")
    public void questionDelete(String msg) throws Exception {
        log.info("{}-{} TDMQ收到试题删除消息 msg: {}", Topics.QUESTION_DELETE, DateUtil.nowDateStr(), msg);
        try {
            JSONObject jsonObject = JSON.parseObject(msg);
            List<Long> questionIdList = JSON.parseArray(jsonObject.getString("questionIdList"), Long.class);
            if (CollectionUtils.isNotEmpty(questionIdList)) {
                questionSearchService.batchDelete(questionIdList);
            }

        } catch (Exception e) {
            log.error("试题删除消息处理失败: {}，异常信息： {}", msg);
            e.printStackTrace();
            throw new ServiceException(String.format("TDMQ消费失败 msg: %s", msg));
        }
    }

}
