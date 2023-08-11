package cn.qbs.wa.teach.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 *
 * @Author zcm
 * @Date 2022-03-28 17:33
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum UniClientCodeEnum {

    ADMIN("admin","管理员"),

    ORG("org","机构职工");

    private final String code;

    private final String name;

    public static UniClientCodeEnum getEnumByCode(String code){
        UniClientCodeEnum[] values = UniClientCodeEnum.values();
        for (UniClientCodeEnum value : values) {
             if(value.getCode().equals(code)){
                 return value;
             }
        }
        return null;
    }

}
