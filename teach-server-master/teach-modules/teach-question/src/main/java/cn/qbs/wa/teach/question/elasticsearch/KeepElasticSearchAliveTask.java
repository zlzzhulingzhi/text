package cn.qbs.wa.teach.question.elasticsearch;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author zcm
 * @Date 2021/11/12 10:09
 * @Version 1.0
 */
@Slf4j
@Component
public class KeepElasticSearchAliveTask {

    @Resource
    private RestHighLevelClient esRestClient;

    @Scheduled(fixedRate = 100000, initialDelay = 100000)
    public void keepConnectionAlive() {
        try {
            boolean pingSuccess = esRestClient.ping(RequestOptions.DEFAULT);
            if (!pingSuccess) {
                log.error("ping elasticsearch failed!");
            }
        } catch (Exception e) {
            log.error("ping elasticsearch exception: ", e);
        }
    }

}
