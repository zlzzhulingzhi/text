package cn.qbs.wa.train.logistics.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClassStatusEnum {

    STAY(0,"待开班"),

    OPEN(1,"开班"),

    CLOSE(2,"结班");

    private final Integer code;

    private String name;

    public static ClassStatusEnum getStatus(Integer status) {
        ClassStatusEnum[] values = ClassStatusEnum.values();
        for (ClassStatusEnum value : values) {
            if (value.getCode().equals(status)) {
                return value;
            }
        }
        return null;
    }
}