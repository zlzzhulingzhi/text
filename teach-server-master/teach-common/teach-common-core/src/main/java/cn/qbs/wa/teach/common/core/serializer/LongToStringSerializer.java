package cn.qbs.wa.teach.common.core.serializer;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/1/7 15:52
 */
public class LongToStringSerializer  extends JsonSerializer<Long> {
    @Override
    public void serialize(Long value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(value == null ? StrUtil.EMPTY : String.valueOf(value));
    }
}
