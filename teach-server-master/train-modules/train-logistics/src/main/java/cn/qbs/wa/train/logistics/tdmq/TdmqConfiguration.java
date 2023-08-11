package cn.qbs.wa.train.logistics.tdmq;//package cn.qbs.wa.train.organization.tdmq;
//
//import cn.qbs.wa.train.task.api.tdmq.topic.Topics;
//import com.qbs.tdmq.constant.Serialization;
//import com.qbs.tdmq.producer.TdmqProducerFactory;
//import org.apache.pulsar.client.api.PulsarClientException;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @Author zcm
// * @Date 2022-03-22 21:35
// * @Version 1.0
// */
//@Configuration
//public class TdmqConfiguration {
//
//    @Bean
//    public TdmqProducerFactory producerFactory() throws PulsarClientException {
//        return new TdmqProducerFactory()
//                .register(Topics.TASKER_ADD_DEPT, Serialization.STRING).register(Topics.TASKER_ADD_GROUP, Serialization.STRING);
//    }
//
//}
