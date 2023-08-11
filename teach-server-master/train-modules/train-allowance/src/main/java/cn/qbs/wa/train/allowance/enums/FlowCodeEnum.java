package cn.qbs.wa.train.allowance.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FlowCodeEnum {

    /**
     * 机构入驻申请
     */
    ORG_SETTLE("JGRZ"),

    /**
     * 机构资助资质
     */
    ORG_QUALIFICATION("JGZZ"),

    /**
     * 机构资金补助
     */
    ORG_ALLOWANCE("ZJBZ"),

    /**
     * 机构学术活动
     */
    ORG_ACTIVITY("XSHD"),

    ;

    private final String code;

    public static FlowCodeEnum getEnumByCode(String code) {
        if (code == null) {
            return null;
        }
        for (FlowCodeEnum flowCodeEnum : FlowCodeEnum.values()) {
            if (flowCodeEnum.getCode().equals(code)) {
                return flowCodeEnum;
            }
        }
        return null;
    }
}
