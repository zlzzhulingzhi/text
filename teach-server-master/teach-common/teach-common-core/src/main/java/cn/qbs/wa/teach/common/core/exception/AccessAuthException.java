package cn.qbs.wa.teach.common.core.exception;

/**
 * 内部认证异常
 *
 * @author ruoyi
 */
public class AccessAuthException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AccessAuthException(String message) {
        super(message);
    }
}
