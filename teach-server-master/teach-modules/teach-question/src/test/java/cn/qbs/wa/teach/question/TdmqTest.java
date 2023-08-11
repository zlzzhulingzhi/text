package cn.qbs.wa.teach.question;

import cn.qbs.wa.teach.question.entity.Paper;
import cn.qbs.wa.teach.question.entity.Question;
import cn.qbs.wa.teach.question.service.MqProducerService;
import cn.qbs.wa.teach.question.service.PaperService;
import cn.qbs.wa.teach.question.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Author zcm
 * @Date 2021/12/9 09:11
 * @Version 1.0
 */
@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TdmqTest {

    @Autowired
    private MqProducerService mqProducerService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private PaperService paperService;


    @Test
    public void sendTest() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            mqProducerService.sendQuestionUpdateMsg(Long.valueOf(i + 1), 0L);
        }

        Thread.sleep(5000);
    }

    @Test
    public void sendFromAllQuestion() {
        List<Question> questionList = questionService.lambdaQuery().list();
        for (Question question : questionList) {
            mqProducerService.sendQuestionUpdateMsg(question.getId(), question.getOrgId());
        }
    }

    @Test
    public void sendFromAllPaper() {
        List<Paper> paperList = paperService.lambdaQuery().list();
        for (Paper paper : paperList) {
            mqProducerService.sendPaperUpdateMsg(paper.getId(), paper.getOrgId());
        }
    }

}
