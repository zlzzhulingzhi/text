package cn.qbs.wa.teach.common.security.annotation;

import java.lang.annotation.*;

/**
 * 允许访问用户类型注解
 *
 * @author yjx
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessAuth {
    /**
     * 类型
     */
    String[] value();

    /**
     * 验证模式：AND | OR，默认OR
     */
    Logical logical() default Logical.OR;
}