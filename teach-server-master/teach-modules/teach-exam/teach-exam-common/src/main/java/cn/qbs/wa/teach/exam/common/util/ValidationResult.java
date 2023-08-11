package cn.qbs.wa.teach.exam.common.util;

import lombok.Data;

import java.text.MessageFormat;
import java.util.Map;

/**
 * @Author zcm
 * @Date 2022/5/20 11:03
 * @Version 1.0
 */
@Data
public class ValidationResult {

    /**
     * 是否有异常
     */
    private boolean hasErrors;

    /**
     * 异常消息记录
     */
    private Map<String, String> errorMsg;

    /**
     * 获取异常消息组装
     *
     * @return
     */
    public String getMessage() {
        if (errorMsg == null || errorMsg.isEmpty()) {
            return "";
        }
        StringBuilder message = new StringBuilder();
        errorMsg.forEach((key, value) -> {
            message.append(MessageFormat.format("{0}:{1} \r\n", key, value));
        });
        return message.toString();
    }

}
