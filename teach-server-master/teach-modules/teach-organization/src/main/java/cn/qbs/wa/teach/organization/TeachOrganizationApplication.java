package cn.qbs.wa.teach.organization;

import cn.qbs.wa.teach.common.core.constant.AppConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Author zcm
 * @Date 2021/11/5 09:49
 * @Version 1.0
 */
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {AppConstant.FEIGN_BASE_PACKAGES})
@SpringBootApplication(scanBasePackages = AppConstant.TEACH_PROJECT_PACKAGES)
@EnableAsync
public class TeachOrganizationApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeachOrganizationApplication.class, args);
    }

}
