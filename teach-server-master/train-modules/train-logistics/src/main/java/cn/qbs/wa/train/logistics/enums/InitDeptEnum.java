package cn.qbs.wa.train.logistics.enums;

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
public enum InitDeptEnum {



    ORG_MASTER( "默认","default");

    @JsonValue
    private String name;

    private String code;

}
