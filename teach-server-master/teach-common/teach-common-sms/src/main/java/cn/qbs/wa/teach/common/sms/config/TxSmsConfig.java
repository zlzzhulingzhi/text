package cn.qbs.wa.teach.common.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 9:19
 */

@ConfigurationProperties(prefix = "tx.sms")
@Configuration
@Data
public class TxSmsConfig extends SmsConfig{

    String secretId;
    String secretKey;
    String appId;
    String sign;
    String templateId;

}
