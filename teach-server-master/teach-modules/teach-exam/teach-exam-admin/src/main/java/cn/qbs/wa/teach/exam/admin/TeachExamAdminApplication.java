package cn.qbs.wa.teach.exam.admin;

import cn.qbs.wa.teach.common.core.constant.AppConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {AppConstant.TEACH_PROJECT_PACKAGES})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {AppConstant.FEIGN_BASE_PACKAGES})
public class TeachExamAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeachExamAdminApplication.class, args);
    }

}
