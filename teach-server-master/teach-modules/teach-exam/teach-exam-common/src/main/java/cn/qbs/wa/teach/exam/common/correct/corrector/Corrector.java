package cn.qbs.wa.teach.exam.common.correct.corrector;

import cn.qbs.wa.teach.exam.common.entity.ExamineeRecordQuestion;

import java.math.BigDecimal;

/**
 *
 * @Author zcm
 * @Date 2021-12-22 16:44
 * @Version 1.0
 */
public interface Corrector {

    /**
     * 正常批改返回批改后的得分，无法批改的情况时返回null
     * @param question
     * @param correctAnswer 正确答案
     * @return
     */
    BigDecimal correct(ExamineeRecordQuestion question, String correctAnswer);

}
