package cn.qbs.wa.teach.exam.common.correct.corrector;

import cn.qbs.wa.teach.exam.common.correct.CorrectUtil;
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
public class DuoXuanTiCorrector implements Corrector {

    @Override
    public BigDecimal correct(ExamineeRecordQuestion question, String correctAnswer) {
        String correctAnswerSplitRegex = "";
        BigDecimal questionScore = question.getQuestionScore();
        String examineeAnswer = question.getAnswer();
        String examineeAnswerSplitRegex = "";
        return CorrectUtil.correctDuoXuanTi(correctAnswer, correctAnswerSplitRegex, questionScore, examineeAnswer, examineeAnswerSplitRegex, true);
    }

    private String replaceInvalidChars(String str) {
        // TODO 后续完善
        if (StringUtils.isNotBlank(str)) {
//            str = str.replace("、", "");
        }
        return str;
    }

}
