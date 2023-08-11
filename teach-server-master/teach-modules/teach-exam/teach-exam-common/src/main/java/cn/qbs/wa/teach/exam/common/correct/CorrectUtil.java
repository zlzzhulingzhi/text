package cn.qbs.wa.teach.exam.common.correct;

import cn.qbs.wa.teach.common.core.exception.ServiceException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * 批改工具类
 * @Author zcm
 * @Date 2022/1/12 18:50
 * @Version 1.0
 */
@Slf4j
public class CorrectUtil {

    public static final char START_OPTION = 'A';

    public static final char END_OPTION = 'J';


    /**
     * 批改多选题
     * @param correctAnswer 正确答案
     * @param correctAnswerSplitRegex 正确答案分离正则表达式
     * @param questionScore 试题分数
     * @param examineeAnswer 考生答案
     * @param examineeAnswerSplitRegex 考生答案分离正则表达式
     * @param aloneCalculateScore 是否单独计算分数
     * @return
     */
    public static BigDecimal correctDuoXuanTi(String correctAnswer, String correctAnswerSplitRegex, BigDecimal questionScore,
                                              String examineeAnswer, String examineeAnswerSplitRegex, boolean aloneCalculateScore) {

        checkParams(correctAnswer, correctAnswerSplitRegex, questionScore, examineeAnswer, examineeAnswerSplitRegex);
        if (StringUtils.isBlank(examineeAnswer)) {
            log.info("考生答案为空 --> 得分: 0");
            return BigDecimal.ZERO;
        }

        if (correctAnswer.equalsIgnoreCase(examineeAnswer)) {
            log.info("correctAnswer: {}, examineeAnswer: {}, questionScore: {} --> 得分: {}", correctAnswer, examineeAnswer, questionScore, questionScore);
            return questionScore;
        }

        String[] correctAnswerArr = correctAnswer.split(correctAnswerSplitRegex);
        String[] examineeAnswerArr = examineeAnswer.split(examineeAnswerSplitRegex);

        int correctAnswerValue = 0;
        for (String s : correctAnswerArr) {
            int optionValue = getOptionValue(s);
            correctAnswerValue += optionValue;
        }

        int examineeAnswerValue = 0;
        for (String s : examineeAnswerArr) {
            int optionValue = getOptionValue(s);
            examineeAnswerValue += optionValue;
        }

        BigDecimal score;
        if (examineeAnswerValue == 0) {
            score = BigDecimal.ZERO;
        } else {

            int diff = correctAnswerValue ^ examineeAnswerValue;
            if (diff == 0) {
                //全对满分
                score = questionScore;
            } else {
                if ((diff | correctAnswerValue) == correctAnswerValue) {
                    //少选
                    if (!aloneCalculateScore) {
                        score = BigDecimal.ZERO;
                    } else {
                        BigDecimal avgScore = questionScore.divide(BigDecimal.valueOf(correctAnswerArr.length), 1, BigDecimal.ROUND_HALF_UP);
                        score = avgScore.multiply(BigDecimal.valueOf(examineeAnswerArr.length));
                    }
                } else {
                    //多选不得分
                    score = BigDecimal.ZERO;
                }
            }
        }

        log.info("correctAnswer: {}, examineeAnswer: {}, questionScore: {}, correctAnswerValue: {}, examineeAnswerValue: {} --> 得分: {}",
                correctAnswer, examineeAnswer, questionScore, correctAnswerValue, examineeAnswerValue, score);
        return score;
    }

    private static int getOptionValue(String optionStr) {
        char option = optionStr.toUpperCase().charAt(0);
        int num = (option - 'A') + 1;
        int value = 1;
        for (int i = 1; i < num; i++) {
            value *= 2;
        }
        return value;
    }

    /**
     * 批改填空题
     * @param correctAnswer 正确答案
     * @param correctAnswerSplitRegex 正确答案分离正则表达式
     * @param questionScore 试题分数
     * @param examineeAnswer 考生答案
     * @param examineeAnswerSplitRegex 考生答案分离正则表达式
     * @param aloneCalculateScore 是否单独计算分数
     * @return
     */
    public static BigDecimal correctTianKongTi(String correctAnswer, String correctAnswerSplitRegex, BigDecimal questionScore,
                                             String examineeAnswer, String examineeAnswerSplitRegex, boolean aloneCalculateScore) throws Exception {
        if (true) {
            throw new Exception("功能未完成！");
        }

        checkParams(correctAnswer, correctAnswerSplitRegex, questionScore, examineeAnswer, examineeAnswerSplitRegex);

        if (correctAnswer.equalsIgnoreCase(examineeAnswer)) {
            return questionScore;
        }

        String[] correctAnswerArr = correctAnswer.split(correctAnswerSplitRegex);
        String[] examineeAnswerArr = examineeAnswer.split(examineeAnswerSplitRegex);
        // 非单独算分的情况下，答案数量不一致，直接算0分
        if (!aloneCalculateScore && correctAnswerArr.length != examineeAnswerArr.length) {
            return BigDecimal.ZERO;
        }

        BigDecimal score = BigDecimal.ZERO;

        // 答对数量
        int correctAnswersCount = 0;
        for (int i = 0; i < correctAnswerArr.length; i++) {
            String a1 = correctAnswerArr[i];
            String a2 = examineeAnswerArr[i];
            if (StringUtils.isNotBlank(a1) && StringUtils.isNotBlank(a2) && a1.equalsIgnoreCase(a2)) {
                correctAnswersCount++;
            }
        }

        if (aloneCalculateScore) {
            BigDecimal avgScore = questionScore.divide(BigDecimal.valueOf(correctAnswerArr.length), 1, BigDecimal.ROUND_HALF_UP);
            score = avgScore.multiply(BigDecimal.valueOf(correctAnswersCount));
        } else {

        }
        return score;
    }

    private static void checkParams(String correctAnswer, String correctAnswerSplitRegex, BigDecimal questionScore,
                                    String examineeAnswer, String examineeAnswerSplitRegex) {
        if (StringUtils.isBlank(correctAnswer)) {
            throw new ServiceException("正确答案不能为空！");
        }
        if (correctAnswerSplitRegex == null) {
            throw new ServiceException("正确答案分离正则表达式不能为null！");
        }
        if (questionScore == null) {
            throw new ServiceException("试题分数不能为null！");
        }
        if (questionScore.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("试题分数不能小于或等于0！");
        }
        if (examineeAnswerSplitRegex == null) {
            throw new ServiceException("考生答案分离正则表达式不能为null！");
        }
    }


    public static void main(String[] args) {
        duoXuanTiCorrectTest();
    }

    private static void duoXuanTiCorrectTest() {
        BigDecimal questionScore = BigDecimal.valueOf(5);
        String correctAnswer = "ABC";
        correctDuoXuanTi(correctAnswer, "", questionScore, "ABC", "", true);
        correctDuoXuanTi(correctAnswer, "", questionScore, "BC", "", true);
        correctDuoXuanTi(correctAnswer, "", questionScore, "AC", "", false);
        correctDuoXuanTi(correctAnswer, "", questionScore, "BCD", "", true);
        correctDuoXuanTi(correctAnswer, "", questionScore, "ACD", "", true);
        correctDuoXuanTi(correctAnswer, "", questionScore, "", "", true);
    }

}
