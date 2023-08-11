package cn.qbs.wa.teach.course.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yjx
 */
@Getter
@AllArgsConstructor
public enum CourseModeEnum {

    /**
     * 章节模式
     */
    CHAPTER(1, "章节模式"),

    /**
     * 节模式
     */
    LESSON(2, "节模式"),

    /**
     * 无章节模式
     */
    NONE(3, "无章节模式"),

    ;

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
        for (CourseModeEnum componentTypeEnum : CourseModeEnum.values()) {
            if (componentTypeEnum.getCode().equals(code)) {
                return componentTypeEnum.getCodeName();
            }
        }
        return null;
    }
}
