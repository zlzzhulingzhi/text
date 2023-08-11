package cn.qbs.wa.teach.exam.common.constant;

/**
 * 消息队列 Topic
 * @Author zcm
 * @Date 2021-12-16 10:56
 * @Version 1.0
 */
public class Topics {

    private Topics() {}

    /** 添加考题 */
    public static final String ADD_EXAM_QUESTIONS = "add_exam_questions";

    /** 交卷 */
    public static final String SUBMIT_PAPER = "submit_paper";

    /** 人工批改 */
    public static final String MANUAL_CORRECT = "manual_correct";

    /**自动颁发证书*/
    public static final String ISSUING_OF_CERTIFICATES = "issuing_of_certificates";

}
