package cn.qbs.wa.train.screen;

import cn.qbs.wa.teach.common.core.constant.AppConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {AppConstant.TRAIN_PROJECT_PACKAGES, AppConstant.TEACH_PROJECT_PACKAGES})
public class TrainScreenApplication {
    public static void main(String[] args) {
        SpringApplication.run(TrainScreenApplication.class, args);
    }
}