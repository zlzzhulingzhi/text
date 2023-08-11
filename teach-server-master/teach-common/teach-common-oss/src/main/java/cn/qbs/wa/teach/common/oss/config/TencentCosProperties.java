package cn.qbs.wa.teach.common.oss.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author yjx
 */
@RefreshScope
@ConfigurationProperties(prefix = "tencent.cos")
@Data
public class TencentCosProperties {

    /**
     * API密钥中的 Secret Id
     */
    private String secretId;

    /**
     * API密钥中的 Secret Key
     */
    private String secretKey;

    /**
     * 存储桶地区
     */
    private String region;

    /**
     * 存储桶
     */
    private String bucket;

    /**
     * 代理IP
     */
    private String proxyHost;

    /**
     * 代理端口
     */
    private String proxyPort;

    /**
     * 预览前缀
     */
    private String viewPrefix;
}
