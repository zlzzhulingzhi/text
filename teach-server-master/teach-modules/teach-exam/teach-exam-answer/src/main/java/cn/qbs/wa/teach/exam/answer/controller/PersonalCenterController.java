package cn.qbs.wa.teach.exam.answer.controller;

import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.exam.ExamServiceException;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import cn.qbs.wa.teach.exam.answer.pojo.ExamVO;
import cn.qbs.wa.teach.exam.answer.pojo.ExamWithQuestions;
import cn.qbs.wa.teach.exam.answer.pojo.center.*;
import cn.qbs.wa.teach.exam.answer.pojo.exam.ExamAnswerSubmitRequest;
import cn.qbs.wa.teach.exam.answer.service.ExamService;
import cn.qbs.wa.teach.exam.answer.service.ExamineeRecordService;
import cn.qbs.wa.teach.exam.answer.service.ExamineeService;
import cn.qbs.wa.teach.exam.answer.service.PersonalExamService;
import cn.qbs.wa.teach.exam.common.entity.Examinee;
import cn.qbs.wa.teach.exam.common.entity.ExamineeRecord;
import cn.qbs.wa.teach.exam.common.util.RedisLockKeyUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author yjx
 */
@Slf4j
@Api(tags = {"考试中心", "我的考试"})
@RestController
@RequestMapping("/personal")
public class PersonalCenterController {

    @Resource
    private ExamService examService;

    @Resource
    private ExamineeService examineeService;

    @Resource
    private ExamineeRecordService examineeRecordService;

    @Resource
    private PersonalExamService personalExamService;

    @Resource
    private RedissonClient redissonClient;

    /**
     * 分页查询考试
     */
    @ApiOperation(value = "分页")
    @PostMapping("/center/page")
    @Log(title = "考试答题-个人考试中心分页查询", businessType = BusinessType.QUERY)
    public R<IPage<PageResponse>> page(@RequestBody PageRequest params) {
        params.setUserId(SecurityContextHolder.getUserId());
        params.setOrgId(SecurityContextHolder.getOrgId());
        return R.ok(examService.centerPage(params));
    }

    /**
     * 分页查询考试记录
     */
    @ApiOperation(value = "分页")
    @PostMapping("/recordPage")
    @Log(title = "考试答题-考试记录分页查询", businessType = BusinessType.QUERY)
    public R<IPage<PageResponse>> recordPage(@RequestBody @Validated PageRequest params) {
        params.setUserId(SecurityContextHolder.getUserId());
        params.setOrgId(SecurityContextHolder.getOrgId());
        return R.ok(examService.recordPage(params));
    }

    /**
     * 查询详情
     */
    @ApiOperation(value = "详情")
    @PostMapping("/record")
    @Log(title = "考试答题-考试记录", businessType = BusinessType.QUERY)
    public R<ExamineInfoResponse> record(@RequestBody @Validated ExamineRecordRequest params) {
        Examinee examinee = examineeService.getById(params.getExamineeId());
        if (examinee == null) {
            return R.ok(null);
        }
        ExamineInfoResponse response = new ExamineInfoResponse();
        response.setRemainingTimes(examinee.getRemainingTimes());
        List<ExamineeRecord> examineRecordList = examineeRecordService.lambdaQuery().eq(ExamineeRecord::getExamineeId, params.getExamineeId()).list();
        response.setRecords(examineRecordList);
        response.setExamTimes(examineRecordList.size());
        if (!examineRecordList.isEmpty()) {
            examineRecordList.stream().filter(e -> Boolean.TRUE.equals(e.getCalculated())).findFirst().ifPresent(e -> response.setHighestScore(e.getScore()));
        }
        return R.ok(response);
    }

    @PostMapping("/exam")
    @Log(title = "考试答题-查询考试基本信息", businessType = BusinessType.QUERY)
    public R<ExamVO> exam(@RequestBody @Validated IdRequest request) {
        return R.ok(personalExamService.getExam(request.getId()));
    }

    @PostMapping("/examPreview")
    @Log(title = "考试答题-考试预览", businessType = BusinessType.QUERY)
    public R<ExamVO> examPreview(@RequestBody @Validated IdRequest request) {
        return R.ok(personalExamService.examPreview(request.getId()));
    }


    @PostMapping("/againExam")
    @Log(title = "考试答题-再次考试", businessType = BusinessType.INSERT)
    public R<Long> againExam(@RequestBody @Validated ExamCommonRequest request) {
        ExamVO exam = examService.getExam(request.getId());
        if (exam.getStartTime().isAfter(LocalDateTime.now())) {
            throw new ExamServiceException("考试未开始！");
        }
        if (request.getExamineeRecordId() == null) {
            return R.fail("考试记录ID不能为空");
        }
        Long examRecordId = null;
        Long userId = SecurityContextHolder.getUserId();
        RLock lock = redissonClient.getLock(RedisLockKeyUtil.getAddExamQuestionsLockKey(request.getId(), userId));
        try {
            if (lock.tryLock(5L, 10L, TimeUnit.SECONDS)) {
                examRecordId = personalExamService.reCreateExamRecord(request.getExamineeId(), request.getExamineeRecordId());
                // 剩余考试次数减一
                examineeService.decrRemainingTimes(request.getExamineeId());
                return R.ok(examRecordId);
            }
        } catch (InterruptedException e) {
            log.error("获取锁中断，", e);
        } finally {
            if (lock != null && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
        return R.ok(examRecordId);
    }

    @PostMapping("/examWithQuestions")
    @Log(title = "考试答题-查询考试和考题", businessType = BusinessType.QUERY)
    public R<ExamWithQuestions> examWithQuestions(@RequestBody @Validated ExamCommonRequest request) {
        ExamVO exam = examService.getExam(request.getId());
        if (exam.getStartTime().isAfter(LocalDateTime.now())) {
            throw new ExamServiceException("考试未开始！");
        }
        return personalExamService.getExamWithQuestions(request);
    }

    @PostMapping("/submitAnswer")
    @Log(title = "考试答题-交卷", businessType = BusinessType.UPDATE)
    public R saveAnswer(@RequestBody @Validated ExamAnswerSubmitRequest request) {
        personalExamService.submitAnswer(request);
        return R.ok();
    }

}
