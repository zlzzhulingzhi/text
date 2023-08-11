package cn.qbs.wa.teach.exam.common.util;

/**
 * Redis Key 工具类
 * @Author zcm
 * @Date 2021-12-31 17:30
 * @Version 1.0
 */
public class RedisKeyUtil {

    /**
     * 前缀
     */
    private static final String PREFIX = "exam:";

    /**
     * 添加考题结果前缀
     */
    private static final String ADD_EXAM_QUESTIONS_RET_PREFIX = PREFIX + "AddExamQuestionsRet:";


    private RedisKeyUtil() {
    }

    /**
     * 添加考题结果 key
     * @param examId
     * @param userId
     * @return
     */
    public static String getAddExamQuestionsRetKey(Long examId, Long userId) {
        return String.format("%s%s:%s", ADD_EXAM_QUESTIONS_RET_PREFIX, examId, userId);
    }

}
