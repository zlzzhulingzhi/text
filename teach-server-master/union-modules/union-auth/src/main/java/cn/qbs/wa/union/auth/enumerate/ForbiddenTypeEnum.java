package cn.qbs.wa.union.auth.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ForbiddenTypeEnum {

    /**
     * 登录
     */
    LOGIN("login"),

    /**
     * 密码修改
     */
    PASSWORD("password"),

    ;

    private final String type;
}
