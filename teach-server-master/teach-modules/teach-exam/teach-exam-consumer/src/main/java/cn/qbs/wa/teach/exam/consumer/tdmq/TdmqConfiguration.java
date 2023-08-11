package cn.qbs.wa.teach.exam.consumer.tdmq;

import cn.qbs.wa.teach.exam.common.constant.Topics;
import com.qbs.tdmq.constant.Serialization;
import com.qbs.tdmq.producer.TdmqProducerFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author zcm
 * @Date 2021-12-07 19:01
 * @Version 1.0
 */
@Slf4j
@Configuration
public class TdmqConfiguration {

    @Bean
    public TdmqProducerFactory producerFactory() throws PulsarClientException {
        log.info("TdmqConfiguration...");
        return new TdmqProducerFactory()
                .register(Topics.ISSUING_OF_CERTIFICATES, Serialization.STRING)
                ;
    }

}
