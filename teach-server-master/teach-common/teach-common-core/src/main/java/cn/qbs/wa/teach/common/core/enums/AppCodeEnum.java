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
public enum AppCodeEnum {

    ADMIN("admin","统一平台管理"),

    ORG("org","统一机构管理"),

    LOGISTICS("logistics","教务中心"),

    TEACH("teach","培训管理");

    private final String code;

    private final String name;

    public static AppCodeEnum getEnumByCode(String code){
        AppCodeEnum[] values = AppCodeEnum.values();
        for (AppCodeEnum value : values) {
             if(value.getCode().equals(code)){
                 return value;
             }
        }
        return null;
    }

}
