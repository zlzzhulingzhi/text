package cn.qbs.wa.teach.common.security.aspect;

import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 *
 *
 * @author vieux
 * @version 1.0
 * @date 2022/3/21 20:39
 */

@Component
@Aspect
public class SelectOrgAspect {

    @Pointcut("execution( @cn.qbs.wa.teach.common.security.annotation.AutoSelectOrg * *(..)) ||"+"within(@cn.qbs.wa.teach.common.security.annotation.AutoSelectOrg *)")
    public void aopOrg() {

    }

    @Before(value="aopOrg()")
    public void setSelectOrgId(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 当前被切点切到的方法
        Method method = signature.getMethod();
        // 参数名称
        String[] parameterNames = signature.getParameterNames();
        // 参数值
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(arg));
            if(jsonObject.containsKey("orgId")&& StrUtil.isNotBlank(jsonObject.getString("orgId"))){
                SecurityContextHolder.setOrgId(jsonObject.getString("orgId"));
            }
        }
    }





}
