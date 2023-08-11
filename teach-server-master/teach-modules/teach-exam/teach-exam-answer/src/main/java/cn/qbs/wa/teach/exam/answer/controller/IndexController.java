package cn.qbs.wa.teach.exam.answer.controller;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import cn.qbs.wa.teach.exam.answer.pojo.ExamResult;
import cn.qbs.wa.teach.exam.answer.pojo.ExamVO;
import cn.qbs.wa.teach.exam.answer.pojo.ExamWithQuestions;
import cn.qbs.wa.teach.exam.answer.pojo.SubmitAnswerRequest;
import cn.qbs.wa.teach.exam.answer.pojo.exam.ExamPageRequest;
import cn.qbs.wa.teach.exam.answer.pojo.exam.ExamPageResponse;
import cn.qbs.wa.teach.exam.answer.pojo.exam.ExamRecord;
import cn.qbs.wa.teach.exam.answer.pojo.exam.SaveAttachment;
import cn.qbs.wa.teach.exam.answer.service.ExamService;
import cn.qbs.wa.teach.exam.answer.service.ExamineeRecordService;
import cn.qbs.wa.teach.exam.answer.service.ExamineeService;
import cn.qbs.wa.teach.exam.common.entity.ExamineeRecord;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @Author zcm
 * @Date 2021-12-16 18:14
 * @Version 1.0
 */
@RestController
@RequiredArgsConstructor
public class IndexController {

    private final ExamService examService;

    private final ExamineeRecordService examineeRecordService;

    private final ExamineeService examineeService;

    @PostMapping("/exam")
    @Log(title = "考试答题-查询考试", businessType = BusinessType.QUERY)
    public R<ExamVO> exam(@RequestBody @Validated IdRequest request) {
        return R.ok(examService.getExam(request.getId()));
    }

    @PostMapping("/examWithQuestions")
    @Log(title = "考试答题-查询考试及考题", businessType = BusinessType.QUERY)
    public R<ExamWithQuestions> examWithQuestions(@RequestBody @Validated IdRequest request) {
        return examService.getExamWithQuestions(request.getId());
    }

    @PostMapping("/startAnswer")
    @Log(title = "考试答题-记录开始考试时间", businessType = BusinessType.UPDATE)
    public R startAnswer(@RequestBody @Validated IdRequest request) {
        examService.updateStartAnswerTime(request.getId());
        return R.ok();
    }

    @PostMapping("/submitAnswer")
    @Log(title = "考试答题-考试提交答案", businessType = BusinessType.QUERY)
    public R saveAnswer(@RequestBody @Validated List<SubmitAnswerRequest> submitAnswerList) {
        examService.submitAnswer(submitAnswerList);
        return R.ok();
    }

    @PostMapping("/submitPaper")
    @Log(title = "考试答题-考试交卷", businessType = BusinessType.UPDATE)
    public R submitPaper(@RequestBody @Validated IdRequest request) {
        examService.submitPaper(request.getId());
        return R.ok();
    }

    /**
     * 再次考试
     * @param request
     * @return
     */
    @PostMapping("/againExam")
    @Log(title = "考试答题-再次考试", businessType = BusinessType.INSERT)
    public R againExam(@RequestBody @Validated IdRequest request) {
        examService.againExam(request.getId());
        return R.ok();
    }

    /**
     * 考试结果
     * @param request
     * @return
     */
    @PostMapping("/examResult")
    @Log(title = "考试答题-查询考试结果", businessType = BusinessType.QUERY)
    public R<ExamResult> examResult(@RequestBody @Validated IdRequest request) {
        return R.ok(examineeRecordService.examResult(request.getId()));
    }

    /**
     * 我的考试列表
     * @param
     * @return
     */
    @ApiOperation("我的考试列表")
    @PostMapping("/myTestPage")
    public R<IPage<ExamPageResponse>> myTestPage(@RequestBody @Validated ExamPageRequest examPageRequest) {
        return R.ok(examineeService.myTestPage(examPageRequest));
    }

    /**
     * 查看考试记录
     */
    @ApiOperation("查看考试记录")
    @PostMapping("/view")
    public R<ExamRecord> viewLog(@RequestBody @Validated ExamPageRequest examPageRequest) {
        return R.ok(examineeService.viewLog(examPageRequest));
    }

    /**
     * 扫描是否作弊
     */
    @ApiOperation("扫描是否作弊")
    @PostMapping("/cheat")
    public R<ExamineeRecord> cheat(@RequestBody @Validated ExamPageRequest examPageRequest) {
        return R.ok(examineeService.cheat(examPageRequest));
    }

    /**
     * 保存附件
     */
    @ApiOperation("保存附件")
    @PostMapping("/saveAttachment")
    @Log(title = "考试答题-保存附件", businessType = BusinessType.QUERY)
    public R<Boolean> saveAttachment(@RequestBody @Validated SaveAttachment saveAttachment) {
        return R.ok(examineeRecordService.saveAttachment(saveAttachment));
    }

}
