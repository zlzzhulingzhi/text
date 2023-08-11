package cn.qbs.wa.train.allowance.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据板块
 *
 * @author yjx
 */
@Getter
@AllArgsConstructor
public enum AttachSectionEnum {

    /**
     * (apply-申请板块 audit-审核板块)
     */
    APPLY("apply"),

    AUDIT("audit"),

    ;

    private final String code;
}
