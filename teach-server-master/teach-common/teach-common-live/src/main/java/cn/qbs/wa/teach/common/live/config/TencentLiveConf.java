package cn.qbs.wa.teach.common.live.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/12/13 16:38
 */
@Component
@ConfigurationProperties(prefix = "tencent.live")
@Data
public class TencentLiveConf {

    /**
     * API密钥中的 Secret Id
     */
    private String secretId;


    /**
     * API密钥中的 Secret Key
     */
    private String secretKey;

    /**
     * API密钥中的 推流鉴权key
     */
    private String pushKey;

    /**
     * API密钥中的 拉流鉴权key
     */
    private String pullKey;

    /**
     * 云直播控制台配置的推流域名
     */
    private String pushDomain;

    /**
     * 云直播控制台配置的拉流域名
     */
    private String pullDomain;

    /**
     * 直播SDK-->应用管理-->自己创建应用中的应用名称
     */
    private String AppName;

}
