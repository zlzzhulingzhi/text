package cn.qbs.wa.teach.question.tdmq;

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
                .register(Topics.QUESTION_UPDATE, Serialization.STRING)
                .register(Topics.QUESTION_DELETE, Serialization.STRING)
                .register(Topics.PAPER_UPDATE, Serialization.STRING)
                .register(Topics.PAPER_DELETE, Serialization.STRING)
//                .register(Topics.DISABLE_CATEGORY, Serialization.STRING)
//                .register(Topics.ENABLE_CATEGORY, Serialization.STRING)
//                .register(Topics.CATEGORY_NAME_CHANGE, Serialization.STRING)
                ;
    }

}
