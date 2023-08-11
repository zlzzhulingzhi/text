package cn.qbs.wa.teach.common.security.interceptor;

import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.constant.SecurityConstants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.LoginUser;
import cn.qbs.wa.teach.common.core.utils.ServletUtils;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.common.security.auth.AuthUtil;
import cn.qbs.wa.teach.common.security.utils.SecurityUtils;
import org.slf4j.MDC;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义请求头拦截器，将Header数据封装到线程变量中方便获取
 * 注意：此拦截器会同时验证当前用户有效期自动刷新有效期
 *
 * @author ruoyi
 */
public class HeaderInterceptor implements AsyncHandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        SecurityContextHolder.setUserId(ServletUtils.getHeader(request, SecurityConstants.DETAILS_USER_ID));
        SecurityContextHolder.setUserName(ServletUtils.getHeader(request, SecurityConstants.DETAILS_USERNAME));
        SecurityContextHolder.setUserKey(ServletUtils.getHeader(request, SecurityConstants.USER_KEY));
        SecurityContextHolder.setUserType(ServletUtils.getHeader(request, SecurityConstants.USER_TYPE));
        String token = SecurityUtils.getToken();
        if (StringUtils.isNotEmpty(token)) {
            LoginUser loginUser = AuthUtil.getLoginUser(token);
            if (StringUtils.isNotNull(loginUser)) {
                AuthUtil.verifyLoginUserExpire(loginUser);

                if (loginUser.getOrgId() != null) {
                    SecurityContextHolder.setOrgId(loginUser.getOrgId().toString());
                }
                if (loginUser.getEmployeeId() != null) {
                    SecurityContextHolder.setEmployeeId(loginUser.getEmployeeId().toString());
                }
                if (loginUser.getUserid() != null) {
                    SecurityContextHolder.setUserId(loginUser.getUserid().toString());
                }
                SecurityContextHolder.set(SecurityConstants.LOGIN_USER, loginUser);
            }
        }
        MDC.put(Constants.TRACE_ID_MDC_FIELD, ServletUtils.getHeader(request, Constants.TRACE_ID_HTTP_FIELD));

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        SecurityContextHolder.remove();
    }
}
