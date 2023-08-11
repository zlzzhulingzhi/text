package cn.qbs.wa.teach.common.live.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * TRTC的配置信息
 *
 * @author lance
 * @version 1.0
 * @date 2021/12/24
 */
@Component
@ConfigurationProperties(prefix = "tencent.trtcmonitollive")
@Data
public class TencentTRTCMonitorLiveConf {

    /**
     * SDKAppId
     */
    private String appId;

    /**
     * 应用名称
     */
    private String appName;

    /**
     *  UserSig的密钥
     */
    private String priKey;


}
