package cn.qbs.wa.teach.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AllowanceFeeEnum {

    PXCSZZ("pxcszzzj", "培训场所资助租金"),

    XYJCZZ("xyjczz", "学员就餐资助费用"),

    XYZSZZ("xyzszz", "学员住宿资助费用"),

    XYJTZZ("xyjtzz", "学员交通资助费用"),

    XYXFZZ("xyxfzz", "学员学费资助金额");

    private final String code;

    private final String name;

    public static AppCodeEnum getEnumByCode(String code) {
        AppCodeEnum[] values = AppCodeEnum.values();
        for (AppCodeEnum value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
