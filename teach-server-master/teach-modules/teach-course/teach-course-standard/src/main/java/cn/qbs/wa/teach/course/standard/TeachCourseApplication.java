package cn.qbs.wa.teach.course.standard;

import cn.qbs.wa.teach.common.core.constant.AppConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author yjx
 */
@EnableAsync
@EnableDiscoveryClient
@EnableFeignClients(basePackages = AppConstant.FEIGN_BASE_PACKAGES)
@SpringBootApplication(scanBasePackages = {AppConstant.TEACH_PROJECT_PACKAGES})
public class TeachCourseApplication {
    public static void main(String[] args) {
        SpringApplication.run(TeachCourseApplication.class, args);
    }
}
