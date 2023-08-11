package cn.qbs.wa.teach.question.service.impl;

import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.question.entity.Question;
import cn.qbs.wa.teach.question.entity.QuestionType;
import cn.qbs.wa.teach.question.pojo.question.QuestionGroupResponse;
import cn.qbs.wa.teach.question.pojo.question.basket.QuestionBasketAddRequest;
import cn.qbs.wa.teach.question.pojo.question.basket.QuestionBasketGroupCountItem;
import cn.qbs.wa.teach.question.service.QuestionBasketService;
import cn.qbs.wa.teach.question.service.QuestionService;
import cn.qbs.wa.teach.question.service.QuestionTypeService;
import cn.qbs.wa.teach.question.util.RedisKeyUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import io.jsonwebtoken.lang.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 试题篮Service实现类
 * @Author zcm
 * @Date 2021/11/25 10:01
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionBasketServiceImpl implements QuestionBasketService {

    private final QuestionService questionService;

    private final RedissonClient redissonClient;

    private final QuestionTypeService questionTypeService;

    /**
     * 试题篮允许添加的最大题数
     */
    @Value("${paperMaxQuestionCount:200}")
    private Integer paperMaxQuestionCount;


    /**
     * 加入试题篮
     * @param params
     */
    @Override
    public void add(QuestionBasketAddRequest params) {
        List<Long> questionIdList = params.getQuestionIdList();
        if (CollectionUtils.isEmpty(questionIdList)) {
            throw new IllegalParamsException("试题ID列表不能为空！");
        }

        List<Long> questionBasket = getQuestionBasket();
        if ((questionIdList.size() + questionBasket.size()) > paperMaxQuestionCount) {
            throw new ServiceException("试题不能超过" + paperMaxQuestionCount + "道！");
        }

        List<Question> questionList = questionService.selectByQuestionIdList(questionIdList);
        List<Long> dbQuestionIdList = questionList.stream().map(i -> i.getId()).collect(Collectors.toList());

        List<Long> failQuestionIdList = new ArrayList<>();
        for (Long questionId : questionIdList) {
            if (!dbQuestionIdList.contains(questionId)) {
                failQuestionIdList.add(questionId);
                continue;
            }

            if (!questionBasket.contains(questionId)) {
                questionBasket.add(questionId);
            }
        }

        if (failQuestionIdList.size() > 0) {
            throw new IllegalParamsException(String.format("以下试题[ID: %s]不存在或已禁用！", failQuestionIdList.toString()));
        }
    }

    @Override
    public List<Long> getQuestionIdList() {
        return getQuestionBasket();
    }

    /**
     * 从redis中获取试题篮
     * @return
     */
    private List<Long> getQuestionBasket() {
        return redissonClient.getList(RedisKeyUtil.getQuestionBasketKey(getUserId()));
    }

    /**
     * 从试题篮移除试题
     * @param questionIdList
     */
    @Override
    public void remove(List<Long> questionIdList) {
        List<Long> questionBasket = getQuestionBasket();
        if (CollectionUtils.isNotEmpty(questionBasket)) {
            questionBasket.removeAll(questionIdList);
        }
    }

    /**
     * 按题型分组统计题数
     * @return
     */
    @Override
    public List<QuestionBasketGroupCountItem> groupCount() {
        List<QuestionBasketGroupCountItem> list = new ArrayList<>();
        List<Long> questionBasket = getQuestionBasket();
        if (CollectionUtils.isNotEmpty(questionBasket)) {
            List<Question> questionList = questionService.selectByQuestionIdList(questionBasket);
            Map<Long, List<Long>> map = questionList.stream().collect(
                    Collectors.groupingBy(
                            Question::getQuestionTypeId, Collectors.mapping(Question::getId, Collectors.toList())
                    )
            );
            map.forEach((questionTypeId, questionIdList) -> {
                QuestionType questionType = questionTypeService.getByCache(questionTypeId);
                list.add(new QuestionBasketGroupCountItem(questionType.getId(), questionType.getName(), questionType.getSortNum(), questionIdList));
            });

            return list.stream().sorted(Comparator.comparing(QuestionBasketGroupCountItem::getSortNum)).collect(Collectors.toList());
        }
        return list;
    }

    /**
     * 获取试题篮详情，按题型分组，包含试题详情
     * @return
     */
    @Override
    public List<QuestionGroupResponse> detail() throws IOException {
        List<Long> questionBasket = getQuestionBasket();

        if (CollectionUtils.isNotEmpty(questionBasket)) {
            Long[] questionIdArray = new Long[questionBasket.size()];
            questionBasket.toArray(questionIdArray);
            return questionService.groupList(questionBasket);
        }

        return null;
    }

    /**
     * 清空试题篮
     */
    @Override
    public void empty() {
        redissonClient.getList(RedisKeyUtil.getQuestionBasketKey(getUserId())).clear();
    }

    private Long getUserId() {
        Long userId = SecurityContextHolder.getUserId();
        if (userId == null || userId == 0) {
            throw new ServiceException("请先登录！");
        }
        return userId;
    }

    /**
     * 从所有试题篮移除试题
     * @param questionIdList
     */
    @Override
    public void removeQuestionFormAllQuestionBasket(List<Long> questionIdList) {
        log.info("从所有试题篮移除试题: {}", questionIdList);
        if (CollectionUtils.isNotEmpty(questionIdList)) {
            String pattern = RedisKeyUtil.getQuestionBasketPrefix() + "*";
            Iterable<String> questionBasketKeysIterable = redissonClient.getKeys().getKeysByPattern(pattern);
            questionBasketKeysIterable.forEach(key -> {
                log.info("试题篮 key: {}", key);
                List<Long> questionBasket = redissonClient.getList(key);
                if (Collections.containsAny(questionBasket, questionIdList)) {
                    questionBasket.removeAll(questionIdList);
                }
            });

        }
    }

}
