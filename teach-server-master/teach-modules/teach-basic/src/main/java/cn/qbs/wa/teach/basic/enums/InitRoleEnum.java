package cn.qbs.wa.teach.basic.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 @description: TODO
 @author vieux
 @date 2021/11/10 15:07
 *
 */
@Getter
@AllArgsConstructor
public enum InitRoleEnum {

    PLAT_MANAGER( "基地管理员","plat_manager"),

    ADMIN( "平台管理员","admin");

    @JsonValue
    private final String name;

    private final String code;



}
