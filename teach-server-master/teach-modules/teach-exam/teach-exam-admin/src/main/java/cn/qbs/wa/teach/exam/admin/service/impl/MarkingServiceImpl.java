package cn.qbs.wa.teach.exam.admin.service.impl;

import cn.qbs.wa.teach.basic.api.pojo.DTO.user.UserListResultDTO;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.exam.admin.mapper.ExamMapper;
import cn.qbs.wa.teach.exam.admin.pojo.exam.*;
import cn.qbs.wa.teach.exam.admin.service.ExamineeRecordQuestionService;
import cn.qbs.wa.teach.exam.admin.service.MarkingService;
import cn.qbs.wa.teach.exam.admin.service.UserService;
import cn.qbs.wa.teach.exam.common.constant.Topics;
import cn.qbs.wa.teach.exam.common.entity.ExamineeRecordQuestion;
import cn.qbs.wa.teach.exam.common.enumerate.CorrectStatusEnum;
import cn.qbs.wa.teach.organization.api.RemoteStudentService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentSearchDTO;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qbs.tdmq.producer.TdmqProducerTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author zcm
 * @Date 2022/1/15 11:28
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MarkingServiceImpl implements MarkingService {

    private final ExamMapper examMapper;

    private final UserService userService;

    private final ExamineeRecordQuestionService examineeRecordQuestionService;

    private final TdmqProducerTemplate tdmqProducerTemplate;

    private final RemoteStudentService remoteStudentService;




    /**
     * 阅卷分页查询
     * @param params
     * @return
     */
    @Override
    public IPage<MarkingPageResponse> markingPage(MarkingPageRequest params) {
        IPage<MarkingPageResponse> page = examMapper.markingPage(params.createMpPage(), params);
        page.getRecords().forEach(i -> i.setCorrectStatus(i.getUnCorrectCount() > 0 ? 1 : 2));
        return page;
    }

    /**
     * 阅卷-考试记录分页查询
     * @param params
     * @return
     */
    @Override
    public IPage<MarkingExamRecordPageResponse> examRecordPage(MarkingExamRecordPageRequest params) {
        List<Long> studentIdList = null;
        if (StringUtils.isNotBlank(params.getExamineeName())) {
            StudentSearchDTO searchDTO = new StudentSearchDTO();
            searchDTO.setRealName(params.getExamineeName());
            searchDTO.setOrgId(SecurityContextHolder.getOrgId());
            R<List<StudentDTO>> r = remoteStudentService.list(searchDTO);
            if (r != null && r.isOk()) {
                List<StudentDTO> studentDTOList = r.getData();
                if (CollectionUtils.isNotEmpty(studentDTOList)) {
                    studentIdList = studentDTOList.stream().map(StudentDTO::getId).collect(Collectors.toList());
                }
            }

            if (CollectionUtils.isEmpty(studentIdList)) {
                return new Page(params.getCurrent(), params.getSize(), 0L);
            }
        }

        IPage<MarkingExamRecordPageResponse> page = examMapper.examRecordPage(params.createMpPage(), params, studentIdList);
        List<MarkingExamRecordPageResponse> examRecordList = page.getRecords();
        if (CollectionUtils.isNotEmpty(examRecordList)) {
            List<Long> userIdList = examRecordList.stream().map(MarkingExamRecordPageResponse::getUserId).collect(Collectors.toList());
            StudentSearchDTO searchDTO = new StudentSearchDTO();
            searchDTO.setUserIds(userIdList);
            R<List<StudentDTO>> r = remoteStudentService.list(searchDTO);
            if (r != null && r.isOk()) {
                List<StudentDTO> data = r.getData();
                if (CollectionUtils.isNotEmpty(data)) {
                    data.forEach(u -> {
                        List<MarkingExamRecordPageResponse> list = examRecordList.stream().filter(i -> i.getUserId().equals(u.getUserId())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(list)) {
                            list.forEach(i -> i.setExamineeName(u.getRealName()));
                        }
                    });
                }
            }
        }
        return page;
    }

    @Override
    public boolean correct(CorrectRequest params) {
        Long examineeRecordQuestionId = params.getExamineeRecordQuestionId();
        ExamineeRecordQuestion examineeRecordQuestion = examineeRecordQuestionService.getById(examineeRecordQuestionId);
        if (examineeRecordQuestion == null) {
            throw new ServiceException("考题不存在或已删除！");
        }

        BigDecimal score = params.getScore();
        BigDecimal questionScore = examineeRecordQuestion.getQuestionScore();
        if (score.compareTo(questionScore) == 1){
            throw new ServiceException("批改分数不能超过试题分数！");
        }

        boolean success = examineeRecordQuestionService.lambdaUpdate().eq(ExamineeRecordQuestion::getId, examineeRecordQuestionId)
                .set(ExamineeRecordQuestion::getScore, score)
                .set(ExamineeRecordQuestion::getCorrectStatus, CorrectStatusEnum.CORRECTED.getStatus())
                .set(ExamineeRecordQuestion::getCorrectTime, LocalDateTime.now())
                .set(ExamineeRecordQuestion::getCorrectResult, score.compareTo(questionScore) == 0 ? 1 : 0)
                .set(ExamineeRecordQuestion::getCorrectBy, SecurityContextHolder.getUserId())
                .update();

        if (success) {
            Map<String, Object> data = new HashMap<>(2);
            data.put("examineeRecordQuestionId", examineeRecordQuestionId);
            data.put("orgId", SecurityContextHolder.getOrgId());
            tdmqProducerTemplate.sendAsyncStringMsg(Topics.MANUAL_CORRECT,  JSON.toJSONString(data));
        }
        return success;
    }

}
