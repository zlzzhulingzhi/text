package cn.qbs.wa.teach.question.service;

import java.util.List;

/**
 * @Author zcm
 * @Date 2021/12/9 18:39
 * @Version 1.0
 */
public interface MqProducerService {

    /**
     * 发送试题更新消息
     * @param questionId
     * @param orgId
     */
    void sendQuestionUpdateMsg(Long questionId, Long orgId);

    /**
     * 发送试题删除消息
     * @param questionIdList
     */
    void sendQuestionDeleteMsg(List<Long> questionIdList);

    /**
     * 发送试卷更新消息
     * @param paperId
     * @param orgId
     */
    void sendPaperUpdateMsg(Long paperId, Long orgId);

    /**
     * 发送试卷删除消息
     * @param paperIdList
     */
    void sendPaperDeleteMsg(List<Long> paperIdList);

    void sendDisableCategoryMsg(List<Long> categoryIdList, Long orgId, Integer groupId);

    void sendEnableCategoryMsg(List<Long> categoryIdList, Long orgId, Integer groupId);

}
