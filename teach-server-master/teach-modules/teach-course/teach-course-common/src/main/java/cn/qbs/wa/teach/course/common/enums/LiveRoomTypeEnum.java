package cn.qbs.wa.teach.course.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum LiveRoomTypeEnum {

    /**
     * 所有可见
     */
    COURSE(1, "课程直播房间"),

    /**
     * 部分可见
     */
    MONITOR(100, "监考直播房间");

    private final Integer id;

    private final String code;

    /**
     * 根据代号获取枚举名
     * @param code 枚举代号
     * @return 枚举名
     */
    public static LiveRoomTypeEnum getEnumByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (LiveRoomTypeEnum visibleStatusEnum : LiveRoomTypeEnum.values()) {
            if (visibleStatusEnum.getId().equals(code)) {
                return visibleStatusEnum;
            }
        }
        return null;
    }

}

