package cn.qbs.wa.teach.course.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum StreamStateEnum {


    ACTIVE("active", "活跃"),


    INACTIVE("inactive", "非活跃");

    private String code;

    private String codeName;

    /**
     * 根据代号获取枚举名
     *
     * @param code 枚举代号
     * @return 枚举名
     */
    public static String getNameByCode(String code) {
        if (code == null) {
            return null;
        }
        for (StreamStateEnum visibleStatusEnum : StreamStateEnum.values()) {
            if (visibleStatusEnum.getCode().equals(code)) {
                return visibleStatusEnum.getCodeName();
            }
        }
        return null;
    }

    public static StreamStateEnum getEnumByCode(String code) {
        if (code == null) {
            return null;
        }
        for (StreamStateEnum visibleStatusEnum : StreamStateEnum.values()) {
            if (visibleStatusEnum.getCode().equals(code)) {
                return visibleStatusEnum;
            }
        }
        return null;
    }

}

