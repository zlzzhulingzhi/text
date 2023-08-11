package cn.qbs.wa.teach.out.union.admin.api.enums;

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
public enum UnionAppCodeEnum {

    ADMIN("admin","管理员"),

    LOGISTICS("logistics","教务系统"),

    TEACH("teach","培训系统");

    private final String code;

    private final String name;

    public static UnionAppCodeEnum getEnumByCode(String code){
        UnionAppCodeEnum[] values = UnionAppCodeEnum.values();
        for (UnionAppCodeEnum value : values) {
             if(value.getCode().equals(code)){
                 return value;
             }
        }
        return null;
    }

}
