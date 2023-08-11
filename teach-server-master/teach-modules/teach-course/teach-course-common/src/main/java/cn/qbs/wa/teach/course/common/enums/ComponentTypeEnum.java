package cn.qbs.wa.teach.course.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yjx
 */

@Getter
@AllArgsConstructor
public enum ComponentTypeEnum {
    /**
     * 资料
     */
    ZL("ZL", "资料"),
    /**
     * 文档
     */
    WD("WD", "文档"),
    /**
     * 视频
     */
    SP("SP", "视频"),
    /**
     * 试卷
     */
    SJ("SJ", "试卷"),
    /**
     * 练习
     */
    LX("LX", "练习"),
    /**
     * 其他
     */
    EXTRA("EXTRA", "其他"),
    /**
     * 试卷
     */
    ZB("ZB", "直播");

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
        for (ComponentTypeEnum componentTypeEnum : ComponentTypeEnum.values()) {
            if (componentTypeEnum.getCode().equals(code)) {
                return componentTypeEnum.getCodeName();
            }
        }
        return null;
    }
}
