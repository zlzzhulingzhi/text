package cn.qbs.wa.train.logistics.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 社交平台
 * @author yjx
 */
@Getter
@AllArgsConstructor
public enum SocialPlatformEnum {

    /**
     * 微信
     */
    WEIXIN("weixin"),

    ;

    private final String name;
}
