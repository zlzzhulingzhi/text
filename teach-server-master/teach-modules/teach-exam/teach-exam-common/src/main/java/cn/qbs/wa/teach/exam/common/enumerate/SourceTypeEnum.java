package cn.qbs.wa.teach.exam.common.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: NQY
 * @Date: 2022/5/6 17:15
 * @Description:
 */
@Getter
@AllArgsConstructor
public enum SourceTypeEnum {
    CERT(1, "证书模块"),


    TASK(2, "任务模块"),

    EXAM(3, "考试模块");
    private final Integer type;

    private final String codeName;

    /**
     * 根据代号获取枚举名
     *
     * @param type 枚举代号
     * @return 枚举名
     */
    public static String getNameByType(Integer type) {
        if (type == null) {
            return null;
        }
        for (SourceTypeEnum visibleStatusEnum : SourceTypeEnum.values()) {
            if (visibleStatusEnum.getType().equals(type)) {
                return visibleStatusEnum.getCodeName();
            }
        }
        return null;
    }

    public static SourceTypeEnum getEnumByType(Integer type) {
        if (type == null) {
            return null;
        }
        for (SourceTypeEnum visibleStatusEnum : SourceTypeEnum.values()) {
            if (visibleStatusEnum.getType().equals(type)) {
                return visibleStatusEnum;
            }
        }
        return null;
    }
}
