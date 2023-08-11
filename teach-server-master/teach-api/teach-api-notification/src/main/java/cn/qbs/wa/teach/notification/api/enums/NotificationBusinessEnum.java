package cn.qbs.wa.teach.notification.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author WX
 * 业务类型
 */

@Getter
@AllArgsConstructor
public enum NotificationBusinessEnum {

    /**
     * 手动添加
     */
    MANUAL(0,null,null),

    /**
     * 培训项目
     */
    TRAIN(1,"您有新的任务{taskName}，<a href='{url}'>请查看~</a>","培训项目通知"),

    /**
     * 在线课堂
     */
    CLASSROOM(2,"尊敬的{}，您已成功报名课程《{courseName}》，快开始学习吧！","报名成功通知"),

    /**
     * 课程直播
     */
    LIVE_COURSE(3,"直播马上开始啦！《{courseName}》直播即将开始，<a href='{url}'>点击查看详情！</a>","直播通知"),

    /**
     * 课程上架
     */
    UP_COURSE(4,"课程上新啦！《{courseName}》已上架，<a href='{url}'>点击查看详情！</a>","新课程通知"),

    /**
     * 考试批阅
     */
    CORRECT_EXAM(5,"您的考试《{examName}》已批阅完成，<a href='{url}'>点击查看详情！</a>","考试批阅通知"),

    /**
     * 获得证书
     */
    GOT_CREDENTIAL(6,"您已获得新的证书《{credentialName}》,<a href='{url}'>点击查看！</a>","新证书通知"),
    ;

    private final Integer codeValue;

    private final String template;

    private final String title;

    /**
     * 根据枚举代号获取模板值
     * */
    public static String getTemplateByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (NotificationBusinessEnum notificationBusinessEnum : NotificationBusinessEnum.values()) {
            if (notificationBusinessEnum.getCodeValue().equals(code)) {
                return notificationBusinessEnum.getTemplate();
            }
        }
        return null;
    }

    /**
     * 根据枚举代号获取标题值
     * */
    public static String getTitleByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (NotificationBusinessEnum notificationBusinessEnum : NotificationBusinessEnum.values()) {
            if (notificationBusinessEnum.getCodeValue().equals(code)) {
                return notificationBusinessEnum.getTitle();
            }
        }
        return null;
    }

}
