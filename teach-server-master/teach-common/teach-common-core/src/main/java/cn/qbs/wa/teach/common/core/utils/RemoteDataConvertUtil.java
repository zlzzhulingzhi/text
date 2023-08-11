package cn.qbs.wa.teach.common.core.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.qbs.wa.teach.common.core.domain.R;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RemoteDataConvertUtil {

    public static  <T> T unboxingRemoteCallData(R<T> data) {
        log.info("远程调用接口响应结果：{}", JSON.toJSONString(data));
        T obj = data.getData();
        if (R.SUCCESS == data.getCode() && ObjectUtil.isNotEmpty(obj)) {
            return obj;
        }
        return null;
    }

    public static String unboxingRemoteCallMsg(R<?> result) {
        log.info("远程调用接口响应结果：{}", JSON.toJSONString(result));
        if (R.SUCCESS == result.getCode()) {
            return null;
        }
        return result.getMsg();
    }
}
