package cn.qbs.wa.train.allowance.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yjx
 */
@Getter
@AllArgsConstructor
public enum ApplyTypeEnum {

    /**
     * settle-入驻申请 qualification-资格申请  allowance-资助申请  activity-活动申请
     */
    SETTLE("settle"),

    QUALIFICATION("qualification"),

    ALLOWANCE("allowance"),

    ACTIVITY("activity"),
    

    ;

    private final String code;
}
