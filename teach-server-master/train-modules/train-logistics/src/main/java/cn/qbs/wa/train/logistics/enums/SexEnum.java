package cn.qbs.wa.train.logistics.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author vieux
 * @description: TODO
 * @date 2021/11/10 15:07
 */
@Getter
@AllArgsConstructor
public enum SexEnum {


    OTHER(0, "未知"),
    MALE(1, "男"),
    FEMALE(2, "女");


    @JsonValue
    private Integer sex;

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
