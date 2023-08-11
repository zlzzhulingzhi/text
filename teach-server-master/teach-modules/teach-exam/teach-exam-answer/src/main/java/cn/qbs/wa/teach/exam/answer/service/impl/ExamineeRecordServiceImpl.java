package cn.qbs.wa.teach.exam.answer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.exam.answer.mapper.ExamMapper;
import cn.qbs.wa.teach.exam.answer.mapper.ExamineeRecordMapper;
import cn.qbs.wa.teach.exam.answer.pojo.ExamResult;
import cn.qbs.wa.teach.exam.answer.pojo.exam.SaveAttachment;
import cn.qbs.wa.teach.exam.answer.service.*;
import cn.qbs.wa.teach.exam.api.RemoteExamCertService;
import cn.qbs.wa.teach.exam.api.pojo.DTO.ExamRecordDTO;
import cn.qbs.wa.teach.exam.api.pojo.DTO.PersonCertDetailResponseDTO;
import cn.qbs.wa.teach.exam.common.entity.Exam;
import cn.qbs.wa.teach.exam.common.entity.ExamineeRecord;
import cn.qbs.wa.teach.exam.common.entity.ExamineeRecordQuestion;
import cn.qbs.wa.teach.exam.common.entity.Rule;
import cn.qbs.wa.teach.exam.common.enumerate.ExamStatusEnum;
import cn.qbs.wa.teach.exam.common.enumerate.ExamineeRecordStatusEnum;
import cn.qbs.wa.teach.exam.common.enumerate.RuleEnum;
import cn.qbs.wa.teach.question.api.pojo.DTO.paper.PaperDetailDTO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qbs.tdmq.producer.TdmqProducerTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 考生考试记录表(ExamineeRecord)表服务实现类
 *
 * @author zcm
 * @since 2021-12-14 11:43:32
 */
@Slf4j
@Service("examineeRecordService")
@RequiredArgsConstructor
public class ExamineeRecordServiceImpl extends ServiceImpl<ExamineeRecordMapper, ExamineeRecord> implements ExamineeRecordService {

    private final ExamMapper examMapper;

    private final PaperService paperService;

    private final ExamineeRecordQuestionService examineeRecordQuestionService;

    private final RuleService ruleService;

    private final ExamRuleService examRuleService;

    private final RemoteExamCertService remoteExamCertService;

    private final TdmqProducerTemplate tdmqProducerTemplate;


    @Override
    public ExamineeRecord getLastExamineeRecord(Long examineeId) {
        if (examineeId == null) {
            return null;
        }
        return this.lambdaQuery().eq(ExamineeRecord::getExamineeId, examineeId)
                .orderByDesc(ExamineeRecord::getId)
                .last("limit 1").one();
    }

    @Override
    public ExamResult examResult(Long examineeRecordId) {
        ExamineeRecord examineeRecord = this.getById(examineeRecordId);
        if (examineeRecord == null) {
            throw new ServiceException("查不到考试记录！");
        }
        if (ExamineeRecordStatusEnum.UNFINISH.getStatus() == examineeRecord.getStatus()) {
            throw new ServiceException("未交卷，不能查看考试结果！");
        }
        Exam exam = examMapper.selectExamByExamineeRecordId(examineeRecordId);
        if (exam == null) {
            throw new ServiceException("查不到考试！");
        }

        ExamResult examResult = baseMapper.selectExamResult(examineeRecordId);
        ExamRecordDTO examRecordDTO = new ExamRecordDTO();
        examRecordDTO.setId(exam.getId());
        examRecordDTO.setUserId(SecurityContextHolder.getUserId());
        //查询考试记录证书
        R<List<PersonCertDetailResponseDTO>> certInfo = remoteExamCertService.getCertInfo(examRecordDTO);
        if(StringUtils.isNotEmpty(certInfo.getData())){
            examResult.setShowCert(true);
            examResult.setCertNameList(certInfo.getData().stream().map(PersonCertDetailResponseDTO::getName).collect(Collectors.toList()));
        }
        examResult.setExamineeRecordId(examineeRecordId);
        BeanUtil.copyProperties(exam, examResult);
        examResult.setTotalScore(exam.getPaperScore());
        examResult.setExamStartTime(exam.getStartTime());
        examResult.setExamEndTime(exam.getEndTime());

        // 是否允许查看答题情况
        boolean allowViewingPaperAnswers = false;
        if (ExamStatusEnum.EXAM_ENDED.getStatus() == exam.getStatus() || LocalDateTime.now().isAfter(exam.getEndTime())) {
            // 默认考试结束就能查看
            allowViewingPaperAnswers = true;
        } else {
            // 交卷后允许查看试卷作答情况的规则
            Rule allowViewingAfterSubmitPaperRule = this.ruleService.selectOneByExamIdAndCode(exam.getId(), RuleEnum.ALLOW_VIEWING_AFTER_SUBMIT_PAPER.getCode());
            if (allowViewingAfterSubmitPaperRule != null) {
                allowViewingPaperAnswers = true;
            }
        }

        examResult.setAllowViewingPaperAnswers(allowViewingPaperAnswers);
        if (allowViewingPaperAnswers) {
            // 是否显示答案解析
            Rule showAnswerDescRule = this.ruleService.selectOneByExamIdAndCode(exam.getId(), RuleEnum.SHOW_ANSWER_DESC.getCode());
            examResult.setShowAnswerDesc(showAnswerDescRule != null);

            PaperDetailDTO paperDetail = paperService.getPaperDetail(exam.getPaperId());
            if (paperDetail == null) {
                throw new ServiceException("查不到考试试卷！");
            }

            // 查询试题
            List<ExamineeRecordQuestion> examineeRecordQuestionList = examineeRecordQuestionService.lambdaQuery()
                    .eq(ExamineeRecordQuestion::getExamineeRecordId, examineeRecordId)
                    .list();

            List<ExamResult.QuestionType> questionTypeList = paperDetail.getQuestionTypeList().stream().map(item -> {
                ExamResult.QuestionType qt = new ExamResult.QuestionType();
                BeanUtil.copyProperties(item, qt, true);
                return qt;
            }).collect(Collectors.toList());
            examResult.setQuestionTypeList(questionTypeList);

            examResult.getQuestionTypeList().stream().forEach(qt -> {
                List<ExamResult.Question> questionList = qt.getQuestionList();
                Iterator<ExamResult.Question> iterator = questionList.iterator();
                while (iterator.hasNext()) {
                    ExamResult.Question question = iterator.next();
                    // 未设置 “显示答案解析”，则将答案和答案解析置空
                    if (!examResult.getShowAnswerDesc()) {
                        question.setAnswer(null);
                        question.setAnswerDesc(null);
                    }

                    ExamineeRecordQuestion examineeRecordQuestion = examineeRecordQuestionList.stream()
                            .filter(item -> item.getQuestionId().equals(question.getId()))
                            .findFirst().orElse(null);
                    if (examineeRecordQuestion != null) {
                        question.setExamineeQuestionId(examineeRecordQuestion.getId());
                        question.setQuestionScore(examineeRecordQuestion.getQuestionScore());
                        question.setExamineeAnswer(examineeRecordQuestion.getAnswer());
                        question.setCorrectResult(examineeRecordQuestion.getCorrectResult());
                        question.setSortNum(examineeRecordQuestion.getSortNum());
                    } else {
                        iterator.remove();
                    }
                }

                questionList = questionList.stream().sorted(Comparator.comparing(ExamResult.Question::getSortNum)).collect(Collectors.toList());
                qt.setQuestionList(questionList);
            });
        }

        return examResult;
    }

    @Override
    public boolean saveAttachment(SaveAttachment saveAttachment) {
        Long examineeRecordId = saveAttachment.getExamineeRecordId();
        Exam exam = this.examMapper.selectExamByExamineeRecordId(examineeRecordId);
        if (exam == null) {
            throw new IllegalParamsException("查不到考试");
        }

        Rule supportUploadAttachmentRule = this.ruleService.selectOneByExamIdAndCode(exam.getId(), RuleEnum.SUPPORT_UPLOAD_ATTACHMENT.getCode());
        if (supportUploadAttachmentRule == null) {
            throw new ServiceException("考试不支持上传附件");
        }

        checkIsAnsweringStatus(examineeRecordId);
        boolean success = this.lambdaUpdate().eq(ExamineeRecord::getId, examineeRecordId)
                .set(ExamineeRecord::getAttachmentName, saveAttachment.getAttachmentName())
                .set(ExamineeRecord::getAttachmentUrl, saveAttachment.getAttachmentUrl())
                .update();
        return success;
    }

    /**
     * 检查是否答题中状态
     * @param examineeRecordId
     * @return
     */
    private ExamineeRecord checkIsAnsweringStatus(Long examineeRecordId) {
        ExamineeRecord examineeRecord = this.getById(examineeRecordId);
        if (examineeRecord == null) {
            throw new IllegalParamsException("查不到考试记录");
        }
        if (examineeRecord.getStatus() > ExamineeRecordStatusEnum.UNFINISH.getStatus()) {
            throw new IllegalParamsException("交卷后不能再上传附件");
        }

        return examineeRecord;
    }

}

