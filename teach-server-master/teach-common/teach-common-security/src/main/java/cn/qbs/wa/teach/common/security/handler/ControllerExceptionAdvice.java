package cn.qbs.wa.teach.common.security.handler;

import cn.qbs.wa.teach.common.core.constant.HttpStatus;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.AccessAuthException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.exception.auth.NotPermissionException;
import cn.qbs.wa.teach.common.core.exception.auth.NotRoleException;
import cn.qbs.wa.teach.common.core.exception.exam.ExamServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.sql.SQLException;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 19:46
 */
@Slf4j
@RestControllerAdvice
public class ControllerExceptionAdvice {

    /**
     * 权限码异常
     */
    @ExceptionHandler({NotPermissionException.class, NotRoleException.class})
    public R<String> handleNotPermissionException(RuntimeException e, HttpServletRequest request) {
        if (e instanceof NotPermissionException) {
            log.error("请求地址'{}',权限码校验失败'{}'", request.getRequestURI(), e.getMessage());
        } else if (e instanceof NotRoleException) {
            log.error("请求地址'{}',无角色权限'{}'", request.getRequestURI(), e.getMessage());
        }
        return R.fail(HttpStatus.FORBIDDEN, "没有访问权限，请联系管理员授权");
    }

    @ExceptionHandler(AccessAuthException.class)
    public R<?> handleInnerAuthException(AccessAuthException e) {
        return R.fail(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(value = ServiceException.class)
    public R<String> serviceExceptionHandler(ServiceException ex) {
        log.error("业务异常:", ex);
        return R.fail(ex.getMessage());
    }

    @ExceptionHandler(value = SQLException.class)
    public R<String> sqlExceptionHandler(SQLException ex) {
        log.error("数据库操作异常:", ex);
        return R.fail("数据库操作异常！");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<?> handleArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("参数校验异常:", e);
        return R.fail(e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public R<?> handleValidationException(ValidationException e) {
        log.error("参数校验异常:", e);
        return R.fail(e.getMessage());
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public R<String> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.error("请求地址'{}',发生未知异常.", request.getRequestURI(), e);
        return R.fail("抱歉，服务开小差了！");
    }

    @ExceptionHandler(value = Exception.class)
    public R<String> exceptionHandler(Exception e, HttpServletRequest request) {
        log.error("请求地址'{}',发生未知异常.", request.getRequestURI(), e);
        return R.fail("抱歉，服务开小差了！");
    }
}
