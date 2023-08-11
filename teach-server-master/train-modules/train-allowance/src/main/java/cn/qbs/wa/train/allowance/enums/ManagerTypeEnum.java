package cn.qbs.wa.train.allowance.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ManagerTypeEnum {

    /**
     * role-  角色    user- 用户
     */
    ROLE("role"),

    USER("user"),
    ;

    private final String code;
}
