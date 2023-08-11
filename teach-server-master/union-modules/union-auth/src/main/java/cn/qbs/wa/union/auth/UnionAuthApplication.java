package cn.qbs.wa.union.auth;


import cn.qbs.wa.teach.common.core.constant.AppConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 认证授权中心
 */
@SpringBootApplication(scanBasePackages = {AppConstant.UNION_PROJECT_PACKAGES, AppConstant.TEACH_PROJECT_PACKAGES})
@EnableFeignClients(basePackages = {AppConstant.FEIGN_BASE_PACKAGES})
@EnableDiscoveryClient
public class UnionAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(UnionAuthApplication.class, args);

    }
}
