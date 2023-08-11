package cn.qbs.wa.teach.common.core.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 参数异常
 *
 * @author zcm
 * @version 1.0
 * @date 2021-11-04 11:10:43
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class IllegalParamsException extends RuntimeException {

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 错误明细，内部调试错误
     * <p>
     */
    private String detailMessage;


    public IllegalParamsException(String message) {
        this.message = message;
    }

}
