package cn.qbs.wa.teach.exam.consumer;

import cn.qbs.wa.teach.common.core.constant.AppConstant;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = {AppConstant.TEACH_PROJECT_PACKAGES})
@EnableDiscoveryClient
@EnableAsync
@EnableFeignClients(basePackages = {AppConstant.FEIGN_BASE_PACKAGES})
public class TeachExamConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeachExamConsumerApplication.class, args);
    }

}
