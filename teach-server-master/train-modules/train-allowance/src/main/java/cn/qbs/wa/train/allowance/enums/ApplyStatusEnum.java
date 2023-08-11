package cn.qbs.wa.train.allowance.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplyStatusEnum {

    /**
     * 0 未提交 ,   1 提交
     */

    NOT_SUBMIT(0),

    SUBMIT(1),

    INVALIDATED(2);

    private final Integer code;
}
