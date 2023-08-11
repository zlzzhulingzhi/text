package cn.qbs.wa.train.basic;


import cn.qbs.wa.teach.common.core.constant.AppConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication(scanBasePackages = {AppConstant.TRAIN_PROJECT_PACKAGES, AppConstant.TEACH_PROJECT_PACKAGES})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {AppConstant.FEIGN_BASE_PACKAGES})
public class TrainBasicApplication {
    public static void main(String[] args) {
        SpringApplication.run(TrainBasicApplication.class, args);

    }
}
