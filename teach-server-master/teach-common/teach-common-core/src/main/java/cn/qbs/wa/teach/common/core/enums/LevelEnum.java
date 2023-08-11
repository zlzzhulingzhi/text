package cn.qbs.wa.teach.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yjx
 */

@Getter
@AllArgsConstructor
public enum LevelEnum {

    /**
     * 第一层级
     */
    FIRST("first", "第一层级"),

    /**
     * 第二层级
     */
    SECOND("second", "第二层级"),

    /**
     * 第三层级
     */
    THIRD("third", "第三层级");

    private final String code;

    private final String codeName;

    /**
     * 根据代号获取枚举名
     * @param code 枚举代号
     * @return 枚举名
     */
    public static String getNameByCode(String code) {
        if (code == null) {
            return null;
        }
        for (LevelEnum levelEnum : LevelEnum.values()) {
            if (levelEnum.getCode().equals(code)) {
                return levelEnum.getCodeName();
            }
        }
        return null;
    }

    /**
     * 根据代号获取枚举名
     * @param code 枚举代号
     * @return 枚举名
     */
    public static LevelEnum getEnumByCode(String code) {
        if (code == null) {
            return null;
        }
        for (LevelEnum levelEnum : LevelEnum.values()) {
            if (levelEnum.getCode().equals(code)) {
                return levelEnum;
            }
        }
        return null;
    }
}
