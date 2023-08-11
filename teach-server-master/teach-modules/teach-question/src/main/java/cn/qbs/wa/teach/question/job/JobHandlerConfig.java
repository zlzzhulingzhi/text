package cn.qbs.wa.teach.question.job;

import cn.qbs.wa.teach.question.entity.Paper;
import cn.qbs.wa.teach.question.entity.Question;
import cn.qbs.wa.teach.question.mapper.PaperMapper;
import cn.qbs.wa.teach.question.mapper.QuestionMapper;
import cn.qbs.wa.teach.question.service.PaperService;
import cn.qbs.wa.teach.question.service.QuestionService;
import com.alibaba.fastjson.JSON;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @Author zcm
 * @Date 2022-01-17 16:36
 * @Version 1.0
 */
@Configuration
@RequiredArgsConstructor
public class JobHandlerConfig {

    private final QuestionMapper questionMapper;

    private final QuestionService questionService;

    private final PaperMapper paperMapper;

    private final PaperService paperService;


    /**
     * 重建ES试题索引
     */
    @XxlJob(JobNames.REINDEX_ES_QUESTION)
    public void reindexEsQuestion() throws InterruptedException {
        String params = XxlJobHelper.getJobParam();
        XxlJobHelper.log("重建ES试题索引任务执行，参数：{}", params);
        Instant beginTime = Instant.now();

        List<Long> questionIdList = null;
        Integer size = 99999;
        if (StringUtils.isNotBlank(params)) {
            ReindexEsQuestionParam reindexEsQuestionParam = JSON.parseObject(params, ReindexEsQuestionParam.class);
            if (reindexEsQuestionParam != null) {
                questionIdList = reindexEsQuestionParam.getQuestionIdList();
                if (reindexEsQuestionParam.getSize() != null && reindexEsQuestionParam.getSize() > 0) {
                    size = reindexEsQuestionParam.getSize();
                }
            }
        }
        List<Question> questionList = questionMapper.selectQuestion(questionIdList, size);
        XxlJobHelper.log("试题总数：{}", questionList.size());

        CountDownLatch countDownLatch = new CountDownLatch(questionList.size());
        int threads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        for (Question question : questionList) {
            executorService.submit(() -> {
                try {
                    questionService.saveEsIndex(question.getId(), question.getOrgId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown();

        XxlJobHelper.log("重建ES试题索引数量：{}，耗时{}秒", questionList.size(), Duration.between(beginTime, Instant.now()).getSeconds());
    }


    /**
     * 重建ES试卷索引
     */
    @XxlJob(JobNames.REINDEX_ES_PAPER)
    public void reindexEsPaper() throws InterruptedException {
        String params = XxlJobHelper.getJobParam();
        XxlJobHelper.log("重建ES试试卷引任务执行，参数：{}", params);
        Instant beginTime = Instant.now();

        List<Long> paperIdList = null;
        Integer size = 99999;
        if (StringUtils.isNotBlank(params)) {
            ReindexEsPaperParam reindexEsPaperParam = JSON.parseObject(params, ReindexEsPaperParam.class);
            if (reindexEsPaperParam != null) {
                paperIdList = reindexEsPaperParam.getPaperIdList();
                if (reindexEsPaperParam.getSize() != null && reindexEsPaperParam.getSize() > 0) {
                    size = reindexEsPaperParam.getSize();
                }
            }
        }
        List<Paper> paperList = paperMapper.selectPaper(paperIdList, size);
        XxlJobHelper.log("试卷总数：{}", paperList.size());

        CountDownLatch countDownLatch = new CountDownLatch(paperList.size());
        int threads = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        for (Paper paper : paperList) {
            executorService.submit(() -> {
                try {
                    paperService.saveEsIndex(paper.getId(), paper.getOrgId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown();

        XxlJobHelper.log("重建ES试卷索引数量：{}，耗时{}秒", paperList.size(), Duration.between(beginTime, Instant.now()).getSeconds());
    }



    @Data
    public static class ReindexEsQuestionParam {
        private List<Long> questionIdList = null;
        private Integer size = 99999;
    }


    @Data
    public static class ReindexEsPaperParam {
        private List<Long> paperIdList = null;
        private Integer size = 99999;
    }

}
