package cn.qbs.wa.train.allowance;

import cn.qbs.wa.teach.common.core.constant.AppConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients(basePackages = {AppConstant.FEIGN_BASE_PACKAGES})
@SpringBootApplication(scanBasePackages = {AppConstant.TRAIN_PROJECT_PACKAGES, AppConstant.TEACH_PROJECT_PACKAGES})
public class TrainAllowanceApplication {
  public static void main(String[] args) {
    SpringApplication.run(TrainAllowanceApplication.class, args);
  }
}
