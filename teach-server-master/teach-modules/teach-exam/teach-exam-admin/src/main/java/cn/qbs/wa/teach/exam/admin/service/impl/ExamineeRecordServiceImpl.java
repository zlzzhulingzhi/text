package cn.qbs.wa.teach.exam.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.basic.api.pojo.DTO.user.UserListResultDTO;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdOrgRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.exam.admin.mapper.ExamMapper;
import cn.qbs.wa.teach.exam.admin.mapper.ExamineeMapper;
import cn.qbs.wa.teach.exam.admin.mapper.ExamineeRecordMapper;
import cn.qbs.wa.teach.exam.admin.pojo.exam.*;
import cn.qbs.wa.teach.exam.admin.service.*;
import cn.qbs.wa.teach.exam.common.entity.Exam;
import cn.qbs.wa.teach.exam.common.entity.Examinee;
import cn.qbs.wa.teach.exam.common.entity.ExamineeRecord;
import cn.qbs.wa.teach.exam.common.entity.ExamineeRecordQuestion;
import cn.qbs.wa.teach.exam.common.enumerate.ExamineeRecordStatusEnum;
import cn.qbs.wa.teach.organization.api.RemoteStudentService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentSearchDTO;
import cn.qbs.wa.teach.question.api.pojo.DTO.paper.PaperDetailDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 考生考试记录表(ExamineeRecord)表服务实现类
 *
 * @author zcm
 * @since 2021-12-14 11:43:32
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExamineeRecordServiceImpl extends ServiceImpl<ExamineeRecordMapper, ExamineeRecord> implements ExamineeRecordService {

    private final UserService userService;

    private final ExamMapper examMapper;

    private final PaperService paperService;

    private final ExamineeMapper examineeMapper;

    private final ExamineeRecordQuestionService examineeRecordQuestionService;

    private final ExamineeService examineeService;

    private final RemoteStudentService remoteStudentService;

    @Resource
    private ExamineeScoreService examineeScoreService;


    @Override
    public IPage<ExamRecord> selectListByExamId(ExamRecordPageRequest params) {
        IPage<ExamRecord> examRecordPage = baseMapper.selectListByExamId(params.createMpPage(), params);
        List<ExamRecord> examRecordList = examRecordPage.getRecords();
        if (CollectionUtils.isNotEmpty(examRecordList)) {
            List<Long> userIdList = examRecordList.stream().map(ExamRecord::getUserId).collect(Collectors.toList());
            StudentSearchDTO searchDTO = new StudentSearchDTO();
            searchDTO.setUserIds(userIdList);
            R<List<StudentDTO>> userList = remoteStudentService.list(searchDTO);
            List<StudentDTO> data = userList.getData();
            if (StringUtils.isNotEmpty(data)) {
                data.forEach(u -> {
                    List<ExamRecord> list = examRecordList.stream().filter(r -> r.getUserId().equals(u.getUserId())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(list)) {
                        list.forEach(i -> i.setExamineeName(u.getRealName()));
                    }
                });
            }
        }
        return examRecordPage;
    }

    @Override
    public IPage<ExamRanking> rankingByExamId(ExamRecordPageRequest pageParams) {
        IPage<ExamRanking> examRankingPage = baseMapper.rankingByExamId(pageParams.createMpPage(), pageParams);
        List<ExamRanking> examRankingList = examRankingPage.getRecords();
        if (CollectionUtils.isNotEmpty(examRankingList)) {
//            // 查询用户
//            List<Long> userIdList = examRankingList.stream().map(ExamRanking::getUserId).collect(Collectors.toList());
//            List<UserListResultDTO> userList = userService.getUserList(userIdList);
//            if (CollectionUtils.isNotEmpty(userList)) {
//                userList.forEach(u -> {
//                    List<ExamRanking> list = examRankingList.stream().filter(r -> r.getUserId().equals(u.getId())).collect(Collectors.toList());
//                    if (CollectionUtils.isNotEmpty(list)) {
//                        list.forEach(i -> {
//                            i.setExamineeName(u.getRealName());
//                            i.setMobile(u.getPhone());
//                        });
//                    }
//                });
//            }
            try {
                List<Long> userIdList = examRankingList.stream().map(ExamRanking::getUserId).collect(Collectors.toList());
                StudentSearchDTO searchDTO = new StudentSearchDTO();
                searchDTO.setUserIds(userIdList);
                R<List<StudentDTO>> userList = remoteStudentService.list(searchDTO);
                List<StudentDTO> data = userList.getData();
                if (StringUtils.isNotEmpty(data)) {
                    data.forEach(u -> {
                        List<ExamRanking> list = examRankingList.stream().filter(r -> r.getUserId().equals(u.getUserId())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(list)) {
                            list.forEach(i -> {
                                i.setExamineeName(u.getRealName());
                                i.setMobile(u.getPhone());
                            });
                        }
                    });
                }
            } catch (Exception e) {
                log.error("调用远程服务查询学生详情异常：", e);
            }

            // 查询部门
            /*EmployeeListSearchDTO params = new EmployeeListSearchDTO();
            params.setOrgId(examRankingList.get(0).getOrgId());
            List<Long> employeeIdList = examRankingList.stream().map(ExamRanking::getEmployeeId).collect(Collectors.toList());
            params.setIdList(employeeIdList);
            R<List<EmployeeListFullResultDTO>> r = remoteEmployeeService.listFull(params);
            List<EmployeeListFullResultDTO> employeeListFullList = r.getData();
            if (CollectionUtils.isNotEmpty(employeeListFullList)) {
                employeeListFullList.forEach(e -> {
                    ExamRanking examRanking = examRankingList.stream().filter(i -> i.getEmployeeId().equals(e.getEmployeeId())).findFirst().orElse(null);
                    if (examRanking != null) {
                        examRanking.setDeptName(e.getDeptNames());
                    }
                });
            }*/
        }
        return examRankingPage;
    }

    @Override
    public QuestionCorrectRateWrap questionCorrectRateByExamId(Long examId) {
        Exam exam = examMapper.selectById(examId);
        if (exam == null) {
            throw new IllegalParamsException("考试不存在或已删除！");
        }
        PaperDetailDTO paperDetail = paperService.getPaperDetail(exam.getPaperId());
        if (paperDetail == null) {
            throw new ServiceException("查不到考试试卷！");
        }

        QuestionCorrectRateWrap questionCorrectRateWrap = new QuestionCorrectRateWrap();
        List<QuestionCorrectRate> questionCorrectRateList = baseMapper.questionCorrectRateByExamId(examId);
        List<QuestionCorrectRateWrap.QuestionType> questionTypeList = paperDetail.getQuestionTypeList().stream().map(qt -> {
            QuestionCorrectRateWrap.QuestionType questionType = new QuestionCorrectRateWrap.QuestionType();
            questionType.setQuestionTypeId(qt.getQuestionTypeId());
            questionType.setQuestionTypeName(qt.getQuestionTypeName());
            questionType.setSortNum(qt.getSortNum());

            AtomicInteger localIndex = new AtomicInteger(1);
            List<QuestionCorrectRate> collect = qt.getQuestionList().stream().map(q -> {
                QuestionCorrectRate qcr = new QuestionCorrectRate();
                qcr.setQuestionId(q.getId());
                qcr.setLocalIndex(localIndex.getAndIncrement());

                QuestionCorrectRate questionCorrectRate = questionCorrectRateList.stream().filter(i -> i.getQuestionId().equals(q.getId())).findFirst().orElse(null);
                if (questionCorrectRate != null) {
                    qcr.setCorrectRate(questionCorrectRate.getCorrectRate());
                } else {
                    qcr.setCorrectRate(0);
                }
                return qcr;
            }).collect(Collectors.toList());
            questionType.setQuestionCorrectRateList(collect);
            return questionType;
        }).collect(Collectors.toList());
        questionCorrectRateWrap.setQuestionTypeList(questionTypeList);

        return questionCorrectRateWrap;
    }

    @Override
    public QuestionAnalysisWrap questionAnalysisByExamId(Long examId) {
        Exam exam = examMapper.selectById(examId);
        if (exam == null) {
            throw new IllegalParamsException("考试不存在或已删除！");
        }
        PaperDetailDTO paperDetail = paperService.getPaperDetail(exam.getPaperId());
        if (paperDetail == null) {
            throw new ServiceException("查不到考试试卷！");
        }

        QuestionAnalysisWrap questionAnalysisWrap = new QuestionAnalysisWrap();
        questionAnalysisWrap.setExamName(exam.getExamName());
        questionAnalysisWrap.setPaperScore(exam.getPaperScore());
        questionAnalysisWrap.setDuration(exam.getDuration());

        List<QuestionAnalysis> questionAnalysisList = baseMapper.questionAnalysisByExamId(examId);
        List<QuestionAnalysisWrap.QuestionType> questionTypeList = paperDetail.getQuestionTypeList().stream().map(item -> {
            QuestionAnalysisWrap.QuestionType qt = new QuestionAnalysisWrap.QuestionType();
            BeanUtil.copyProperties(item, qt, true);
            return qt;
        }).collect(Collectors.toList());
        questionAnalysisWrap.setQuestionTypeList(questionTypeList);

        questionTypeList.stream().forEach(qt -> {
            for (QuestionAnalysisWrap.Question question : qt.getQuestionList()) {
                QuestionAnalysis questionAnalysis = questionAnalysisList.stream().filter(i -> i.getQuestionId().equals(question.getId())).findFirst().orElse(null);
                if (questionAnalysis == null) {
                    questionAnalysis = new QuestionAnalysis();
                    questionAnalysis.setQuestionId(question.getId());
                    questionAnalysis.setRightCount(0);
                    questionAnalysis.setWrongCount(0);
                    questionAnalysis.setAvgScore(BigDecimal.ZERO);
                    questionAnalysis.setScoringRate(0);
                }
                question.setQuestionAnalysis(questionAnalysis);
            }
        });

        return questionAnalysisWrap;
    }

    @Override
    public ExamRecordDetails recordDetails(Long examineeRecordId) {
        ExamineeRecord examineeRecord = this.getById(examineeRecordId);
        if (examineeRecord == null) {
            throw new IllegalParamsException("考试记录不存在或已删除！");
        }

        Examinee examinee = examineeMapper.selectById(examineeRecord.getExamineeId());
        if (examinee == null) {
            throw new IllegalParamsException("考生不存在或已删除！");
        }

        Exam exam = examMapper.selectById(examinee.getExamId());
        if (exam == null) {
            throw new IllegalParamsException("考试不存在或已删除！");
        }

        PaperDetailDTO paperDetail = paperService.getPaperDetail(exam.getPaperId());
        if (paperDetail == null) {
            throw new ServiceException("查不到考试试卷！");
        }

        ExamRecordDetails examRecordDetails = new ExamRecordDetails();
        BeanUtil.copyProperties(examineeRecord, examRecordDetails);
        examRecordDetails.setExamName(exam.getExamName());
        examRecordDetails.setTotalScore(exam.getPaperScore());
        examRecordDetails.setDuration(exam.getDuration());

        Long studentId = examinee.getStudentId();
        if (studentId != null) {
            IdOrgRequest idRequest = new IdOrgRequest();
            idRequest.setId(studentId);
            idRequest.setOrgId(SecurityContextHolder.getOrgId());
            R<StudentDTO> r = remoteStudentService.detail(idRequest);
            if (r != null) {
                StudentDTO studentDTO = r.getData();
                if (studentDTO != null) {
                    examRecordDetails.setExamineeName(studentDTO.getRealName());
                    examRecordDetails.setExamineePhone(studentDTO.getPhone());
                }
            }
        }

        List<ExamRecordDetails.QuestionType> questionTypeList = paperDetail.getQuestionTypeList().stream().map(item -> {
            ExamRecordDetails.QuestionType qt = new ExamRecordDetails.QuestionType();
            BeanUtil.copyProperties(item, qt, true);
            return qt;
        }).collect(Collectors.toList());
        examRecordDetails.setQuestionTypeList(questionTypeList);

        List<ExamineeRecordQuestion> examineeRecordQuestionList = examineeRecordQuestionService.lambdaQuery().eq(ExamineeRecordQuestion::getExamineeRecordId, examineeRecordId).list();
        questionTypeList.stream().forEach(qt -> {
            for (ExamRecordDetails.Question question : qt.getQuestionList()) {
                ExamineeRecordQuestion examineeRecordQuestion = examineeRecordQuestionList.stream().filter(i -> i.getQuestionId().equals(question.getId())).findFirst().orElse(null);
                question.setAnswerStatus(examineeRecordQuestion.getAnswerStatus());
                question.setExamineeAnswer(examineeRecordQuestion.getAnswer());
                question.setCorrectResult(examineeRecordQuestion.getCorrectResult());
                question.setExamineeQuestionId(examineeRecordQuestion.getId());
                question.setExamineeScore(examineeRecordQuestion.getScore());
                question.setCorrectBy(examineeRecordQuestion.getCorrectBy());
            }
        });

        // 填充批改人
        List<Long> userIdList = examineeRecordQuestionList.stream().map(erq -> erq.getCorrectBy()).collect(Collectors.toList());
        userIdList.removeIf(userId -> userId == null || userId.equals(-1L)|| userId.equals(0L));
        List<UserListResultDTO> userList = userService.getUserList(userIdList);
        questionTypeList.stream().forEach(qt -> {
            for (ExamRecordDetails.Question question : qt.getQuestionList()) {
                Long correctBy = question.getCorrectBy();
                if (correctBy != null) {
                    if (correctBy.equals(-1L) || correctBy.equals(0L)) {
                        question.setCorrectByName("系统自动批改");
                    } else {
                        if (CollectionUtils.isNotEmpty(userList)) {
                            UserListResultDTO user = userList.stream().filter(i -> i.getId().equals(correctBy)).findFirst().orElse(null);
                            if (user != null) {
                                question.setCorrectByName(user.getRealName());
                            }
                        }
                    }
                }
            }
        });


        return examRecordDetails;
    }


    @Override
    public IPage<ExamPageResponse> page(ExamPageRequest params) {
        return examineeMapper.page(params.createMpPage(), params);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRecord(Long examineeRecordId) {
        if (examineeRecordId == null) {
            throw new ServiceException("考试记录ID不能为空！");
        }

        ExamineeRecord examineeRecord = this.getById(examineeRecordId);
        if (examineeRecord == null) {
            throw new ServiceException("考试记录不存在或已删除！");
        }

        Long examineeId = examineeRecord.getExamineeId();
        // 删除考题
        this.examineeRecordQuestionService.lambdaUpdate().eq(ExamineeRecordQuestion::getExamineeRecordId, examineeRecordId).remove();
        // 删除考试记录
        this.removeById(examineeRecordId);

        // 更新考生剩余考试次数
        Examinee examinee = examineeMapper.selectById(examineeId);
        Exam exam = examMapper.selectById(examinee.getExamId());
        Integer examLimitCount = exam.getLimitCount();
        if (examLimitCount != null && examLimitCount > 0) {
            examineeService.updateRemainingTimes(examineeId, examLimitCount);
        }

        // 如果被删除的这条考试记录是被纳入计算的，那么要重新选择1条最高分的记录纳入计算
        Boolean calculated = examineeRecord.getCalculated();
        if (calculated != null && calculated) {
            // 重新选择其它最高分的考试记录，标记为纳入计算
            this.updateCalculated(examineeId);
        }

        // 更新考生剩余考试次数
        if (examLimitCount != null && examLimitCount > 0) {
            examineeService.updateRemainingTimes(examineeId, examLimitCount);
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCalculated(Long examineeId) {
        if (examineeId != null) {
            List<ExamineeRecord> examineeRecordList = this.lambdaQuery()
                    .eq(ExamineeRecord::getExamineeId, examineeId)
                    .ge(ExamineeRecord::getStatus, ExamineeRecordStatusEnum.CORRECTED.getStatus())
                    .list();
            if (CollectionUtils.isNotEmpty(examineeRecordList)) {
                if (examineeRecordList.size() == 1) {
                    ExamineeRecord examineeRecord = examineeRecordList.get(0);
                    examineeRecord.setCalculated(true);
                    this.updateById(examineeRecord);
                    // 添加或更新成绩
                    examineeScoreService.addOrUpdateExamineeScore(examineeRecord);
                    return;
                }

                BigDecimal maxScore = examineeRecordList.stream().map(ExamineeRecord::getScore).max(BigDecimal::compareTo).get();
                Iterator<ExamineeRecord> iterator = examineeRecordList.iterator();
                while (iterator.hasNext()) {
                    ExamineeRecord examineeRecord = iterator.next();
                    if (maxScore.compareTo(examineeRecord.getScore()) == 0) {
                        examineeRecord.setCalculated(true);
                        this.updateById(examineeRecord);
                        // 添加或更新成绩
                        examineeScoreService.addOrUpdateExamineeScore(examineeRecord);
                        iterator.remove();
                        break;
                    }
                }

                examineeRecordList.forEach(i -> i.setCalculated(false));
                this.updateBatchById(examineeRecordList);
            }
        }
    }

    @Override
    public Long selectCountByExamineeId(Long examineeId) {
        Long count = this.lambdaQuery().eq(ExamineeRecord::getExamineeId, examineeId)
                .notIn(ExamineeRecord::getStatus,ExamineeRecordStatusEnum.UNFINISH.getStatus()).count();
        return count;
    }
}

