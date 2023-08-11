package cn.qbs.wa.teach.common.security.aspect;

import cn.qbs.wa.teach.common.core.constant.SecurityConstants;
import cn.qbs.wa.teach.common.core.constant.UserConstants;
import cn.qbs.wa.teach.common.core.exception.AccessAuthException;
import cn.qbs.wa.teach.common.core.utils.ServletUtils;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.common.security.annotation.AccessAuth;
import cn.qbs.wa.teach.common.security.annotation.Logical;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * 允许访问用户类型验证处理(employee、student、inner)
 *
 * @author yjx
 */
@Slf4j
@Aspect
@Component
public class AccessAuthAspect implements Ordered {

    @Around("@annotation(accessAuth)")
    public Object innerAround(ProceedingJoinPoint point, AccessAuth accessAuth) throws Throwable {
        String userType = ServletUtils.getRequest().getHeader(SecurityConstants.USER_TYPE);
        String source = ServletUtils.getRequest().getHeader(SecurityConstants.FROM_SOURCE);
        String[] types = accessAuth.value();
        boolean pass = false;
        if (Logical.OR == accessAuth.logical()) {
            for (String type : types) {
                // 用户类型请求认证
                if (StringUtils.equals(type, userType) || StringUtils.equals(type, source)) {
                    pass = true;
                    break;
                }
            }
        } else {
            pass = true;
            for (String type : types) {
                // 用户类型请求认证
                if (UserConstants.USER_EMPLOYEE.equals(type) || UserConstants.USER_STUDENT.equals(type)) {
                    if (!StringUtils.equals(type, userType)) {
                        pass = false;
                        break;
                    }
                } else {
                    if (!StringUtils.equals(type, source)) {
                        pass = false;
                        break;
                    }
                }
            }
        }
        if (!pass) {
            String requestURI = ServletUtils.getRequest().getRequestURI();
            log.error("没有访问权限，不允许访问 {requestURI: {}, args: {}, accessAuth: {}, userType: {}, source: {}}",
                    requestURI, JSON.toJSON(point.getArgs()), JSON.toJSONString(accessAuth), userType, source);
            throw new AccessAuthException("没有访问权限，不允许访问");
        }
        return point.proceed();
    }

    /**
     * 确保在权限认证aop执行前执行
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
