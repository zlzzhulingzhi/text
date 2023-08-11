package cn.qbs.wa.train.allowance.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FlowStatusEnum {

    /**
     * 1-审批中 2-通过 3-挂起 4-驳回
     */
    ING(1),

    PASS(2),

    STOP(3),

    REJECT(4),

    ;

    private final int code;

}
