package cn.qbs.wa.teach.auth;


import cn.qbs.wa.teach.common.core.constant.AppConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 认证授权中心
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {AppConstant.TEACH_PROJECT_PACKAGES}, exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients(basePackages = {AppConstant.FEIGN_BASE_PACKAGES})
public class TeachAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(TeachAuthApplication.class, args);

    }
}
