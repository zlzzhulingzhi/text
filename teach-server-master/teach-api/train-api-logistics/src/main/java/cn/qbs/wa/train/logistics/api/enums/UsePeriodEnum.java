package cn.qbs.wa.train.logistics.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum UsePeriodEnum {


    ALLDAY("allDay", "全天"),

    MORNING("morning", "上午"),

    AFTERNOON("afternoon", "下午"),

    ;

    private final String code;
    private final String name;

    public static String getNameByCode(String code) {
        UsePeriodEnum[] values = UsePeriodEnum.values();
        for (UsePeriodEnum value : values) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }
}
