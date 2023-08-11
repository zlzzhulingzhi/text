package cn.qbs.wa.teach.common.core.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author zcm
 * @Date 2022/6/28 15:56
 * @Version 1.0
 */
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    private DateTimeFormatter yyyyMMddHHmmssFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private DateTimeFormatter yyyyMMddHHmmFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private DateTimeFormatter yyyyMMddFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private DateTimeFormatter hHmmssFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String text = jsonParser.getText();
        LocalDateTime localDateTime = null;
        if (StringUtils.isNotBlank(text)) {
            if (text.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
                localDateTime = LocalDateTime.parse(text, yyyyMMddFormatter);
            } else if (text.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
                localDateTime = LocalDateTime.parse(text, yyyyMMddHHmmFormatter);
            } else if (text.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
                localDateTime = LocalDateTime.parse(text, yyyyMMddHHmmssFormatter);
            }

        }
        return localDateTime;
    }

}