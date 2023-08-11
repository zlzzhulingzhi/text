package cn.qbs.wa.teach.course.standard.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author WX
 * 报名验证类型
 */

@Getter
@AllArgsConstructor
public enum SignUpAuthTypeEnum {

    /**
     * 无需验证
     */
    NONE(1),

    /**
     * 密码验证
     */
    NEED(2),
    ;

    private final Integer codeValue;

}
