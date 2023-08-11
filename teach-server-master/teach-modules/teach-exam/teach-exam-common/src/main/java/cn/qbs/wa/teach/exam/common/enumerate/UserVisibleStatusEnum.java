package cn.qbs.wa.teach.exam.common.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 课程可见用户枚举类
 * @author yjx
 */

@Getter
@AllArgsConstructor
public enum UserVisibleStatusEnum {

    /**
     * 所有可见
     */
    ALL(1, "所有可见"),

    /**
     * 部分可见
     */
    SOME(2, "部分可见");

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
        for (UserVisibleStatusEnum visibleStatusEnum : UserVisibleStatusEnum.values()) {
            if (visibleStatusEnum.getCode().equals(code)) {
                return visibleStatusEnum.getCodeName();
            }
        }
        return null;
    }

}

