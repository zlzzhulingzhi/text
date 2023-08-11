package cn.qbs.wa.teach.exam.admin.service.impl;

import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.exam.admin.pojo.invigilation.TerminateExamRequest;
import cn.qbs.wa.teach.exam.admin.service.*;
import cn.qbs.wa.teach.exam.common.entity.Examinee;
import cn.qbs.wa.teach.exam.common.entity.ExamineeRecord;
import cn.qbs.wa.teach.exam.common.entity.Rule;
import cn.qbs.wa.teach.exam.common.entity.ViolationRecord;
import cn.qbs.wa.teach.exam.common.enumerate.ExamineeRecordStatusEnum;
import cn.qbs.wa.teach.exam.common.enumerate.RuleEnum;
import cn.qbs.wa.teach.common.core.utils.DateFormatUtils;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 监考 Service 实现
 * @Author zcm
 * @Date 2022/1/25 10:10
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InvigilationServiceImpl implements InvigilationService {

    private final RuleService ruleService;

    private final ExamineeService examineeService;

    private final ExamineeRecordService examineeRecordService;

    private final ViolationRecordService violationRecordService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean terminateExam(TerminateExamRequest params) {
        Long examineeRecordId = params.getExamineeRecordId();
        ExamineeRecord examineeRecord = examineeRecordService.getById(examineeRecordId);
        if (examineeRecord == null) {
            throw new ServiceException("查不到考试记录！");
        }
        if (ExamineeRecordStatusEnum.INTERRUPT.getStatus() == examineeRecord.getStatus()) {
            throw new ServiceException("考试已终止，请勿重复操作！");
        }

        Boolean calculated = examineeRecord.getCalculated();
        LambdaUpdateChainWrapper<ExamineeRecord> lambdaUpdateChainWrapper = examineeRecordService.lambdaUpdate().eq(ExamineeRecord::getId, examineeRecordId)
                .set(ExamineeRecord::getStatus, ExamineeRecordStatusEnum.INTERRUPT.getStatus())
                .set(ExamineeRecord::getRemark, params.getReason())
                .set(ExamineeRecord::getScore, BigDecimal.ZERO)
                .set(ExamineeRecord::getCalculated, false);

        LocalDateTime submitTime = examineeRecord.getSubmitTime();
        if (submitTime == null) {
            submitTime = LocalDateTime.now();
            lambdaUpdateChainWrapper.set(ExamineeRecord::getSubmitTime, submitTime);

            LocalDateTime startTime = examineeRecord.getStartTime();
            if (startTime == null) {
                startTime = examineeRecord.getCreateTime();
                lambdaUpdateChainWrapper.set(ExamineeRecord::getStartTime, startTime);
            }

            Long diffSeconds = ChronoUnit.SECONDS.between(startTime, submitTime);
            lambdaUpdateChainWrapper.set(ExamineeRecord::getUseDuration, diffSeconds.intValue());
            String secondsFormat = DateFormatUtils.secondsFormat(diffSeconds);
            lambdaUpdateChainWrapper.set(ExamineeRecord::getUseDurationFormat, secondsFormat);
        }

        boolean success = lambdaUpdateChainWrapper.update();
        if (!success) {
            return false;
        }

        // 修改考试状态和备注,不作为有效成绩, 后期线下修改
        Long examineeId = examineeRecord.getExamineeId();
        if (calculated) {
            examineeRecordService.updateCalculated(examineeId);
        }

        // 插入信息到违规考试表,监考的违规信息写死为摄像头录像
        ViolationRecord violationRecord = new ViolationRecord();
        Rule rule = ruleService.selectOneByCode(RuleEnum.CAMERA_RECORDING.getCode());
        violationRecord.setRuleId(rule.getId());
        violationRecord.setExamineeId(examineeId);
        violationRecord.setExamineeRecordId(examineeRecordId);

        Examinee examinee = examineeService.getById(examineeId);
        violationRecord.setEmployeeId(examinee.getEmployeeId());
        success = violationRecordService.save(violationRecord);
        return success;
    }

}
