package cn.qbs.wa.teach.exam.common.correct.corrector;

import cn.qbs.wa.teach.exam.common.entity.ExamineeRecordQuestion;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 *
 * @Author zcm
 * @Date 2021-12-22 15:26
 * @Version 1.0
 */
@Component
@Scope("prototype")
public class DanXuanTiCorrector implements Corrector {

    @Override
    public BigDecimal correct(ExamineeRecordQuestion question, String correctAnswer) {
        if (StringUtils.isNotBlank(correctAnswer)) {
            if (StringUtils.isNotBlank(question.getAnswer())) {
                if (correctAnswer.equalsIgnoreCase(question.getAnswer())) {
                    return question.getQuestionScore();
                }
            }

            return BigDecimal.ZERO;
        }

        return null;
    }

}
