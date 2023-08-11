package cn.qbs.wa.train.allowance.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FlowNodeStatusEnum {

    /**
    * 10-未开始 11-进行中 21-通过 22-未通过
    */
    NOT_START(10),

    IN_PROGRESS(11),

    PASS(21),

    NOT_PASS(22),

    ;

    private final int code;

}
