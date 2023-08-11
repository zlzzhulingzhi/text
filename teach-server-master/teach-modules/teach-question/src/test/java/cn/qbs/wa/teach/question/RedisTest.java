package cn.qbs.wa.teach.question;

import cn.qbs.wa.teach.common.redis.service.RedisService;
import cn.qbs.wa.teach.common.redisson.operation.RedissonObject;
import cn.qbs.wa.teach.question.entity.QuestionBody;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2021/11/18 14:20
 */

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RedisTest {

    @Autowired
    RedisService redisService;

    @Autowired
    RedissonObject redissonObject;





    @Test
    public void test(){
        QuestionBody questionBody = new QuestionBody();
        questionBody.setAnswer("的记得你");
        questionBody.setAnswerDesc("dddd");
        redissonObject.setValue("ccc",questionBody);
    }
}
