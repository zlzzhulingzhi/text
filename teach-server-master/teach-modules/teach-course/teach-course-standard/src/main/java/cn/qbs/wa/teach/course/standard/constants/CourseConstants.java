package cn.qbs.wa.teach.course.standard.constants;

/**
 * 课程常用状态常量
 *
 * @author lance
 * @date 2021/12/1
 */
public class CourseConstants {

    /**
     * 留言未回复
     */
    public static final int REPLY_STATUS_NOT_REPLY = 0;

    /**
     * 留言已回复
     */
    public static final int REPLY_STATUS_REPLIED = 1;

    /**
     * 课程开始进度
     */
    public static final int STUDY_PROGRESS_BEGIN = 1;

    /**
     * 学习已完成进度
     */
    public static final int STUDY_PROGRESS_COMPLETED = 100;

    /**
     * 学习已完成偏差
     */
    public static final int STUDY_PROGRESS_OFFSET = 0;

    /**
     * 学习已完成偏差
     */
    public static final String COURSE_LESSON_ADD = "add";

    /**
     * 学习已完成偏差
     */
    public static final String COURSE_LESSON_REMOVE = "remove";

    /**
     * 课程每日统计锁前缀
     */
    public static final String COURSE_STATISTIC_DAILY_LOCK_PREFIX = "course:statistic:daily:";

    /**
     * 课程汇总统计锁前缀
     */
    public static final String COURSE_STATISTIC_OVERVIEW_LOCK_PREFIX = "course:statistic:overview:";

    /**
     * 开启课时管理
     */
    public static final int COURSE_MANAGE_OPEN = 1;

    /**
     * 关闭课时管理
     */
    public static final int COURSE_MANAGE_CLOSE = 0;

}
