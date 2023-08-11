package cn.qbs.wa.train.screen.enums;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DataOverviewItemEnum {

    KBKC("开班课程"),

    PXRC("培训人次"),

    RWJG("入围机构"),

    ZZJG("资助机构"),

    ZZBC("资助班次"),

    ZZRC("资助人次"),

    ;

    private final String displayName;

    /**
     * 根据代号获取枚举名
     * @param displayName 枚举代号
     * @return 枚举名
     */
    public static String getCodeName(String displayName) {
        if (StrUtil.isBlank(displayName)) {
            return null;
        }
        for (DataOverviewItemEnum visibleStatusEnum : DataOverviewItemEnum.values()) {
            if (visibleStatusEnum.getDisplayName().equals(displayName)) {
                return visibleStatusEnum.name();
            }
        }
        return null;
    }
}
