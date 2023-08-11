package cn.qbs.wa.teach.question.tdmq;

import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.question.service.ElasticSearchPaperService;
import cn.qbs.wa.teach.question.service.PaperService;
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
public class PaperTdmqConsumerService {

    private final PaperService paperService;

    private final ElasticSearchPaperService paperSearchService;


    @TdmqConsumer(topic= Topics.PAPER_UPDATE, serialization = Serialization.STRING, subscriptionName = "question")
    public void paperUpdate(String msg) throws Exception {
        log.info("{}-{} TDMQ收到试卷更新消息 msg: {}", Topics.PAPER_UPDATE, DateUtil.nowDateStr(), msg);
        try {
            JSONObject jsonObject = JSONObject.parseObject(msg);
            Long paperId = jsonObject.getLong("paperId");
            Long orgId = jsonObject.getLong("orgId");
            log.info("paperId: {}", paperId);
            paperService.saveEsIndex(paperId, orgId);

        } catch (Exception e) {
            log.error("试卷更新消息处理失败: " + msg , e);
            e.printStackTrace();
            throw new ServiceException(String.format("TDMQ消费失败 msg: %s", msg));
        }
    }


    @TdmqConsumer(topic= Topics.PAPER_DELETE, serialization = Serialization.STRING, subscriptionName = "question")
    public void paperDelete(String msg) throws Exception {
        log.info("{}-{} TDMQ收到试卷删除消息 msg: {}", Topics.PAPER_DELETE, DateUtil.nowDateStr(), msg);
        try {
            JSONObject jsonObject = JSON.parseObject(msg);
            List<Long> paperIdList = JSON.parseArray(jsonObject.getString("paperIdList"), Long.class);
            if (CollectionUtils.isNotEmpty(paperIdList)) {
                paperSearchService.batchDelete(paperIdList);
            }

        } catch (Exception e) {
            log.error("试卷删除消息处理失败: {}", msg);
            e.printStackTrace();
            throw new ServiceException(String.format("TDMQ消费失败 msg: %s", msg));
        }
    }

}
