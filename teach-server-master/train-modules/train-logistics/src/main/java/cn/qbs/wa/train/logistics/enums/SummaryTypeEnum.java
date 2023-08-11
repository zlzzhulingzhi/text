package cn.qbs.wa.train.logistics.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum SummaryTypeEnum {


    TOTAL("total"),

    USE("use"),

    FREE("free"),

    ;

    private final String code;

}
