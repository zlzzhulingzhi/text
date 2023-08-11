package cn.qbs.wa.teach.common.core.exception.exam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 考试业务异常
 *
 * @author yjx
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class ExamServiceException extends RuntimeException {

    /**
     * 错误码
     */
    private int code = 801;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 错误明细，内部调试错误
     */
    private String detailMessage;


    public ExamServiceException(String message) {
        this.message = message;
    }

    public ExamServiceException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}