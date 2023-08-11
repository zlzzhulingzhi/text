package cn.qbs.wa.teach.notification.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author WX
 * 渠道类型
 */

@Getter
@AllArgsConstructor
public enum NotificationChannelEnum {

    /**
     * 站内信
     */
    STATIONMSG("station_msg"),

    /**
     * 短信
     */
    SMS("sms"),

    /**
     * 公众号
     */
    OFFICIALACCOUNT("official_account"),

    /**
     * 小程序
     */
    MINIPROGRAM("mini_program"),
    ;

    private final String codeValue;

}
