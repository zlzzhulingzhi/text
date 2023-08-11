package cn.qbs.wa.teach.common.live.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/1/12 16:47
 */

@Component
@ConfigurationProperties(prefix = "waproxy")
@Data
public class WaProxyConf {

    Boolean enable=false;

    String ip;

    Integer port;
}
