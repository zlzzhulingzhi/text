package cn.qbs.wa.train.allowance.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author songzc
 * @desc 删除标识
 * @date 2022/5/26
 */
@Getter
@AllArgsConstructor
public enum DeleteEnum {

    NORMAL(0, "正常"),

    DELETED(1, "已删除");

    private final Integer value;

    private final String name;

}
