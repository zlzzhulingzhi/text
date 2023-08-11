package cn.qbs.wa.teach.utils;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.beans.Introspector;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/6/16 10:46
 */
public class CommonSetInfoUtil {

    /**
     　　* @description: 根据source对象数组的值注入target对象数组,key为他们判断的同一个的标准,content是要注入的值
     　　* @author vieux
     　　* @date 2022/6/16 13:54
     　　*/
    public static <S, T>void setInfo(List<S> sourceList, List<T> targetList, SFunction<S, Object> sourceKey, SFunction<T, Object> targetKey, SFunction<S, Object> sourceContent, SFunction<T, Object> targetContent) {

        if (CollUtil.isNotEmpty(sourceList) && CollUtil.isNotEmpty(targetList)) {
            String propertyKey = getPropertyName(sourceKey);
            String propertyTargetKey = getPropertyName(targetKey);
            String propertyContent = getPropertyName(sourceContent);
            String propertyTargetContent = getPropertyName(targetContent);

            for (T t : targetList) {
                for (S s : sourceList) {
                    try {
                        if (ReflectUtil.getFieldValue(t, propertyTargetKey).equals(ReflectUtil.getFieldValue(s, propertyKey))) {
                            ReflectUtil.setFieldValue(t, propertyTargetContent, ReflectUtil.getFieldValue(s, propertyContent));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }

    }



    public  static <T> String getPropertyName(SFunction<T, Object> lambda) {
        try {
            Class lambdaClass = lambda.getClass();
            Method method = lambdaClass.getDeclaredMethod("writeReplace");
            //writeReplace是私有方法，需要去掉私有属性
            method.setAccessible(Boolean.TRUE);
            //手动调用writeReplace()方法，返回一个SerializedLambda对象
            SerializedLambda serializedLambda = (SerializedLambda) method.invoke(lambda);
            //得到lambda表达式中调用的方法名，如 "User::getSex"，则得到的是"getSex"
            String getterMethod = serializedLambda.getImplMethodName();
            //去掉”get"前缀，最终得到字段名“sex"
            String fieldName = Introspector.decapitalize(getterMethod.replace("get", ""));
            return fieldName;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }


}
