package cn.qbs.wa.teach.organization.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 账号类型
 * @author yjx
 */
@Getter
@AllArgsConstructor
public enum AccountTypeEnum {


    /**
     * 学员
     */
    STUDENT("student"),

    /**
     * 职工
     */
    EMPLOYEE("employee"),

    ;

    private final String name;

}
