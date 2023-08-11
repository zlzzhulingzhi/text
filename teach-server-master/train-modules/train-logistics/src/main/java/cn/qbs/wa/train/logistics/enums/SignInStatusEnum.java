package cn.qbs.wa.train.logistics.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SignInStatusEnum {

    /**
     * 0未签到 1已签到 2请假 3旷课
     */
    NOT_SUBMIT(0,"未签到"),

    NORMAL(1,"签到"),

    SICK(2,"请假"),

    ABSENT(3,"旷课");

    private final Integer code;

    private final String name;
}