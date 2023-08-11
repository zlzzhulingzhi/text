package cn.qbs.wa.teach.question.constant;

import java.math.BigDecimal;

/**
 * @Author zcm
 * @Date 2022/5/16 13:42
 * @Version 1.0
 */
public class QuestionConst {

    /**
     * 试题默认分数
     */
    public static final BigDecimal DEFAULT_SCORE = BigDecimal.ONE;

    /**
     * 试题最低分数
     */
    public static final BigDecimal MIN_SCORE = BigDecimal.valueOf(0.5D).setScale(1, BigDecimal.ROUND_HALF_UP);

    /**
     * 试题最高分数
     */
    public static final BigDecimal MAX_SCORE = BigDecimal.valueOf(999).setScale(1, BigDecimal.ROUND_HALF_UP);

    /**
     * 填空题答案分隔符
     */
    public static final String TIAN_KONG_TI_ANSWER_SEPARATOR = "|";

    /**
     * 试题默认难度名称
     */
    public static final String DEFAULT_DIFFICULTY_NAME = "简单";

    /**
     * 选择题最少选项个数
     */
    public static final int MULTIPLE_CHOICE_QUESTION_MIN_OPTION_COUNT = 2;


}
