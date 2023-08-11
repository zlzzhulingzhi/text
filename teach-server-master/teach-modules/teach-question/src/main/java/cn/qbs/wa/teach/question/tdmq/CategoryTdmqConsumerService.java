package cn.qbs.wa.teach.question.tdmq;

import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.question.enumerate.CategoryGroupEnum;
import cn.qbs.wa.teach.question.service.CategoryService;
import cn.qbs.wa.teach.question.service.QuestionSearchService;
import cn.qbs.wa.teach.question.service.QuestionService;
import cn.qbs.wa.teach.question.util.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
public class CategoryTdmqConsumerService {

    private final CategoryService categoryService;

    private final QuestionSearchService questionSearchService;

    private final QuestionService questionService;


//    @TdmqConsumer(topic= Topics.CATEGORY_NAME_CHANGE, serialization = Serialization.STRING, subscriptionName = "question")
    public void categoryNameChange(String msg) throws Exception {
        log.info("{}-{} TDMQ收到分类名称修改消息 msg: {}", Topics.QUESTION_UPDATE, DateUtil.nowDateStr(), msg);
        try {
            JSONObject jsonObject = JSONObject.parseObject(msg);
            Long categoryId = jsonObject.getLong("categoryId");
            Long orgId = jsonObject.getLong("orgId");
            log.info("categoryId: {}", categoryId);

//            List<QuestionSearchResult> questionSearchResultList = questionSearchService.searchByCategoryId(categoryId);
//            Category category = categoryService.selectFirst(categoryId, orgId);

        } catch (Exception e) {
            log.error("分类名称修改消息处理失败: {}", msg);
            e.printStackTrace();
            throw new ServiceException(String.format("TDMQ消费失败 msg: %s", msg));
        }
    }

    /**
     * 禁用分类
     * @param msg
     * @throws Exception
     */
    @TdmqConsumer(topic= Topics.DISABLE_CATEGORY, serialization = Serialization.STRING, subscriptionName = "question")
    public void onDisableCategory(String msg) throws Exception {
        log.info("{}-{} TDMQ收到禁用分类消息 msg: {}", Topics.DISABLE_CATEGORY, DateUtil.nowDateStr(), msg);
        try {
            JSONObject jsonObject = JSONObject.parseObject(msg);
            List<Long> categoryIdList = JSONArray.parseArray(jsonObject.getString("categoryIdList"), Long.class);
            log.info("categoryIdList: {}", categoryIdList);
            Long orgId = jsonObject.getLong("orgId");
            Integer groupId = jsonObject.getInteger("groupId");

            if (CategoryGroupEnum.QUESTION.getId() == groupId) {
                questionSearchService.deleteByCategoryIdList(categoryIdList);

            } else if (CategoryGroupEnum.PAPER.getId() == groupId) {
//                questionService.removeByCategoryIdListFromES(categoryIdList, orgId);
            }

        } catch (Exception e) {
            log.error("禁用分类消息处理失败: {}", msg);
            e.printStackTrace();
            throw new ServiceException(String.format("TDMQ消费失败 msg: %s", msg));
        }
    }

    /**
     * 启用分类
     * @param msg
     * @throws Exception
     */
    @TdmqConsumer(topic= Topics.ENABLE_CATEGORY, serialization = Serialization.STRING, subscriptionName = "question")
    public void onEnableCategory(String msg) throws Exception {
        log.info("{}-{} TDMQ收到启用分类消息 msg: {}", Topics.DISABLE_CATEGORY, DateUtil.nowDateStr(), msg);
        try {
            JSONObject jsonObject = JSONObject.parseObject(msg);
            List<Long> categoryIdList = JSONArray.parseArray(jsonObject.getString("categoryIdList"), Long.class);
            log.info("categoryIdList: {}", categoryIdList);
            Long orgId = jsonObject.getLong("orgId");
            Integer groupId = jsonObject.getInteger("groupId");

            if (CategoryGroupEnum.QUESTION.getId() == groupId) {
                questionService.addQuestionToESByCategoryIdList(categoryIdList, orgId);

            } else if (CategoryGroupEnum.PAPER.getId() == groupId) {
//                questionService.removeByCategoryIdListFromES(categoryIdList, orgId);
            }

        } catch (Exception e) {
            log.error("启用分类消息处理失败: " + msg, e);
            throw new ServiceException(String.format("TDMQ消费失败 msg: %s", msg));
        }
    }

}
