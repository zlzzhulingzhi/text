package cn.qbs.wa.teach.course.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum LiveBusinessTypeEnum {

    /**
     * 所有可见
     */
    COURSE(1, "课程直播"),

    COURSE_CHILD(2, "课程子直播");



    private final Integer id;

    private final String typeName;

    /**
     * 根据代号获取枚举名
     * @param code 枚举代号
     * @return 枚举名
     */
    public static String getNameByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (LiveBusinessTypeEnum visibleStatusEnum : LiveBusinessTypeEnum.values()) {
            if (visibleStatusEnum.getId().equals(code)) {
                return visibleStatusEnum.getTypeName();
            }
        }
        return null;
    }

}

