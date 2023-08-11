package cn.qbs.wa.teach.course.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum ProviderTypeEnum {

    /**
     * 所有可见
     */
    TXZB("txzb", "腾讯直播"),

    /**
     * 部分可见
     */
    TXKZB("txkzb", "腾讯快直播");

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
        for (ProviderTypeEnum visibleStatusEnum : ProviderTypeEnum.values()) {
            if (visibleStatusEnum.getCode().equals(code)) {
                return visibleStatusEnum.getCodeName();
            }
        }
        return null;
    }

    public static ProviderTypeEnum getEnumByCode(String code) {
        if (code == null) {
            return null;
        }
        for (ProviderTypeEnum visibleStatusEnum : ProviderTypeEnum.values()) {
            if (visibleStatusEnum.getCode().equals(code)) {
                return visibleStatusEnum;
            }
        }
        return null;
    }

}

