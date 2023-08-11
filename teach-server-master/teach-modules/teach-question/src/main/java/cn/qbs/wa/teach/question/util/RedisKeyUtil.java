package cn.qbs.wa.teach.question.util;

/**
 * Redis Key 工具类
 * @Author zcm
 * @Date 2021/12/7 11:39
 * @Version 1.0
 */
public class RedisKeyUtil {

    /**
     * 前缀
     */
    private static final String PREFIX = "question:";

    /**
     * 题型前缀
     */
    private static final String QUESTION_TYPE_PREFIX = PREFIX + "question:type:";

    /**
     * 题型 selectOptionList key
     */
    private static final String QUESTION_TYPE_SELECT_OPTION_LIST_KEY = QUESTION_TYPE_PREFIX + "selectOptionList";

    /**
     * 难度前缀
     */
    private static final String DIFFICULTY_PREFIX =  PREFIX +"difficulty:";

    /**
     * 难度 selectOptionList key
     */
    private static final String DIFFICULTY_SELECT_OPTION_LIST_KEY = DIFFICULTY_PREFIX + "selectOptionList";

    /**
     * 试题篮前缀
     */
    private static final String QUESTION_BASKET_PREFIX = PREFIX + "questionBasket:";


    private RedisKeyUtil() {
    }

    public static String getQuestionBasketPrefix() {
        return QUESTION_BASKET_PREFIX;
    }

    /**
     * 获取题库ID key
     * @param questionTypeId
     * @return
     */
    public static String getQuestionTypeIdKey(Long questionTypeId) {
        return QUESTION_TYPE_PREFIX + questionTypeId;
    }

    /**
     * 获取题库名称 key
     * @param questionTypeName
     * @return
     */
    public static String getQuestionTypeNameKey(String questionTypeName) {
        return QUESTION_TYPE_PREFIX + questionTypeName;
    }

    public static String getQuestionTypePrefix() {
        return QUESTION_TYPE_PREFIX;
    }

    public static String getQuestionTypeSelectOptionListKey() {
        return QUESTION_TYPE_SELECT_OPTION_LIST_KEY;
    }

    public static String getDifficultyIdKey(Long difficultyId) {
        return DIFFICULTY_PREFIX + difficultyId;
    }

    public static String getDifficultyNameKey(String difficultyName) {
        return DIFFICULTY_PREFIX + difficultyName;
    }

    public static String getDifficultySelectOptionListKey() {
        return DIFFICULTY_SELECT_OPTION_LIST_KEY;
    }

    public static String getQuestionBasketKey(Long userId) {
        return QUESTION_BASKET_PREFIX + userId;
    }

}
