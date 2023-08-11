package cn.qbs.wa.teach.exam.common.util;

/**
 * Redis Key 工具类
 * @Author zcm
 * @Date 2021-12-31 17:30
 * @Version 1.0
 */
public class RedisLockKeyUtil {

    /**
     * 前缀
     */
    private static final String PREFIX = "exam:";

    /**
     * 启动考试锁前缀
     */
    private static final String LOCK_START_EXAM_PREFIX = PREFIX + "Lock:StartExam:";

    /**
     * 添加考生锁前缀
     */
    private static final String LOCK_ADD_EXAMINEE_PREFIX = PREFIX + "Lock:AddExaminee:";

    /**
     * 添加考题锁前缀
     */
    private static final String LOCK_ADD_EXAM_QUESTIONS_PREFIX = PREFIX + "Lock:AddExamQuestions:";


    private RedisLockKeyUtil() {
    }

    /**
     * 启动考试锁key
     * @param examId
     * @return
     */
    public static String getStartExamLockKey(Long examId) {
        return String.format("%s%s", LOCK_START_EXAM_PREFIX, examId);
    }

    /**
     * 添加考生锁key
     * @param examId
     * @param userId
     * @return
     */
    public static String getAddExamineeLockKey(Long examId, Long userId) {
        return String.format("%s%s:%s", LOCK_ADD_EXAMINEE_PREFIX, examId, userId);
    }

    /**
     * 添加考题锁key
     * @param examId
     * @param userId
     * @return
     */
    public static String getAddExamQuestionsLockKey(Long examId, Long userId) {
        return String.format("%s%s:%s", LOCK_ADD_EXAM_QUESTIONS_PREFIX, examId, userId);
    }

}
