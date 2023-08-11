package cn.qbs.wa.train.logistics.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yjx
 */
@Getter
@AllArgsConstructor
public enum DecorationEnum {

    /**
     * PC装修
     */
    PC(1, "PC装修"),

    /**
     * H5装修
     */
    H5(2, "H5装修");

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
        for (DecorationEnum visibleStatusEnum : DecorationEnum.values()) {
            if (visibleStatusEnum.getCode().equals(code)) {
                return visibleStatusEnum.getCodeName();
            }
        }
        return null;
    }
}
