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
public enum EnabledEnum {

    DISABLE(0,"禁用"),
    ENABLED(1,"启用");

    private final Integer value;

    private final String name;

}
