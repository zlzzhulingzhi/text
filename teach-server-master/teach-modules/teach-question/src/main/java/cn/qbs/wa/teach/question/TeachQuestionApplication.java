package cn.qbs.wa.teach.question;

import cn.qbs.wa.teach.common.core.constant.AppConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Author zcm
 * @Date 2021/10/28 13:37
 * @Version 1.0
 */
@EnableScheduling
@EnableFeignClients(basePackages= AppConstant.FEIGN_BASE_PACKAGES)
@SpringBootApplication(scanBasePackages = AppConstant.TEACH_PROJECT_PACKAGES)
public class TeachQuestionApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeachQuestionApplication.class, args);
    }

}
