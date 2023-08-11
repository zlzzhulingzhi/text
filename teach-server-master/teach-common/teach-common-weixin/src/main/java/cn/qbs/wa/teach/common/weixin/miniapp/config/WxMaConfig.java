package cn.qbs.wa.teach.common.weixin.miniapp.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yjx
 */
@Slf4j
@AllArgsConstructor
@Configuration
@EnableConfigurationProperties(WxMaProperties.class)
public class WxMaConfig {

    private final WxMaProperties properties;

    @Bean
    public WxMaService wxMaService() {
        List<WxMaProperties.Config> configs = this.properties.getConfigs();
        WxMaService maService = new WxMaServiceImpl();
        if (configs == null) {
            return maService;
        }
        maService.setMultiConfigs(
                configs.stream()
                        .map(a -> {
                            WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
                            config.setAppid(a.getAppid());
                            config.setSecret(a.getSecret());
                            config.setToken(StringUtils.trimToNull(a.getToken()));
                            config.setAesKey(StringUtils.trimToNull(a.getAesKey()));
                            config.setMsgDataFormat(StringUtils.trimToNull(a.getMsgDataFormat()));
                            return config;
                        }).collect(Collectors.toMap(WxMaDefaultConfigImpl::getAppid, a -> a, (o, n) -> o)));
        return maService;
    }

}
