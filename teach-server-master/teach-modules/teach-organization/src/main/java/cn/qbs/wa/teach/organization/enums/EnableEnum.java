package cn.qbs.wa.teach.organization.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnableEnum {

    FORBIDDEN(0, "禁用"),
    START(1, "启用");

    @JsonValue
    private Integer code;

    private String name;

    public static SexEnum getEnum(String name) {
        SexEnum[] values = SexEnum.values();
        for (SexEnum value : values) {
            if (value.getName().equals(name)) {
                return value;
            }
        }
        return null;
    }

    public static SexEnum getEnum(Integer sex) {
        SexEnum[] values = SexEnum.values();
        for (SexEnum value : values) {
            if (value.getSex().equals(sex)) {
                return value;
            }
        }
        return null;
    }
}
