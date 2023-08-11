package cn.qbs.wa.teach.question.validate;

import cn.qbs.wa.teach.question.pojo.question.BasicQuestion;

/**
 * @Author zcm
 * @Date 2021/11/9 11:00
 * @Version 1.0
 */
public interface QuestionValidate {

    /**
     * 试题校验
     * @param basicQuestion
     */
    void validate(BasicQuestion basicQuestion);

}
