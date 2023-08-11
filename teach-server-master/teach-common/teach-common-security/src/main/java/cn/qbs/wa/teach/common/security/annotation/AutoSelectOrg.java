package cn.qbs.wa.teach.common.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 　　* @description: 传参带有orgId,上下文自动注入orgId 可注解类(controller)所有方法生效或者单独方法
 　　* @author vieux
 　　* @date 2022/3/22 9:05
 　　*/
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
public @interface AutoSelectOrg {

}
