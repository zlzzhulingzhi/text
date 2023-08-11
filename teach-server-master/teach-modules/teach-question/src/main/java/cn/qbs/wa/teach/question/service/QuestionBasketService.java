package cn.qbs.wa.teach.question.service;

import cn.qbs.wa.teach.question.pojo.question.QuestionGroupResponse;
import cn.qbs.wa.teach.question.pojo.question.basket.QuestionBasketAddRequest;
import cn.qbs.wa.teach.question.pojo.question.basket.QuestionBasketGroupCountItem;

import java.io.IOException;
import java.util.List;

/**
 * 试题篮Service
 * @Author zcm
 * @Date 2021/11/25 11:57
 * @Version 1.0
 */
public interface QuestionBasketService {

    void add(QuestionBasketAddRequest params);

    List<Long> getQuestionIdList();

    void remove(List<Long> questionIdList);

    List<QuestionBasketGroupCountItem> groupCount();

    List<QuestionGroupResponse> detail() throws IOException;

    void empty();

    /**
     * 从所有试题篮移除试题
     * @param questionIdList
     */
    void removeQuestionFormAllQuestionBasket(List<Long> questionIdList);

}
