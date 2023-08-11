package cn.qbs.wa.teach.organization.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 @author vieux
 @date 2021/11/10 15:07
 *
 */
@Getter
@AllArgsConstructor
public enum InitRoleEnum {



    ORG_MASTER( "机构管理员","orgMaster"),
    ;

    @JsonValue
    private final String name;

    private final String code;



}
