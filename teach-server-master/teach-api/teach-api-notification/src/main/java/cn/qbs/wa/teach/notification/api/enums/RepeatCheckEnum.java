package cn.qbs.wa.teach.notification.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author WX
 * 发送通知时是否开启重复检查
 */

@Getter
@AllArgsConstructor
public enum RepeatCheckEnum {

    /**
     * 关闭
     */
    CLOSE(0,"关闭重复检查"),

    /**
     * 开启
     */
    OPEN(1,"开启重复检查"),
    ;

    private final Integer code;

    private final String codeValue;

}
