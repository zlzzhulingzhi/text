package cn.qbs.wa.teach.common.security.aspect;

import cn.hutool.core.util.DesensitizedUtil;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.teach.common.security.annotation.EncodeContent;
import cn.qbs.wa.teach.common.security.enums.SensitiveTypeEnum;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Objects;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/15 17:26
 */

@NoArgsConstructor
@AllArgsConstructor
public class EncodeContentSerializer extends JsonSerializer<Object> implements ContextualSerializer  {

    boolean desensitized;

    boolean decode;

    SensitiveTypeEnum desensitizedType;

    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (o != null) {
            String decodeContent = o.toString();
            if (decode) {
                String content = o.toString();
                decodeContent = AesUtil.decode(content);
            }
            if (desensitized) {
                switch (desensitizedType) {
                    case ID_CARD:
                        decodeContent =   DesensitizedUtil.idCardNum(decodeContent, 1, 2);
                        break;
                    case MOBILE_PHONE:
                        decodeContent = DesensitizedUtil.mobilePhone(decodeContent);
                        break;
                }
            }
            jsonGenerator.writeString(decodeContent);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) {
            if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
                EncodeContent encodeContent = beanProperty.getAnnotation(EncodeContent.class);
                if (encodeContent == null) {
                    encodeContent = beanProperty.getContextAnnotation(EncodeContent.class);
                }
                if (encodeContent != null) {
                    return new EncodeContentSerializer(encodeContent.desensitized(), encodeContent.decode(),
                            encodeContent.desensitizedType());
                }
            }
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        return serializerProvider.findNullValueSerializer(null);

    }
}
