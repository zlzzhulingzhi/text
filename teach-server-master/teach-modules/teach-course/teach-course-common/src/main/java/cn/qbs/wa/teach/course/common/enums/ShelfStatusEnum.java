package cn.qbs.wa.teach.course.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 课程上下架枚举类
 * @author yjx
 */

@Getter
@AllArgsConstructor
public enum ShelfStatusEnum {

    /**
     * 上架状态
     */
    UP(1, "上架"),

    /**
     * 下架状态
     */
    DOWN(0, "下架");

    private final Integer code;

    private final String codeName;

    /**
     * 根据代号获取枚举名
     * @param code 枚举代号
     * @return 枚举名
     */
    public static String getNameByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (ShelfStatusEnum shelfStatusEnum : ShelfStatusEnum.values()) {
            if (shelfStatusEnum.getCode().equals(code)) {
                return shelfStatusEnum.getCodeName();
            }
        }
        return null;
    }
}
