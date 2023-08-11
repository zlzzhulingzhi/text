package cn.qbs.wa.teach.common.security.aspect;

import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.common.core.constant.TenantConstants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.utils.ServletUtils;
import cn.qbs.wa.teach.common.security.annotation.TenantFieldInject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 租户参数属性值设置
 *
 * @author yjx
 */

@Aspect
@Component
public class TenantFieldAspect {

    @Around("@annotation(tenantField)")
    public Object innerAround(ProceedingJoinPoint point, TenantFieldInject tenantField) throws Throwable {
        HttpServletRequest request = ServletUtils.getRequest();
        if (request != null) {
            String tenantFieldName = tenantField.value();
            String orgId = request.getHeader(TenantConstants.TENANT_ID);
            if (orgId != null && !"".equals(orgId.trim())) {
                for (Object arg : point.getArgs()) {
                    Map<String, Object> map = BeanUtil.beanToMap(arg);
                    if (map.containsKey(tenantFieldName)) {
                        BeanUtil.setFieldValue(arg, tenantFieldName, orgId);
                    }
                }
                SecurityContextHolder.setOrgId(orgId);
                SecurityContextHolder.setSelectOrgId(orgId);
            }
        }
        return point.proceed();
    }

}
