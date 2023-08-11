package cn.qbs.wa.teach.common.live.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author yjx
 */
@Component
@ConfigurationProperties(prefix = "tencent.vod")
@Data
public class TencentVodConf {

    /**
     * API密钥中的 Secret Id
     */
    private String secretId;

    /**
     * API密钥中的 Secret Key
     */
    private String secretKey;


    /**
     * 子应用id
     */
    private String subAppId;


}
