package cn.qbs.wa.teach.course.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yjx
 */

@Getter
@AllArgsConstructor
public enum CourseTypeEnum {
    /**
     * 录播课
     */
    RECORD("record", "录播课"),
    /**
     * 直播课
     */
    LIVE("live", "直播课"),
    /**
     * 综合课
     */
    MIX("mix", "综合课");

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
        for (CourseTypeEnum componentTypeEnum : CourseTypeEnum.values()) {
            if (componentTypeEnum.getCode().equals(code)) {
                return componentTypeEnum.getCodeName();
            }
        }
        return null;
    }
}
