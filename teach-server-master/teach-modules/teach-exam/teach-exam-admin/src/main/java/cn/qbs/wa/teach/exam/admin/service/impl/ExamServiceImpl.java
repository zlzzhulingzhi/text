package cn.qbs.wa.teach.exam.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.EnableRequest;
import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.exam.admin.mapper.ExamMapper;
import cn.qbs.wa.teach.exam.admin.mapper.ExamineeRecordMapper;
import cn.qbs.wa.teach.exam.admin.pojo.exam.*;
import cn.qbs.wa.teach.exam.admin.service.*;
import cn.qbs.wa.teach.exam.common.constant.Topics;
import cn.qbs.wa.teach.exam.common.entity.*;
import cn.qbs.wa.teach.exam.common.enumerate.*;
import cn.qbs.wa.teach.exam.common.pojo.ExamAndCertDetailResponse;
import cn.qbs.wa.teach.common.core.utils.DateFormatUtils;
import cn.qbs.wa.teach.exam.common.util.RedisLockKeyUtil;
import cn.qbs.wa.teach.organization.api.RemoteEmployeeService;
import cn.qbs.wa.teach.organization.api.RemoteStudentService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.employee.EmployeePageResultDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.employee.EmployeePageSearchDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentSearchDTO;
import cn.qbs.wa.teach.question.api.pojo.DTO.paper.BasicPaper;
import cn.qbs.wa.teach.question.api.pojo.DTO.paper.PaperDetailDTO;
import cn.qbs.wa.teach.question.api.pojo.DTO.question.QuestionDetailsDTO;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qbs.tdmq.producer.TdmqProducerTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 考试表(Exam)表服务实现类
 *
 * @author zcm
 * @since 2021-12-14 11:40:51
 */
@Slf4j
@Service("examService")
@RequiredArgsConstructor
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam> implements ExamService {

    private final RuleService ruleService;

    private final ExamRuleService examRuleService;

    private final ExamUserVisibleService examUserVisibleService;

    private final PaperService paperService;

    private final ExamineeService examineeService;

    private final ExamineeRecordService examineeRecordService;

    private final ExamineeRecordQuestionService examineeRecordQuestionService;

    private final JobService jobService;

    private final ExamineeRecordMapper examineeRecordMapper;

    private final TdmqProducerTemplate tdmqProducerTemplate;

    /**
     * 启动任务提前秒数（允许提前多少秒启动）
     */
    private static final long START_JOB_ADVANCE_SECONDS = 10L;

    private final RedissonClient redissonClient;

    private final RemoteEmployeeService remoteEmployeeService;

    @Resource
    private RemoteStudentService remoteStudentService;

    @Resource
    private RemoteService remoteService;

    @Override
    public void checkExamTime(Long examId) {
        Exam exam = this.getById(examId);
        if (ExamStatusEnum.NOT_ON_SHELF.getStatus() == exam.getShelfStatus() ){
            throw new ServiceException("该考试已下架或禁用！");
        }
        LocalDateTime now = LocalDateTime.now();
        if (ExamModeEnum.STANDARD.getMode() == exam.getExamMode() && now.isAfter(exam.getEndTime())){
            throw new ServiceException("考试已结束！");
        }
        if (ExamModeEnum.ADVANCED.getMode() == exam.getExamMode() && now.isAfter(exam.getAdmissionEndTime())){
            throw new ServiceException("考试入场截止时间已到！");
        }
    }

    @Override
    @Transactional
    public Long add(ExamAddRequest params) {
        checkParams(params);

        Long paperId = params.getPaperId();
        PaperDetailDTO paperDetail = paperService.getPaperDetail(paperId);
        if (paperDetail == null) {
            throw new ServiceException("查不到试卷！");
        }
        BigDecimal totalScore = paperDetail.getTotalScore();
        if (params.getPassScore() != null && params.getPassScore().compareTo(totalScore) > 0) {
            throw new ServiceException("当前试卷总分" + totalScore + "分，通过分数设置要小于试卷总分！");
        }

        Exam exam = new Exam();
        BeanUtils.copyProperties(params, exam);
        exam.setPaperScore(totalScore);
        // 1: 创建时发布, 2 手动发布, 3: 定时发布
        Integer releaseMode = params.getReleaseMode();
        if (ReleaseModeEnum.TIMED_RELEASE.getId() != releaseMode) {
            exam.setReleaseTime(null);
        }
        this.save(exam);

        Long examId = exam.getId();

        // 需求更改，不需要考试分类
        /*List<ExamCategory> examCategoryList = params.getCategoryIdList().stream().distinct().map(categoryId -> {
            ExamCategory examCategory = new ExamCategory();
            examCategory.setExamId(examId);
            examCategory.setCategoryId(categoryId);
            return examCategory;
        }).collect(Collectors.toList());
        this.examCategoryService.saveBatch(examCategoryList);*/

        List<ExamRule> examRuleList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(params.getRuleIdList())) {
            List<ExamRule> ruleList = params.getRuleIdList().stream().distinct().map(ruleId -> {
                ExamRule examRule = new ExamRule();
                examRule.setExamId(examId);
                examRule.setRuleId(ruleId);
                return examRule;
            }).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(ruleList)) {
                examRuleList.addAll(ruleList);
            }
        }
        if (CollectionUtils.isNotEmpty(examRuleList)) {
            this.examRuleService.saveBatch(examRuleList);
        }

        if (params.getUserVisible() == 2) {
            if (CollectionUtils.isEmpty(params.getVisibleUserList())) {
                throw new ServiceException("可见用户列表不能为空！");
            }
            List<ExamUserVisible> examUserVisibleList = params.getVisibleUserList().stream().distinct().map(user -> {
                ExamUserVisible examUserVisible = new ExamUserVisible();
                examUserVisible.setExamId(examId);
                examUserVisible.setUserId(user.getUserId());
                examUserVisible.setEmployeeId(user.getEmployeeId());
                return examUserVisible;
            }).collect(Collectors.toList());
            this.examUserVisibleService.saveBatch(examUserVisibleList);
        }

        paperService.editable(paperId, false);

        LocalDateTime releaseTime = params.getReleaseTime();
        LocalDateTime startTime = params.getStartTime();
        LocalDateTime now = LocalDateTime.now();

        // 1: 创建时发布, 2 手动发布, 3: 定时发布
        if (ReleaseModeEnum.CREATED_RELEASE.getId() == releaseMode) {
            this.onShelf(exam);
            releaseTime = now;
        } else if (ReleaseModeEnum.TIMED_RELEASE.getId() == releaseMode) {
            if (ChronoUnit.SECONDS.between(now, releaseTime) <= START_JOB_ADVANCE_SECONDS) {
                // 现在时间距发布时间相差10秒内，立即启动，不再走xxl-job。避免出现任务不执行导致无法定时上架的问题
                this.onShelf(exam);
            } else {
                // 创建定时任务
                jobService.addTimedOnShelfExamTask(exam.getId(), releaseTime);
            }
        }

        if (!startTime.equals(releaseTime)) {
            if (ChronoUnit.SECONDS.between(now, startTime) <= START_JOB_ADVANCE_SECONDS) {
                // 现在时间距开始时间相差10秒内，立即开始，不再走xxl-job。避免出现任务不执行导致无法定时开始的问题
                this.start(examId);
            } else {
                // 创建定时任务
                jobService.addTimedStartExamTask(exam.getId(), startTime);
            }
        }

        jobService.addTimedEndExamTask(exam.getId(), exam.getEndTime());
        // 考试结束，不再自动下架
//        jobService.addTimedOffShelfExamTask(examId, params.getEndTime());

        return examId;
    }

    private void checkParams(ExamAddRequest params) {
        if (params.getStartTime().isBefore(LocalDateTime.now())) {
            throw new IllegalParamsException("考试开始时间不能早于当前时间！");
        }

        if (!params.getStartTime().isBefore(params.getEndTime())) {
            throw new IllegalParamsException("考试截止时间不能早于或等于开始时间！");
        }
        /*if (params.getDuration() > ChronoUnit.MINUTES.between(params.getStartTime(), params.getEndTime())) {
            throw new IllegalParamsException("考试时长不能大于有效开考时间！");
        }*/

        if (params.getPassScore() != null && params.getPassScore().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalParamsException("考试通过分数不能小于或等于0！");
        }

        if (ReleaseModeEnum.TIMED_RELEASE.getId() == params.getReleaseMode()) {
            LocalDateTime releaseTime = params.getReleaseTime();
            if (releaseTime == null) {
                throw new IllegalParamsException("考试定时发布，发布时间不能为空！");
            }
            if (releaseTime.isBefore(LocalDateTime.now())) {
                throw new IllegalParamsException("发布时间不能早于当前时间！");
            }
        }
    }

    @Deprecated
    @Override
    public IPage<ExamPageResponse> page(ExamPageRequest params) {
        IPage<ExamPageResponse> page = baseMapper.page(params.createMpPage(), params);
        List<ExamPageResponse> records = page.getRecords();
        if (CollectionUtils.isNotEmpty(records)) {
            List<Long> userIdList = records.stream().map(ExamPageResponse::getCreateBy).collect(Collectors.toList());
            EmployeePageSearchDTO employeePageSearchDTO = new EmployeePageSearchDTO();
            employeePageSearchDTO.setUserIdList(userIdList);
            employeePageSearchDTO.setSize(userIdList.size());
            R<PageResultComDTO<EmployeePageResultDTO>> remoteResult = remoteEmployeeService.page(employeePageSearchDTO);
            PageResultComDTO<EmployeePageResultDTO> remoteData = remoteResult.getRemoteData();

            Map<Long, EmployeePageResultDTO> userMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(remoteData.getRecords())) {
                userMap = remoteData.getRecords().stream().collect(Collectors.toMap(EmployeePageResultDTO::getUserId, o -> o, (o1, o2) -> o1));
            }
            Map<Long, EmployeePageResultDTO> finalUserMap = userMap;

            List<Long> examIdList = records.stream().map(ExamPageResponse::getId).collect(Collectors.toList());
            List<ExamRuleDTO> examRuleList = baseMapper.selectExamRuleByExamIdList(examIdList);
            records.forEach(i -> {
                EmployeePageResultDTO u = finalUserMap.get(i.getCreateBy());
                if (u != null) {
                    i.setCreateByName(u.getRealName());
                }
                List<ExamRuleDTO> collect = examRuleList.stream().filter(r -> r.getExamId().equals(i.getId())).collect(Collectors.toList());
                i.setRuleList(collect);
            });
        }
        return page;
    }

    @Override
    public IPage<ExamPageResponseV2> pageV2(ExamPageRequestV2 params) {
        IPage<ExamPageResponseV2> page = baseMapper.pageV2(params.createMpPage(), params);
        List<ExamPageResponseV2> examList = page.getRecords();
        if (CollectionUtils.isNotEmpty(examList)) {
            List<Long> userIdList = examList.stream().map(ExamPageResponseV2::getCreateBy).collect(Collectors.toList());
            EmployeePageSearchDTO employeePageSearchDTO = new EmployeePageSearchDTO();
            employeePageSearchDTO.setUserIdList(userIdList);
            employeePageSearchDTO.setSize(userIdList.size());
            R<PageResultComDTO<EmployeePageResultDTO>> remoteResult = remoteEmployeeService.page(employeePageSearchDTO);
            PageResultComDTO<EmployeePageResultDTO> remoteData = remoteResult.getRemoteData();

            // 查询创建人姓名
            Map<Long, EmployeePageResultDTO> userMap = new HashMap<>();
            if (remoteData != null && CollectionUtils.isNotEmpty(remoteData.getRecords())) {
                userMap = remoteData.getRecords().stream().collect(Collectors.toMap(EmployeePageResultDTO::getUserId, o -> o, (o1, o2) -> o1));
            }
            Map<Long, EmployeePageResultDTO> finalUserMap = userMap;
            examList.forEach(i -> {
                EmployeePageResultDTO u = finalUserMap.get(i.getCreateBy());
                if (u != null) {
                    i.setCreateByName(u.getRealName());
                }
            });

            // 查询试卷名称
            List<Long> paperIdList = examList.stream().map(ExamPageResponseV2::getPaperId).distinct().collect(Collectors.toList());
            List<BasicPaper> basicPaperList = paperService.getBasicList(paperIdList);
            if(CollectionUtils.isNotEmpty(basicPaperList)) {
                Map<Long, BasicPaper> paperIdMap = basicPaperList.stream().collect(Collectors.toMap(BasicPaper::getId, Function.identity()));
                examList.forEach(e -> {
                    BasicPaper basicPaper = paperIdMap.get(e.getPaperId());
                    if (basicPaper != null) {
                        e.setPaperName(basicPaper.getName());
                    }
                });
            }

            if (ShelfStatusEnum.ON.getStatus() == params.getReleaseStatus()) {
                Map<Long, ExamPageResponseV2> responseMap = examList.stream().collect(Collectors.toMap(ExamPageResponseV2::getId, Function.identity()));
                // 按状态分组
                Map<Integer, List<Long>> statusExamIdListMap = examList.stream().collect(
                        Collectors.groupingBy(
                                ExamPageResponseV2::getStatus,
                                Collectors.mapping(ExamPageResponseV2::getId, Collectors.toList())
                        )
                );
                statusExamIdListMap.forEach((status, examIds) -> {
                    ExamStatusEnum examStatusEnum = ExamStatusEnum.fromStatus(status);
                    switch (examStatusEnum) {
                        case NOT_START:
                            // 暂不做处理
                            break;

                        case DURING_EXAM:
                            // 考试中统计
                            List<StatisticsInExam> statisticsInExamList = this.getBaseMapper().statisticsInExamByExamIdList(examIds);
                            if (CollectionUtils.isNotEmpty(statisticsInExamList)) {
                                statisticsInExamList.forEach(i -> {
                                    ExamPageResponseV2 examPageResponseV2 = responseMap.get(i.getExamId());
                                    if (examPageResponseV2 != null) {
                                        examPageResponseV2.setExamineeCount(i.getExamineeCount());
                                        examPageResponseV2.setSubmitCount(i.getSubmitCount());
                                    }
                                });
                            }
                            break;

                        case EXAM_ENDED:
                            // 考试后统计
                            List<StatisticsAfterExam> examineeAndSubmitCountList = this.getBaseMapper().statisticsAfterExamByExamIdList(examIds);
                            if (CollectionUtils.isNotEmpty(examineeAndSubmitCountList)) {
                                examineeAndSubmitCountList.forEach(i -> {
                                    ExamPageResponseV2 examPageResponseV2 = responseMap.get(i.getExamId());
                                    if (examPageResponseV2 != null) {
                                        examPageResponseV2.setExamineeCount(i.getExamineeCount());
                                        examPageResponseV2.setSubmitCount(i.getSubmitCount());
                                        examPageResponseV2.setCorrectCount(i.getCorrectCount());
                                    }
                                });
                            }
                            break;
                    }
                });

                boolean needQueryExamRule = false;
                for (Integer status : statusExamIdListMap.keySet()) {
                    if (status.compareTo(ExamStatusEnum.DURING_EXAM.getStatus()) >= 0) {
                        needQueryExamRule = true;
                    }
                }
                if (needQueryExamRule) {
                    // 查询考试是否设置开启摄像头
                    List<Long> examIdList = examList.stream().map(ExamPageResponseV2::getId).collect(Collectors.toList());
                    List<ExamRuleDTO> examRuleList = baseMapper.selectExamRuleByExamIdListAndCode(examIdList, RuleEnum.CAMERA_RECORDING.getCode());
                    if (CollectionUtils.isNotEmpty(examRuleList)) {
                        Map<Long, ExamRuleDTO> examRuleMap = examRuleList.stream().collect(Collectors.toMap(ExamRuleDTO::getExamId, r -> r, (r1, r2) -> r2));
                        examList.forEach(i -> {
                            // 是否可以监考
                            i.setCanInvigilate(i.getStatus() >= ExamStatusEnum.DURING_EXAM.getStatus() && examRuleMap.get(i.getId()) != null);
                        });
                    }
                }
            }
        }
        return page;
    }

    @Override
    public IPage<ExamPageResponseV3> pageV3(ExamPageRequestV3 params) {
        IPage<ExamPageResponseV3> page = baseMapper.pageV3(params.createMpPage(), params);
        List<ExamPageResponseV3> examList = page.getRecords();
        if (CollectionUtils.isNotEmpty(examList)) {
            List<Long> userIdList = examList.stream().map(ExamPageResponseV3::getCreateBy).collect(Collectors.toList());
            EmployeePageSearchDTO employeePageSearchDTO = new EmployeePageSearchDTO();
            employeePageSearchDTO.setUserIdList(userIdList);
            employeePageSearchDTO.setSize(userIdList.size());
            R<PageResultComDTO<EmployeePageResultDTO>> remoteResult = remoteEmployeeService.page(employeePageSearchDTO);
            PageResultComDTO<EmployeePageResultDTO> remoteData = remoteResult.getRemoteData();

            // 查询创建人姓名
            Map<Long, EmployeePageResultDTO> userMap = new HashMap<>();
            if (remoteData != null && CollectionUtils.isNotEmpty(remoteData.getRecords())) {
                userMap = remoteData.getRecords().stream().collect(Collectors.toMap(EmployeePageResultDTO::getUserId, o -> o, (o1, o2) -> o1));
            }
            Map<Long, EmployeePageResultDTO> finalUserMap = userMap;
            examList.forEach(i -> {
                EmployeePageResultDTO u = finalUserMap.get(i.getCreateBy());
                if (u != null) {
                    i.setCreateByName(u.getRealName());
                }
            });

            // 查询试卷名称
            List<Long> paperIdList = examList.stream().map(ExamPageResponseV3::getPaperId).distinct().collect(Collectors.toList());
            List<BasicPaper> basicPaperList = paperService.getBasicList(paperIdList);
            if(CollectionUtils.isNotEmpty(basicPaperList)) {
                Map<Long, BasicPaper> paperIdMap = basicPaperList.stream().collect(Collectors.toMap(BasicPaper::getId, Function.identity()));
                examList.forEach(e -> {
                    BasicPaper basicPaper = paperIdMap.get(e.getPaperId());
                    if (basicPaper != null) {
                        e.setPaperName(basicPaper.getName());
                    }
                });
            }

            if (ShelfStatusEnum.ON.getStatus() == params.getReleaseStatus()) {
                Map<Long, ExamPageResponseV3> responseMap = examList.stream().collect(Collectors.toMap(ExamPageResponseV3::getId, Function.identity()));
                // 按状态分组
                Map<Integer, List<Long>> statusExamIdListMap = examList.stream().collect(
                        Collectors.groupingBy(
                                ExamPageResponseV3::getStatus,
                                Collectors.mapping(ExamPageResponseV3::getId, Collectors.toList())
                        )
                );


                boolean needQueryExamRule = false;
                for (Integer status : statusExamIdListMap.keySet()) {
                    if (status.compareTo(ExamStatusEnum.DURING_EXAM.getStatus()) >= 0) {
                        needQueryExamRule = true;
                    }
                }
                if (needQueryExamRule) {
                    // 查询考试是否设置开启摄像头
                    List<Long> examIdList = examList.stream().map(ExamPageResponseV3::getId).collect(Collectors.toList());
                    List<ExamRuleDTO> examRuleList = baseMapper.selectExamRuleByExamIdListAndCode(examIdList, RuleEnum.CAMERA_RECORDING.getCode());
                    if (CollectionUtils.isNotEmpty(examRuleList)) {
                        Map<Long, ExamRuleDTO> examRuleMap = examRuleList.stream().collect(Collectors.toMap(ExamRuleDTO::getExamId, r -> r, (r1, r2) -> r2));
                        examList.forEach(i -> {
                            // 是否可以监考
                            i.setCanInvigilate(i.getStatus() >= ExamStatusEnum.DURING_EXAM.getStatus() && examRuleMap.get(i.getId()) != null);
                        });
                    }
                }
            }
        }
        return page;
    }

    @Override
    public IPage<ExamUserRecordPageResponse> assignUserExamRecordPage(ExamUserRecordPageRequest params) {
        Exam exam = this.getById(params.getExamId());
        IPage<ExamUserRecordPageResponse> page = null;
        StudentSearchDTO studentSearchDTO = new StudentSearchDTO();
        BeanUtils.copyProperties(params, studentSearchDTO);
        R<List<StudentDTO>> selectStudentList = remoteStudentService.list(studentSearchDTO);
        if (CollectionUtils.isNotEmpty(selectStudentList.getData())){
            params.setUserIdList(selectStudentList.getData().stream().map(StudentDTO::getUserId).collect(Collectors.toList()));
        }else if (StringUtils.isNotEmpty(params.getPhone()) || StringUtils.isNotEmpty(params.getRealName())
                || org.apache.commons.collections4.CollectionUtils.isNotEmpty(params.getDeptIdList()) || org.apache.commons.collections4.CollectionUtils.isNotEmpty(params.getGroupIdList())){
            return new Page<>(params.getCurrent(), params.getSize(), 0L);
        }
        if (UserVisibleStatusEnum.ALL.getCode().equals(exam.getUserVisible())){
            page = baseMapper.assignUserExamRecordPageAllVisible(params.createMpPage(), params);
        }else {
            page = baseMapper.assignUserExamRecordPage(params.createMpPage(), params);
        }
        if (CollectionUtils.isEmpty(page.getRecords())){
            return page;
        }
        List<Long> studentIdList = page.getRecords().stream().map(ExamUserRecordPageResponse::getStudentId).collect(Collectors.toList());
        StudentSearchDTO searchDTO = new StudentSearchDTO();
        searchDTO.setIdList(studentIdList);
        R<List<StudentDTO>> studentList = remoteStudentService.list(searchDTO);
        if (CollectionUtils.isNotEmpty(studentList.getData())){
            Map<Long, StudentDTO> studentMap = studentList.getData().stream().collect(Collectors.toMap(StudentDTO::getId, o -> o));
            page.getRecords().forEach(i -> {
                i.setStudentName(studentMap.get(i.getStudentId()).getRealName());
                i.setPhone(studentMap.get(i.getStudentId()).getPhone());
                i.setDeptName(studentMap.get(i.getStudentId()).getDeptName());
                i.setGroupList(studentMap.get(i.getStudentId()).getGroupList());
            });
        }
        return page;
    }

    @Override
    public PageResultComDTO<StudentDTO> examAssignUserPage(ExamAssignUserPageRequest pageRequest) {
        StudentSearchDTO studentSearchDTO = new StudentSearchDTO();
        BeanUtils.copyProperties(pageRequest, studentSearchDTO);
        R<PageResultComDTO<StudentDTO>> studentList = remoteStudentService.pageWithStaff(studentSearchDTO);
        if (CollectionUtils.isEmpty(studentList.getData().getRecords())){
            return studentList.getData();
        }
        List<Long> studentIdList = studentList.getData().getRecords().stream().map(StudentDTO::getId).distinct().collect(Collectors.toList());
        List<ExamUserVisible> examUserVisibleList = examUserVisibleService.lambdaQuery().eq(ExamUserVisible::getExamId, pageRequest.getExamId()).in(ExamUserVisible::getStudentId, studentIdList).list();
        if (CollectionUtils.isEmpty(examUserVisibleList)){
            return studentList.getData();
        }
        List<Long> existIdList = examUserVisibleList.stream().map(ExamUserVisible::getStudentId).distinct().collect(Collectors.toList());
        studentList.getData().getRecords().forEach(i -> {
            if (existIdList.contains(i.getId())){
                i.setAdded(1);
            }
        });
        return studentList.getData();
    }

    @Override
    public boolean assignUser(ExamAssignUserRequest pageRequest) {
        //考试时间检查
        checkExamTime(pageRequest.getExamAssignUserList().get(0).getExamId());
        List<ExamUserVisible> examUserVisibleList = BeanUtil.copyToList(pageRequest.getExamAssignUserList(), ExamUserVisible.class);
        return examUserVisibleService.saveBatch(examUserVisibleList);
    }

    @Override
    public ExamDetailResponse detail(Serializable examId) {
        ExamDetailResponse examDetailResponse = baseMapper.selectDetailById(examId);
        // 查询可见用户
        List<VisibleUser> userVisibleList = examDetailResponse.getVisibleUserList();
        if (!userVisibleList.isEmpty()) {
            // 远程获取用户信息
            List<Long> userIds = userVisibleList.stream().map(VisibleUser::getUserId).collect(Collectors.toList());

            /*Map<Long, StudentDTO> studentMap = new HashMap<>(userIds.size());
            StudentSearchDTO searchDTO = new StudentSearchDTO();
            searchDTO.setUserIds(userIds);
            searchDTO.setEnabled(1);
            List<StudentDTO> studentList = unboxingRemoteCallData(remoteStudentService.list(searchDTO));
            if (CollUtil.isNotEmpty(studentList)) {
                studentMap = studentList.stream().collect(Collectors.toMap(StudentDTO::getUserId, o -> o, (o1, o2) -> o1));
            }*/
            Map<Long, StudentDTO> finalUserMap = this.remoteService.remoteStudentMap(userIds);
            List<VisibleUser> dtoList = userVisibleList.stream().map(e -> {
                VisibleUser userVisibleDTO = new VisibleUser();
                BeanUtils.copyProperties(e, userVisibleDTO);
                Optional.ofNullable(finalUserMap.get(e.getUserId())).ifPresent(user -> {
                    userVisibleDTO.setRealName(user.getRealName());
                    userVisibleDTO.setPhone(user.getPhone());
                    userVisibleDTO.setStudentId(user.getId());
                });
                return userVisibleDTO;
            }).collect(Collectors.toList());
            examDetailResponse.setVisibleUserList(dtoList);
        }

        if (examDetailResponse != null) {
            List<ExamRuleConfigDTO> examRuleConfigList = this.ruleService.getExamRuleConfig(SecurityContextHolder.getOrgId());
            examDetailResponse.setRuleList(examRuleConfigList);

            List<Long> ruleIdList = examDetailResponse.getRuleIdList();
            if (CollectionUtils.isNotEmpty(ruleIdList)) {
                examRuleConfigList.forEach(i -> {
                    i.getRuleList().forEach(rule -> {
                        if (ruleIdList.contains(rule.getId())) {
                            rule.setSelected(true);
                        }
                    });
                });
            }

            PaperDetailDTO paperDetail = this.paperService.getPaperDetail(examDetailResponse.getPaperId());
            if (paperDetail != null) {
                examDetailResponse.setPaperName(paperDetail.getName());
            }
        }
       return examDetailResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(ExamUpdateRequest params) {
        Long examId = params.getId();
        if (examId == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        Exam exam = this.getById(examId);
        if (exam == null) {
            throw new ServiceException("考试不存在或已删除！");
        }
        if (ShelfStatusEnum.ON.getStatus() == exam.getShelfStatus()) {
            throw new ServiceException("考试已发布不能修改！");
        }

        checkParams(params);

        PaperDetailDTO paperDetail = paperService.getPaperDetail(params.getPaperId());
        if (paperDetail == null) {
            throw new ServiceException("查询试卷详情失败！");
        }

        BigDecimal passScore = params.getPassScore();
        BigDecimal totalScore = paperDetail.getTotalScore();
        if (passScore != null && passScore.compareTo(totalScore) > 0) {
            throw new ServiceException("当前试卷总分" + totalScore + "分，通过分数设置要小于试卷总分！");
        }

        LocalDateTime oldReleaseTime = exam.getReleaseTime();
        LocalDateTime oldStartTime = exam.getStartTime();
        LocalDateTime oldEndTime = exam.getEndTime();

        BeanUtils.copyProperties(params, exam);
        exam.setPaperScore(totalScore);
        // 1: 创建时发布, 2 手动发布, 3: 定时发布
        Integer releaseMode = params.getReleaseMode();
        if (ReleaseModeEnum.TIMED_RELEASE.getId() != releaseMode) {
            exam.setReleaseTime(null);
        }
        boolean success = this.updateById(exam);

        if (success) {
            List<ExamRule> addExamRuleList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(params.getRuleIdList())) {
                List<ExamRule> ruleList = params.getRuleIdList().stream().distinct().map(ruleId -> {
                    ExamRule examRule = new ExamRule();
                    examRule.setExamId(examId);
                    examRule.setRuleId(ruleId);
                    return examRule;
                }).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(ruleList)) {
                    addExamRuleList.addAll(ruleList);
                }
            }

            // 查询数据库中当前考试已有的考试规则
            List<ExamRule> dbExamRuleList = examRuleService.lambdaQuery().eq(ExamRule::getExamId, examId).list();
            if (CollectionUtils.isNotEmpty(dbExamRuleList)) {
                // 从数据库已有的考试规则中移除参数中没有的考试规则
                List<Long> addRuleIdList = addExamRuleList.stream().map(rule -> rule.getRuleId()).collect(Collectors.toList());
                List<Long> deleteRuleIdList = new ArrayList<>();
                for (ExamRule examRule : dbExamRuleList) {
                    Long ruleId = examRule.getRuleId();
                    if (addRuleIdList.contains(ruleId)) {
                        // 从添加考试规则列表中移除数据库中存在的考试规则
                        addExamRuleList.removeIf(rule -> rule.getRuleId().equals(ruleId));
                    } else {
                        deleteRuleIdList.add(ruleId);
                    }
                }

                if (deleteRuleIdList.size() > 0) {
                    this.examRuleService.lambdaUpdate().eq(ExamRule::getExamId, examId).in(ExamRule::getRuleId, deleteRuleIdList).remove();
                }
            }

            if (CollectionUtils.isNotEmpty(addExamRuleList)) {
                this.examRuleService.saveBatch(addExamRuleList);
            }

            List<ExamUserVisible> addExamUserVisibleList;
            if (params.getUserVisible() == 1) {
                addExamUserVisibleList = new ArrayList<>(0);
            } else {
                if (CollectionUtils.isEmpty(params.getVisibleUserList())) {
                    throw new ServiceException("可见用户列表不能为空！");
                }
                addExamUserVisibleList = params.getVisibleUserList().stream().distinct().map(user -> {
                    ExamUserVisible examUserVisible = new ExamUserVisible();
                    examUserVisible.setExamId(examId);
                    examUserVisible.setUserId(user.getUserId());
                    examUserVisible.setEmployeeId(user.getEmployeeId());
                    return examUserVisible;
                }).collect(Collectors.toList());
            }

            List<ExamUserVisible> dbExamUserVisibleList = this.examUserVisibleService.lambdaQuery().eq(ExamUserVisible::getExamId, examId).list();
            if (CollectionUtils.isNotEmpty(dbExamUserVisibleList)) {
                // 从数据库已有的考试可见用户中移除参数中没有的可见用户
                List<Long> addUserIdList = addExamUserVisibleList.stream().map(i -> i.getUserId()).collect(Collectors.toList());
                List<Long> deleteUserIdList = new ArrayList<>();
                for (ExamUserVisible examUserVisible : dbExamUserVisibleList) {
                    Long userId = examUserVisible.getUserId();
                    if (addUserIdList.contains(userId)) {
                        // 从添加考试规则列表中移除数据库中存在的考试规则
                        addExamUserVisibleList.removeIf(i -> i.getUserId().equals(userId));
                    } else {
                        deleteUserIdList.add(userId);
                    }
                }

                if (deleteUserIdList.size() > 0) {
                    this.examUserVisibleService.lambdaUpdate().eq(ExamUserVisible::getExamId, examId).in(ExamUserVisible::getUserId, deleteUserIdList).remove();
                }
            }

            if (CollectionUtils.isNotEmpty(addExamUserVisibleList)) {
                this.examUserVisibleService.saveBatch(addExamUserVisibleList);
            }

            LocalDateTime releaseTime = params.getReleaseTime();
            LocalDateTime startTime = params.getStartTime();
            LocalDateTime now = LocalDateTime.now();

            // 1: 创建时发布, 2 手动发布, 3: 定时发布
            if (ReleaseModeEnum.CREATED_RELEASE.getId() == releaseMode) {
                this.onShelf(exam);
            } else if (ReleaseModeEnum.TIMED_RELEASE.getId() == releaseMode && !releaseTime.equals(oldReleaseTime)) {
                if (ChronoUnit.SECONDS.between(now, releaseTime) <= START_JOB_ADVANCE_SECONDS) {
                    // 现在时间距发布时间相差10秒内，立即启动，不再走xxl-job。避免出现任务不执行导致无法定时上架的问题
                    this.onShelf(exam);
                } else {
                    // 创建定时任务
                    jobService.addTimedOnShelfExamTask(exam.getId(), releaseTime);
                }
            }

            if (!startTime.equals(oldStartTime)) {
                if (ChronoUnit.SECONDS.between(now, startTime) <= START_JOB_ADVANCE_SECONDS) {
                    // 现在时间距开始时间相差10秒内，立即开始，不再走xxl-job。避免出现任务不执行导致无法定时开始的问题
                    this.start(examId);
                } else {
                    // 创建定时任务
                    jobService.addTimedStartExamTask(exam.getId(), startTime);
                }
            }

            jobService.addTimedEndExamTask(exam.getId(), exam.getEndTime());
            // 考试结束，不再自动下架
            /*LocalDateTime endTime = params.getEndTime();
            if (!endTime.equals(oldEndTime)) {
                jobService.addTimedOffShelfExamTask(examId, endTime);
            }*/
        }

        return success;
    }

    @Override
    @Transactional
    public boolean onShelf(List<Long> examIdList) {
        if (CollectionUtils.isNotEmpty(examIdList)) {
            for (Long examId : examIdList) {
                this.onShelf(examId);
                // 添加考生和考题修改为在用户进入考试时创建
//                afterOnShelf(examId);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onShelf(Long examId) {
        if (examId != null) {
            Exam exam = this.lambdaQuery()
                    .ne(Exam::getShelfStatus, ShelfStatusEnum.ON.getStatus())
                    .eq(Exam::getId, examId)
                    .last("limit 1").one();
            if (exam != null) {
                this.onShelf(exam);
            }
        }
    }

    private void onShelf(Exam exam) {
        if (exam == null) {
            return;
        }

        // 如果后面修改了考试定时上架时间，不会删除之前的定时任务，所以这里需要再次判断一下
        if (exam.getReleaseTime() == null || ChronoUnit.SECONDS.between(LocalDateTime.now(), exam.getReleaseTime()) <= START_JOB_ADVANCE_SECONDS) {
            this.lambdaUpdate().eq(Exam::getId, exam.getId())
                    .set(Exam::getShelfStatus, ShelfStatusEnum.ON.getStatus())
                    .set(exam.getReleaseTime() == null , Exam::getReleaseTime, LocalDateTime.now())
                    .set(!LocalDateTime.now().isBefore(exam.getStartTime()), Exam::getStatus, ExamStatusEnum.DURING_EXAM.getStatus())
                    .update();
        }
    }

    @Override
    @Transactional
    public void afterOnShelf(Long examId) {
        Exam exam = this.getById(examId);
        if (exam != null) {
            PaperDetailDTO paperDetail = paperService.getPaperDetail(exam.getPaperId());
            if (paperDetail == null) {
                throw new ServiceException("查询试卷详情失败！");
            }

            List<QuestionDetailsDTO> questionList = paperDetail.getQuestionTypeList().stream()
                    .flatMap(qt -> qt.getQuestionList().stream())
                    .collect(Collectors.toList());
            List<ExamineeRecordQuestion> examineeRecordQuestionList = questionList.stream().map(q -> {
                ExamineeRecordQuestion examineeRecordQuestion = new ExamineeRecordQuestion();
                examineeRecordQuestion.setQuestionId(q.getId());
                examineeRecordQuestion.setQuestionScore(q.getScore());
                return examineeRecordQuestion;
            }).collect(Collectors.toList());

            // 打乱试题顺序
            // 由前端打乱顺序
                /*Long count = examRuleService.lambdaQuery().eq(ExamRule::getExamId, examId).eq(ExamRule::getRuleId, RuleEnum.SHUFFLE_QUESTION.getId()).count();
                if (count != null && count > 0) {
                    Collections.shuffle(examineeRecordQuestionList);
                }*/

            List<ExamUserVisible> userVisibleList = examUserVisibleService.lambdaQuery().eq(ExamUserVisible::getExamId, examId).list();
            for (ExamUserVisible examUserVisible : userVisibleList) {
                Examinee examinee = new Examinee();
                examinee.setExamId(examId);
                examinee.setUserId(examUserVisible.getUserId());
                examinee.setEmployeeId(examUserVisible.getEmployeeId());
                examinee.setOrgId(examUserVisible.getOrgId());
                examineeService.save(examinee);

                addExamineeRecordAndQuestions(examinee, examineeRecordQuestionList);
            }

        }
    }

    /**
     * 添加考试记录和试题
     *
     * @param examinee
     * @param examineeRecordQuestionList
     */
    private void addExamineeRecordAndQuestions(Examinee examinee, List<ExamineeRecordQuestion> examineeRecordQuestionList) {
        ExamineeRecord examineeRecord = new ExamineeRecord();
        examineeRecord.setExamineeId(examinee.getId());
        examineeRecord.setOrgId(examinee.getOrgId());
        examineeRecord.setStatus(ExamineeRecordStatusEnum.UNFINISH.getStatus());
        examineeRecord.setCalculated(false);
        examineeRecordService.save(examineeRecord);

        examineeRecordQuestionList.forEach(q -> {
            q.setId(null);
            q.setExamineeRecordId(examineeRecord.getId());
            q.setOrgId(examinee.getOrgId());
        });

        examineeRecordQuestionService.saveBatch(examineeRecordQuestionList);
    }

    @Override
    public boolean offShelf(List<Long> examIdList) {
        if (CollectionUtils.isNotEmpty(examIdList)) {
            return this.lambdaUpdate().in(Exam::getId, examIdList).set(Exam::getShelfStatus, ShelfStatusEnum.OFF.getStatus()).update();
        }
        return false;
    }

    @Override
    public boolean offShelf(Long examId, boolean ignoreExamEndTime) {
        if (examId == null) {
            return false;
        }

        Exam exam = this.getById(examId);
        if (exam == null) {
            return false;
        }

        if (!ignoreExamEndTime) {
            // 如果后面修改了考试定时下架时间，不会删除之前的定时任务，所以这里需要再次判断一下
            if (LocalDateTime.now().isBefore(exam.getEndTime())) {
                return false;
            }
        }

        return this.lambdaUpdate().eq(Exam::getId, examId).set(Exam::getShelfStatus, ShelfStatusEnum.OFF.getStatus()).update();
    }

    @Override
    public boolean enable(EnableRequest request) {
        List<Long> idList = request.getIdList();
        if (request.getEnabled() == 0) {
            // 查询处于上架状态的考试
            List<Exam> onShelfExamList = this.lambdaQuery().in(Exam::getId, idList).eq(Exam::getShelfStatus, ShelfStatusEnum.ON.getStatus()).list();
            if (CollectionUtils.isNotEmpty(onShelfExamList)) {
                List<Long> examIdList = onShelfExamList.stream().map(i -> i.getId()).collect(Collectors.toList());
                throw new ServiceException(String.format("上架状态的考试%s不能禁用", examIdList.toString()));
            }
        }
        return this.lambdaUpdate().in(Exam::getId, idList).set(Exam::getEnabled, request.getEnabled()).update();
    }

    @Override
    public ExamStatistics statistics(Long examId) {
        // 查询考试基本信息，统计最高分、最低分、平均分
        ExamStatistics statistics = baseMapper.statistics(examId);
        if (statistics == null) {
            throw new ServiceException("查不到考试！");
        }

        // 统计提交人数、已批改人数、未批改人数
        /*ExamStatistics es = baseMapper.countCorrect(examId);
        if (es == null) {
            statistics.setSubmitCount(0);
//            statistics.setUnCorrectCount(0);
            statistics.setCorrectCount(0);
        } else {
//            statistics.setUnCorrectCount(es.getUnCorrectCount());
            statistics.setCorrectCount(es.getCorrectCount());
        }*/

        List<Long> userIdList = Collections.singletonList(statistics.getCreateBy());
        EmployeePageSearchDTO employeePageSearchDTO = new EmployeePageSearchDTO();
        employeePageSearchDTO.setUserIdList(userIdList);
        employeePageSearchDTO.setSize(userIdList.size());
        employeePageSearchDTO.setOrgId(SecurityContextHolder.getOrgId());
        R<PageResultComDTO<EmployeePageResultDTO>> remoteResult = remoteEmployeeService.page(employeePageSearchDTO);
        PageResultComDTO<EmployeePageResultDTO> remoteData = remoteResult.getRemoteData();
        if (remoteData != null) {
            List<EmployeePageResultDTO> records = remoteData.getRecords();
            if (CollectionUtils.isNotEmpty(records)) {
                statistics.setCreateByName(records.get(0).getRealName());
            }
        }

        if (statistics.getStatus().compareTo(ExamStatusEnum.DURING_EXAM.getStatus()) >= 0) {
            // 查询考试是否设置开启摄像头
            List<Long> examIdList = Collections.singletonList(statistics.getId());
            List<ExamRuleDTO> examRuleList = baseMapper.selectExamRuleByExamIdListAndCode(examIdList, RuleEnum.CAMERA_RECORDING.getCode());
            if (CollectionUtils.isNotEmpty(examRuleList)) {
                // 是否可以监考
                statistics.setCanInvigilate(true);
            }
        }

        return statistics;
    }

    @Override
    public void start(Long examId) {
        if (examId == null) {
            return;
        }

        String lockKey = RedisLockKeyUtil.getStartExamLockKey(examId);
        RLock rLock = redissonClient.getFairLock(lockKey);
        try {
            rLock.lock(10, TimeUnit.SECONDS);
            Exam exam = this.getById(examId);
            if (exam == null || ExamStatusEnum.NOT_START.getStatus() != exam.getStatus()) {
                return;
            }

            // 如果后面修改了考试开始时间，不会删除之前的定时任务，所以这里需要再次判断一下
            if (ChronoUnit.SECONDS.between(LocalDateTime.now(), exam.getStartTime()) <= START_JOB_ADVANCE_SECONDS) {
                this.lambdaUpdate().eq(Exam::getId, exam.getId())
                        .set(Exam::getStatus, ExamStatusEnum.DURING_EXAM.getStatus())
                        .update();
            }
        } finally {
            if (rLock != null && rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void end(Long examId) {
        log.info("考试结束({})....", examId);
        if (examId == null) {
            log.info("考试结束，examId为null");
            return;
        }

        Exam exam = this.getById(examId);
        if (exam == null ) {
            log.info("考试结束({})，查不到考试", examId);
            return;
        }
        if (ExamStatusEnum.DURING_EXAM.getStatus() != exam.getStatus()) {
            log.info("考试结束({})，考试状态异常-->{status: {}}", examId, exam.getStatus());
            return;
        }

        // 如果后面修改了考试结束时间，不会删除之前的定时任务，所以这里需要再次判断一下
        if (LocalDateTime.now().isBefore(exam.getEndTime())) {
            log.info("考试结束({})，考试时间异常-->{endTime: {}, now: {}}", examId, exam.getEndTime(), LocalDateTime.now());
            return;
        }

        this.lambdaUpdate().eq(Exam::getId, exam.getId())
                .set(Exam::getStatus, ExamStatusEnum.EXAM_ENDED.getStatus())
                .set(Exam::getEdited, Constants.DB_FAIL)
                .update();

        List<ExamineeRecord> examineeRecordList = this.examineeRecordMapper.selectUnsubmitListByExamId(examId);
        if (CollectionUtils.isNotEmpty(examineeRecordList)) {
            log.info("结束考试，自动交卷，examineeRecordList: {}", examineeRecordList);
            for (ExamineeRecord examineeRecord : examineeRecordList) {
                if (examineeRecord != null) {
                    log.info("考试结束，自动交卷 {}", JSON.toJSONString(examineeRecord));
                    this.submitPaper(examineeRecord);
                }
            }
        } else {
            log.info("考试结束({})，未交卷考试查询结果为空", examId);
        }
    }

    public void submitPaper(ExamineeRecord examineeRecord) {
        log.info("考试交卷: {}", JSON.toJSONString(examineeRecord));
        if (examineeRecord == null) {
            log.info("考试交卷，考试记录不存在");
            throw new ServiceException("考试记录不存在！");
        }
        Long examineeRecordId = examineeRecord.getId();
        if (examineeRecord.getSubmitTime() != null) {
            log.info("考试交卷({})，考试交卷试卷不为null", examineeRecordId);
//            throw new ServiceException("试卷已提交！");
            return;
        }

        examineeRecord.setSubmitTime(LocalDateTime.now());
        examineeRecord.setStatus(ExamineeRecordStatusEnum.TO_BE_CORRECTED.getStatus());
        LocalDateTime startTime = examineeRecord.getStartTime();
        if (startTime == null) {
            startTime = examineeRecord.getCreateTime();
            examineeRecord.setStartTime(startTime);
        }
        Long diffSeconds = ChronoUnit.SECONDS.between(startTime, examineeRecord.getSubmitTime());
        examineeRecord.setUseDuration(diffSeconds.intValue());
        String secondsFormat = DateFormatUtils.secondsFormat(diffSeconds);
        examineeRecord.setUseDurationFormat(secondsFormat);
        this.examineeRecordService.updateById(examineeRecord);

        log.info("考试交卷({})，发送交卷消息", examineeRecordId);
        tdmqProducerTemplate.sendAsyncStringMsg(Topics.SUBMIT_PAPER, String.valueOf(examineeRecordId));
    }

    @Override
    public ExamAndCertDetailResponse selectCertRuleByExamineeId(Long examineeId) {
        return baseMapper.selectCertRuleByExamineeId(examineeId);
    }

}

