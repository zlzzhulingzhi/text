package cn.qbs.wa.teach.exam.answer.tdmq;

import cn.qbs.wa.teach.exam.common.constant.Topics;
import com.qbs.tdmq.constant.Serialization;
import com.qbs.tdmq.producer.TdmqProducerFactory;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author zcm
 * @Date 2021-12-07 19:01
 * @Version 1.0
 */
@Configuration
public class TdmqConfiguration {

    @Bean
    public TdmqProducerFactory producerFactory() throws PulsarClientException {
        return new TdmqProducerFactory()
                .register(Topics.ADD_EXAM_QUESTIONS, Serialization.STRING)
                .register(Topics.SUBMIT_PAPER, Serialization.STRING)
                .register(Topics.ISSUING_OF_CERTIFICATES, Serialization.STRING)
                ;
    }

}
