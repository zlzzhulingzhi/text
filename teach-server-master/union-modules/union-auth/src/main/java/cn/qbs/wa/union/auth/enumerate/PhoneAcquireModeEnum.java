package cn.qbs.wa.union.auth.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 手机号获取来源
 * @author yjx
 */
@Getter
@AllArgsConstructor
public enum PhoneAcquireModeEnum {

    /**
     * manual:手动输入、weixin:微信获取
     */
    MANUAL(" manual"),

    WEIXIN("weixin"),

    ;

    private final String code;
}
