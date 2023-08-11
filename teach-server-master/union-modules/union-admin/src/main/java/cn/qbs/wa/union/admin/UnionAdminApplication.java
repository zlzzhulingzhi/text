package cn.qbs.wa.union.admin;

import cn.qbs.wa.teach.common.core.constant.AppConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 认证授权中心
 */
@SpringBootApplication(scanBasePackages = {AppConstant.UNION_PROJECT_PACKAGES, AppConstant.TEACH_PROJECT_PACKAGES})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {AppConstant.FEIGN_BASE_PACKAGES})
public class UnionAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(UnionAdminApplication.class, args);
    }
}
