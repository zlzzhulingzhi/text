package cn.qbs.wa.teach.question.elasticsearch;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @Author zcm
 * @Date 2021/11/4 15:01
 * @Version 1.0
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = "elasticsearch")
public class ElasticSearchProperties {

    private String hostname = "127.0.0.1";
    private int port = 9200;
    private String scheme = "http";

    private String username;
    private String password;

    /**
     * 连接超时时间
     */
    private int connectTimeOut = 1000;

    /**
     * Socket连接超时时间
     */
    private int socketTimeOut = 30000;

    /**
     * 获取连接的超时时间
     */
    private int connectionRequestTimeOut = 500;

    /**
     * 最大连接数
     */
    private int maxConnectNum = 100;

    /**
     * 最大路由连接数
     */
    private int maxConnectPerRoute = 100;

}
