package cn.qbs.wa.train.allowance.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yjx
 */
@Configuration
public class SnowflakeConf {

    @Value("${snowflake.workId:1}")
    private long workId;

    @Bean
    public Snowflake setSnowflake() {
        return IdUtil.getSnowflake(workId, 1);
    }
}