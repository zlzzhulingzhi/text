package cn.qbs.wa.teach.question.elasticsearch;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author zcm
 * @Date 2021/11/4 15:01
 * @Version 1.0
 */
@Configuration
@EnableConfigurationProperties(ElasticSearchProperties.class)
public class ElasticSearchConfig {

    @Autowired
    private ElasticSearchProperties elasticSearchProperties;

    public static final RequestOptions COMMON_OPTIONS;

    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        // builder.addHeader("Authorization", "Bearer " + TOKEN);
        // builder.setHttpAsyncResponseConsumerFactory(
        //         new HttpAsyncResponseConsumerFactory
        //                 .HeapBufferedResponseConsumerFactory(30 * 1024 * 1024 * 1024));
        COMMON_OPTIONS = builder.build();
    }

    @Bean
    public RestHighLevelClient esRestClient() {
        RestClientBuilder builder = RestClient.builder(
                new HttpHost(elasticSearchProperties.getHostname(), elasticSearchProperties.getPort(), elasticSearchProperties.getScheme())
        );

        // 异步httpclient连接数配置
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
                    CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                    if (StringUtils.isNotBlank(elasticSearchProperties.getUsername())) {
                        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(
                                elasticSearchProperties.getUsername(), elasticSearchProperties.getPassword()));
                    }
                    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    httpClientBuilder.setMaxConnTotal(elasticSearchProperties.getMaxConnectNum());
                    httpClientBuilder.setMaxConnPerRoute(elasticSearchProperties.getMaxConnectPerRoute());
                    return httpClientBuilder;
                }
        );

        // 异步httpclient连接延时配置
        builder.setRequestConfigCallback((requestConfigBuilder) -> {
                    requestConfigBuilder.setConnectTimeout(elasticSearchProperties.getConnectTimeOut());
                    requestConfigBuilder.setSocketTimeout(elasticSearchProperties.getSocketTimeOut());
                    requestConfigBuilder.setConnectionRequestTimeout(elasticSearchProperties.getConnectionRequestTimeOut());
                    return requestConfigBuilder;
                }
        );

        return new RestHighLevelClient(builder);
    }

}
