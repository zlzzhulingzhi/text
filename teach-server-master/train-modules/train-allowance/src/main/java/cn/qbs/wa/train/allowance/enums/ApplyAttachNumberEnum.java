package cn.qbs.wa.train.allowance.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplyAttachNumberEnum {

    /**
     * qualification-资格申请  allowance-资助申请  activity-活动申请
     */
    QUALIFICATION(13, "qualification"),

    ALLOWANCE(10, "allowance"),

    ACTIVITY(4, "activity"),
    ;

    private final Integer number;

    private final String code;

}
