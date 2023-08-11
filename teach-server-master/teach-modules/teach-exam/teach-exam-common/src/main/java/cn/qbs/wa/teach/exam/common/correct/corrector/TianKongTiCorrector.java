package cn.qbs.wa.teach.exam.common.correct.corrector;

import cn.qbs.wa.teach.exam.common.entity.ExamineeRecordQuestion;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
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
public class TianKongTiCorrector implements Corrector {

    @Override
    public BigDecimal correct(ExamineeRecordQuestion question, String correctAnswer) {
        if (StringUtils.isNotBlank(correctAnswer)) {
            String answer = question.getAnswer();
            if (StringUtils.isNotBlank(answer)) {
                if (correctAnswer.equalsIgnoreCase(answer)) {
                    return question.getQuestionScore();
                }

                String[] correctAnswerArr = correctAnswer.split("\\|");
                answer = replaceInvalidChars(answer);
                String[] answerArr = answer.split("\\|");
                BigDecimal avgScore = question.getQuestionScore().divide(BigDecimal.valueOf(correctAnswerArr.length), 1, BigDecimal.ROUND_HALF_UP);
                BigDecimal score = BigDecimal.ZERO;
                for (String s : answerArr) {
                    if (ArrayUtils.contains(correctAnswerArr, s)) {
                        score = score.add(avgScore);
                    }
                }

                return score;
            }

            return BigDecimal.ZERO;
        }

        return null;
    }

    private String replaceInvalidChars(String str) {
        // TODO 后续完善
        if (StringUtils.isNotBlank(str)) {
//            str = str.replace("、", "");
        }
        return str;
    }

}
