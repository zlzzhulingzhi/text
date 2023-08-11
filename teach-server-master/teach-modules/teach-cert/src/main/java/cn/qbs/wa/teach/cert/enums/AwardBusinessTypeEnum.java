package cn.qbs.wa.teach.cert.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum AwardBusinessTypeEnum {

    /**
     * 所有可见
     */
    EXAM(1, "考试");



    private final Integer id;

    private final String typeName;


}

