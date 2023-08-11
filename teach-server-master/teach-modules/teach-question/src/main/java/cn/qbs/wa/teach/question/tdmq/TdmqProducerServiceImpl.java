package cn.qbs.wa.teach.question.tdmq;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.question.service.MqProducerService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.qbs.tdmq.producer.TdmqProducerTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author zcm
 * @Date 2021/12/9 08:45
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TdmqProducerServiceImpl implements MqProducerService {

    private final TdmqProducerTemplate tdmqProducerTemplate;


    @Override
    public void sendQuestionUpdateMsg(Long questionId, Long orgId) {
        if (questionId == null) {
            throw new IllegalParamsException("questionId 不能为空！");
        }
        if (orgId == null) {
            throw new IllegalParamsException("orgId 不能为空！");
        }

        try {
            Map<String, Object> data = new HashMap<>(2);
            data.put("questionId", questionId);
            data.put("orgId", orgId);
            tdmqProducerTemplate.sendAsyncStringMsg(Topics.QUESTION_UPDATE, JSON.toJSONString(data));
        } catch (Exception e) {
            log.info("发送试题更新消息异常--> {questionId: {}, orgId: {}}", questionId, orgId);
            e.printStackTrace();
        }
    }

    @Override
    public void sendQuestionDeleteMsg(List<Long> questionIdList) {
        if (CollectionUtils.isEmpty(questionIdList)) {
            throw new IllegalParamsException("questionIdList 不能为空！");
        }

        try {
            Map<String, Object> data = new HashMap<>(1);
            data.put("questionIdList", questionIdList);
            tdmqProducerTemplate.sendAsyncStringMsg(Topics.QUESTION_DELETE, JSON.toJSONString(data));
        } catch (Exception e) {
            log.info("发送试题删除消息异常--> {questionIdList: {}}", questionIdList);
            e.printStackTrace();
        }
    }

    @Override
    public void sendPaperUpdateMsg(Long paperId, Long orgId) {
        if (paperId == null) {
            throw new IllegalParamsException("paperId 不能为空！");
        }
        if (orgId == null) {
            throw new IllegalParamsException("orgId 不能为空！");
        }

        try {
            Map<String, Object> data = new HashMap<>(2);
            data.put("paperId", paperId);
            data.put("orgId", orgId);
            tdmqProducerTemplate.asyncSendDelayJsonMsg(Topics.PAPER_UPDATE, JSON.toJSONString(data), 1L, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.info("发送试卷更新消息异常--> {paperId: {}, orgId: {}}", paperId, orgId);
            e.printStackTrace();
        }
    }

    @Override
    public void sendPaperDeleteMsg(List<Long> paperIdList) {
        if (CollectionUtils.isEmpty(paperIdList)) {
            throw new IllegalParamsException("paperIdList 不能为空！");
        }

        try {
            Map<String, Object> data = new HashMap<>(1);
            data.put("paperIdList", paperIdList);
            tdmqProducerTemplate.sendAsyncStringMsg(Topics.PAPER_DELETE, JSON.toJSONString(data));
        } catch (Exception e) {
            log.info("发送试题删除消息异常--> {paperIdList: {}}", paperIdList);
            e.printStackTrace();
        }
    }

    @Override
    public void sendDisableCategoryMsg(List<Long> categoryIdList, Long orgId, Integer groupId) {
        if (categoryIdList == null) {
            throw new IllegalParamsException("categoryIdList 不能为空！");
        }
        if (orgId == null) {
            throw new IllegalParamsException("orgId 不能为空！");
        }
        if (groupId == null) {
            throw new IllegalParamsException("groupId 不能为空！");
        }

        try {
            Map<String, Object> data = new HashMap<>(2);
            data.put("categoryIdList", categoryIdList);
            data.put("orgId", orgId);
            data.put("groupId", groupId);
            tdmqProducerTemplate.sendAsyncStringMsg(Topics.DISABLE_CATEGORY, JSON.toJSONString(data));
        } catch (Exception e) {
            log.info("发送禁用分类消息异常--> {categoryIdList: {}}", categoryIdList);
            e.printStackTrace();
        }
    }

    @Override
    public void sendEnableCategoryMsg(List<Long> categoryIdList, Long orgId, Integer groupId) {
        if (categoryIdList == null) {
            throw new IllegalParamsException("categoryIdList 不能为空！");
        }
        if (orgId == null) {
            throw new IllegalParamsException("orgId 不能为空！");
        }
        if (groupId == null) {
            throw new IllegalParamsException("groupId 不能为空！");
        }

        try {
            Map<String, Object> data = new HashMap<>(2);
            data.put("categoryIdList", categoryIdList);
            data.put("orgId", orgId);
            data.put("groupId", groupId);
            tdmqProducerTemplate.sendAsyncStringMsg(Topics.ENABLE_CATEGORY, JSON.toJSONString(data));
        } catch (Exception e) {
            log.info("发送启用分类消息异常--> {categoryId: {}}", categoryIdList);
            e.printStackTrace();
        }
    }

    /*public void sendCategoryNameChange(Long categoryId, Long orgId) {
        if (categoryId == null) {
            throw new IllegalParamsException("categoryId 不能为空！");
        }
        if (orgId == null) {
            throw new IllegalParamsException("orgId 不能为空！");
        }

        try {
            Map<String, Object> data = new HashMap<>(2);
            data.put("categoryId", categoryId);
            data.put("orgId", orgId);
            tdmqProducerTemplate.sendAsyncStringMsg(Topics.CATEGORY_NAME_CHANGE, JSON.toJSONString(data));
        } catch (Exception e) {
            log.info("发送试题删除消息异常--> {categoryId: {}}", categoryId);
            e.printStackTrace();
        }
    }*/

}
