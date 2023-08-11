package cn.qbs.wa.teach.common.security.annotation;


import cn.qbs.wa.teach.common.security.aspect.EncodeContentSerializer;
import cn.qbs.wa.teach.common.security.enums.SensitiveTypeEnum;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@JacksonAnnotationsInside
@JsonSerialize(using = EncodeContentSerializer.class)
public @interface EncodeContent {

    /**
     *  脱敏手机号码
     */
    boolean desensitized() default false;

    boolean decode() default true;

    SensitiveTypeEnum desensitizedType() default SensitiveTypeEnum.MOBILE_PHONE;

}
