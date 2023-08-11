package cn.qbs.wa.train.allowance.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ManageTypeEnum {

    /**
     * 角色
     */
    ROLE("role"),

    /**
     * 用户
     */
    USER("user")

    ;

    private final String code;

    public static ManageTypeEnum getEnumByCode(String code) {
        if (code == null) {
            return null;
        }
        for (ManageTypeEnum flowCodeEnum : ManageTypeEnum.values()) {
            if (flowCodeEnum.getCode().equals(code)) {
                return flowCodeEnum;
            }
        }
        return null;
    }
}
