package cn.qbs.wa.teach.common.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/2 8:58
 */

@ConfigurationProperties(prefix = "ali.sms")
@Configuration
@Data
public class AliSmsConfig extends SmsConfig{

    private String regionId= "cn-hangzhou";;
    private String accessKeyId;
    private String accessSecret;
    private String doMain="dysmsapi.aliyuncs.com";
    private String version="2017-05-25";
    private String action="SendSms";
    private String signName;
    private String templateCode;

//    private String regionId = "cn-hangzhou";
//    private String accessKeyId = "accessKeyId";
//    private String accessSecret = "accessSecret";
//    private String signName = "签名名称";
//    private String templateCode = "模板id";


}
