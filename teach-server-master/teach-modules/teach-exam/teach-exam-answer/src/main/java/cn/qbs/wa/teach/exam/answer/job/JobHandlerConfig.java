package cn.qbs.wa.teach.exam.answer.job;

import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.exam.answer.service.ExamService;
import cn.qbs.wa.teach.exam.answer.service.ExamineeRecordService;
import cn.qbs.wa.teach.exam.common.entity.ExamineeRecord;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;

/**
 * @Author zcm
 * @Date 2022/1/4 10:37
 * @Version 1.0
 */
@Configuration
@RequiredArgsConstructor
public class JobHandlerConfig {

    private final ExamService examService;

    private final ExamineeRecordService examineeRecordService;

    /**
     * 到期自动交卷任务
     */
    @XxlJob(JobNames.AUTO_SUBMIT_PAPER)
    public void autoSubmitPaperJobHandler() {
        String params = XxlJobHelper.getJobParam();
        XxlJobHelper.log("到期自动交卷任务执行，参数：{}", params);
        if (StringUtils.isEmpty(params)) {
            XxlJobHelper.log("参数为空，执行失败！");
            XxlJobHelper.handleFail();
            return;
        }

        JSONObject jsonObject = JSON.parseObject(params);
        Long examineeRecordId = jsonObject.getLong("examineeRecordId");
        Long orgId = jsonObject.getLong("orgId");
        SecurityContextHolder.setSelectOrgId(String.valueOf(orgId));

        ExamineeRecord examineeRecord = examineeRecordService.getById(examineeRecordId);
        if (examineeRecord == null) {
            XxlJobHelper.log("考试记录[{}]不存在，任务结束！", examineeRecordId);
            return;
        }

        if (examineeRecord.getSubmitTime() != null) {
            XxlJobHelper.log("考试记录[{}]已交卷，任务结束！", examineeRecordId);
            return;
        }

        XxlJobHelper.log("考试记录[{}]，执行交卷处理！", examineeRecordId);
        examService.submitPaper(examineeRecord);
    }

}
