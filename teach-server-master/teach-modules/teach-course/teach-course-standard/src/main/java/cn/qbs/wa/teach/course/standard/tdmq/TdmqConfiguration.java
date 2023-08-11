//package cn.qbs.wa.teach.course.standard.tdmq;
//
//import cn.qbs.wa.teach.common.core.constant.TopicsConstants;
//import com.qbs.tdmq.constant.Serialization;
//import com.qbs.tdmq.producer.TdmqProducerFactory;
//import org.apache.pulsar.client.api.PulsarClientException;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import cn.qbs.wa.teach.task.api.tdmq.topic.Topics;
//
//@Configuration
//public class TdmqConfiguration {
//
//    @Bean
//    public TdmqProducerFactory producerFactory() throws PulsarClientException {
//        return new TdmqProducerFactory()
//                .register(Topics.LEARNED_PROGRESS_UPDATE, Serialization.STRING)
//                .register(TopicsConstants.FILE_QUOTED, Serialization.STRING)
//                .register(TopicsConstants.FILE_QUOTED_DELETED, Serialization.STRING)
//                ;
//    }
//
//}
