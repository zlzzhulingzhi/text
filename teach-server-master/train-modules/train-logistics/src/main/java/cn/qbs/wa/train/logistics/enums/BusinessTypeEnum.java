package cn.qbs.wa.train.logistics.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据导入业务类型
 * @author WX
 */
@Getter
@AllArgsConstructor
public enum BusinessTypeEnum {


    /**
     * 学员
     */
    STUDENT(1),

    /**
     * 职工
     */
    EMPLOYEE(2),

    ;

    private final Integer code;

}
