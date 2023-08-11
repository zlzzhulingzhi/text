package cn.qbs.wa.teach.common.redisson.config;

import cn.qbs.wa.teach.common.redisson.codec.FastJsonCodec;
import cn.qbs.wa.teach.common.redisson.operation.RedissonBinary;
import cn.qbs.wa.teach.common.redisson.operation.RedissonCollection;
import cn.qbs.wa.teach.common.redisson.operation.RedissonObject;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
@EnableConfigurationProperties(value = RedissonProperties.class)
@ConditionalOnClass(RedissonProperties.class)
public class RedissonConfiguration {

    @Autowired
    private RedissonProperties redissonProperties;


    @Bean
    @ConditionalOnMissingBean(RedissonBinary.class)
    public RedissonBinary RedissonBinary() {
        return new RedissonBinary();
    }

    @Bean
    @ConditionalOnMissingBean(RedissonObject.class)
    public RedissonObject RedissonObject() {
        return new RedissonObject();
    }

    @Bean
    @ConditionalOnMissingBean(RedissonCollection.class)
    public RedissonCollection RedissonCollection() {
        return new RedissonCollection();
    }

    @Bean
    @ConditionalOnMissingBean(RedissonClient.class)
    public RedissonClient redissonClient() {
        Config config = new Config();
        try {
            config.setCodec(new FastJsonCodec());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        config.setTransportMode(redissonProperties.getTransportMode());
        if (redissonProperties.getThreads() != null) {
            config.setThreads(redissonProperties.getThreads());
        }
        if (redissonProperties.getNettyThreads() != null) {
            config.setNettyThreads(redissonProperties.getNettyThreads());
        }
        config.setReferenceEnabled(redissonProperties.getReferenceEnabled());
        config.setLockWatchdogTimeout(redissonProperties.getLockWatchdogTimeout());
        config.setKeepPubSubOrder(redissonProperties.getKeepPubSubOrder());
        config.setUseScriptCache(redissonProperties.getUseScriptCache());
        config.setMinCleanUpDelay(redissonProperties.getMinCleanUpDelay());
        config.setMaxCleanUpDelay(redissonProperties.getMaxCleanUpDelay());

        initSingleServerConfig(config);

        return Redisson.create(config);
    }

    private void initSingleServerConfig(Config config) {
        SingleServerConfig singleServerConfig = config.useSingleServer();
        cn.qbs.wa.teach.common.redisson.config.SingleServerConfig param = redissonProperties.getSingleServerConfig();
        singleServerConfig.setAddress(prefixAddress(param.getAddress()));
        singleServerConfig.setConnectionMinimumIdleSize(param.getConnectionMinimumIdleSize());
        singleServerConfig.setConnectionPoolSize(param.getConnectionPoolSize());
        singleServerConfig.setDatabase(param.getDatabase());
        singleServerConfig.setDnsMonitoringInterval(param.getDnsMonitoringInterval());
        singleServerConfig.setSubscriptionConnectionMinimumIdleSize(param.getSubscriptionConnectionMinimumIdleSize());
        singleServerConfig.setSubscriptionConnectionPoolSize(param.getSubscriptionConnectionPoolSize());
        singleServerConfig.setClientName(redissonProperties.getClientName());
        singleServerConfig.setConnectTimeout(redissonProperties.getConnectTimeout());
        singleServerConfig.setIdleConnectionTimeout(redissonProperties.getIdleConnectionTimeout());
        singleServerConfig.setKeepAlive(redissonProperties.getKeepAlive());
        singleServerConfig.setPassword(redissonProperties.getPassword());
        singleServerConfig.setPingConnectionInterval(redissonProperties.getPingConnectionInterval());
        singleServerConfig.setRetryAttempts(redissonProperties.getRetryAttempts());
        singleServerConfig.setRetryInterval(redissonProperties.getRetryInterval());
        singleServerConfig.setSslEnableEndpointIdentification(redissonProperties.getSslEnableEndpointIdentification());
        singleServerConfig.setSslKeystore(redissonProperties.getSslKeystore());
        singleServerConfig.setSslKeystorePassword(redissonProperties.getSslKeystorePassword());
        singleServerConfig.setSslProvider(redissonProperties.getSslProvider());
        singleServerConfig.setSslTruststore(redissonProperties.getSslTruststore());
        singleServerConfig.setSslTruststorePassword(redissonProperties.getSslTruststorePassword());
        singleServerConfig.setSubscriptionsPerConnection(redissonProperties.getSubscriptionsPerConnection());
        singleServerConfig.setTcpNoDelay(redissonProperties.getTcpNoDelay());
        singleServerConfig.setTimeout(redissonProperties.getTimeout());
    }

    private String prefixAddress(String address) {
        if (!StringUtils.isEmpty(address) && !address.startsWith("redis")) {
            return "redis://" + address;
        }
        return address;
    }
}
