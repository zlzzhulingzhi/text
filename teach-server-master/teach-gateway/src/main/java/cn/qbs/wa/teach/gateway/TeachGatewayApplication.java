package cn.qbs.wa.teach.gateway;


import cn.qbs.wa.teach.common.core.constant.AppConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 认证授权中心
 */
@SpringBootApplication(scanBasePackages = {AppConstant.TEACH_PROJECT_PACKAGES}, exclude = {DataSourceAutoConfiguration.class})
public class TeachGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(TeachGatewayApplication.class, args);
    }
}
