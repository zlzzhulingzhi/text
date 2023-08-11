package cn.qbs.wa.teach.course.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SectionEnum {
    OFF(0, "关闭"),
    ON(1, "开启");

    private Integer code;
    private String codeName;

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
