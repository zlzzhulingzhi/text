package cn.qbs.wa.teach.question.tdmq;

/**
 * 消息队列 Topic
 * @Author zcm
 * @Date 2021-12-07 18:56
 * @Version 1.0
 */
public class Topics {

    private Topics() {}

    /** 试题更新 */
    public static final String QUESTION_UPDATE = "question_update";

    /** 试题删除 */
    public static final String QUESTION_DELETE = "question_delete";

    /** 试卷更新 */
    public static final String PAPER_UPDATE = "paper_update";

    /** 试卷删除 */
    public static final String PAPER_DELETE = "paper_delete";

    /** 禁用分类 */
    public static final String DISABLE_CATEGORY = "disable_category";

    /** 启用分类 */
    public static final String ENABLE_CATEGORY = "enable_category";

    /** 分类名称修改 */
//    public static final String CATEGORY_NAME_CHANGE = "category_name_change";

}
