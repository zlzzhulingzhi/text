package cn.qbs.wa.teach.exam.answer.service.impl;

import cn.qbs.wa.teach.common.core.utils.StringUtils;
import cn.qbs.wa.teach.common.security.utils.SecurityUtils;
import cn.qbs.wa.teach.exam.answer.mapper.ExamMapper;
import cn.qbs.wa.teach.exam.answer.mapper.ExamineeMapper;
import cn.qbs.wa.teach.exam.answer.mapper.ExamineeRecordMapper;
import cn.qbs.wa.teach.exam.answer.pojo.exam.ExamPageRequest;
import cn.qbs.wa.teach.exam.answer.pojo.exam.ExamPageResponse;
import cn.qbs.wa.teach.exam.answer.pojo.exam.ExamRecord;
import cn.qbs.wa.teach.exam.answer.service.ExamineeRecordService;
import cn.qbs.wa.teach.exam.answer.service.ExamineeService;
import cn.qbs.wa.teach.exam.common.entity.Exam;
import cn.qbs.wa.teach.exam.common.entity.Examinee;
import cn.qbs.wa.teach.exam.common.entity.ExamineeRecord;
import cn.qbs.wa.teach.exam.common.enumerate.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 考生表(Examinee)表服务实现类
 *
 * @author zcm
 * @since 2021-12-14 11:43:32
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExamineeServiceImpl extends ServiceImpl<ExamineeMapper, Examinee> implements ExamineeService {

    private final ExamineeMapper examineeMapper;

    private final ExamMapper examMapper;

    private final ExamineeRecordMapper examineeRecordMapper;

    private ExamineeRecordService examineeRecordService;

    @Autowired
    public void setExamineeRecordService(ExamineeRecordService examineeRecordService) {
        this.examineeRecordService = examineeRecordService;
    }

    @Override
    public Examinee getExaminee(Long examId, Long userId) {
        if (examId == null || userId == null) {
            return null;
        }
        return this.lambdaQuery()
                .eq(Examinee::getExamId, examId)
                .eq(Examinee::getUserId, userId)
                .last("limit 1").one();
    }

    @Override
    public IPage<ExamPageResponse> myTestPage(ExamPageRequest examPageRequest) {
        examPageRequest.setUserId(SecurityUtils.getLoginUser().getUserid());
        //查询所有的考试id
        List<ExamPageResponse> examPageResponseIPage = examineeMapper.selectAllIds(examPageRequest);
        /*if (StringUtils.isNull(examPageRequest.getComplete())) {
            getDetailed(examPageResponseIPage);
        }*/
        //判断考试是否完成
        if (examPageRequest.getComplete().toString().equals(CompleteEnum.NOT_START.getIndex())) {
            //未开始
            IPage<ExamPageResponse> examPageResponseIPage1 = examineeMapper.selectUnfinished(examPageRequest.createMpPage(), examPageResponseIPage, examPageRequest);
            if (StringUtils.isNotEmpty(examPageResponseIPage1.getRecords())) {
                String flag = PaperStatusEnum.HAVE_NOT_STARTED.getIndex();
                getDetailed(examPageResponseIPage1.getRecords(), flag);
            }
            return examPageResponseIPage1;
        } else if (examPageRequest.getComplete().toString().equals(CompleteEnum.DURING_EXAM.getIndex())) {
            //考试进行中,考试进行中的试卷状态有三种,未开始答题,答题中, 已提交
            IPage<ExamPageResponse> examPageResponseIPage2 = examineeMapper.selectUnfinished(examPageRequest.createMpPage(), examPageResponseIPage, examPageRequest);
            if (StringUtils.isNotEmpty(examPageResponseIPage2.getRecords())) {
                //查询未开始答题
                List<ExamPageResponse> examPageResponseList = examineeMapper.selectNotStart(examPageResponseIPage2.getRecords(), examPageRequest);
                if (StringUtils.isNotEmpty(examPageResponseList)) {
                    String flag = PaperStatusEnum.START_TEST.getIndex();
                    getDetailed(examPageResponseList, flag);
                    addPage(examPageResponseIPage2, examPageResponseList);
                }

                List<ExamPageResponse> examPageResponses = new ArrayList<>();
                List<ExamPageResponse> examPageResponses1 = new ArrayList<>();
                if (StringUtils.isNotEmpty(examPageResponseIPage2.getRecords())) {
                    for (ExamPageResponse record : examPageResponseIPage2.getRecords()) {
                        //查询已提交
                        ExamPageResponse examPageResponse1 = examineeMapper.selectAnswer(record, examPageRequest, PaperStatusEnum.START_TEST.getFlag());
                        if(StringUtils.isNotNull(examPageResponse1)){
                            examPageResponses.add(examPageResponse1);
                        }

                        //查询答题中
                        ExamPageResponse examPageResponse2 = examineeMapper.selectAnswer(record, examPageRequest, PaperStatusEnum.HAVE_NOT_STARTED.getFlag());
                        if(StringUtils.isNotNull(examPageResponse2)){
                            examPageResponses1.add(examPageResponse2);
                        }

                    }
                }
                if (StringUtils.isNotEmpty(examPageResponses)) {
                    String flag = PaperStatusEnum.RE_EXAMINATION.getIndex();
                    getDetailed(examPageResponses, flag);
                    addPage(examPageResponseIPage2, examPageResponses);
                }

                if (StringUtils.isNotEmpty(examPageResponses1) ) {
                    String flag = PaperStatusEnum.CONTINUE_TO_TEST.getIndex();
                    getDetailed(examPageResponses1, flag);
                    addPage(examPageResponseIPage2, examPageResponses1);
                }
            }
            //startTimeSort(examPageResponseIPage2);
            return examPageResponseIPage2;
        } else {
            //考试结束,结束后两种状态, 未参加考试,已交卷
            IPage<ExamPageResponse> examPageResponseIPage3 = examineeMapper.selectUnfinished(examPageRequest.createMpPage(), examPageResponseIPage, examPageRequest);
            if (StringUtils.isNotEmpty(examPageResponseIPage3.getRecords())) {
                //未参加考试
                List<ExamPageResponse> examPageResponseList = examineeMapper.selectNotStart(examPageResponseIPage3.getRecords(), examPageRequest);
                if (StringUtils.isNotEmpty(examPageResponseList)) {
                    String flag = PaperStatusEnum.EXAM_ENDED.getIndex();
                    getDetailed(examPageResponseList, flag);
                    addPage(examPageResponseIPage3, examPageResponseList);
                }

                //已提交试卷
                ArrayList<ExamPageResponse> examPageResponses = new ArrayList<>();
                if (StringUtils.isNotEmpty(examPageResponseIPage3.getRecords())) {
                    for (ExamPageResponse record : examPageResponseIPage3.getRecords()) {
                        ExamPageResponse examPageResponseList1 = examineeMapper.selectAnswer(record, examPageRequest, PaperStatusEnum.START_TEST.getFlag());
                        if(StringUtils.isNotNull(examPageResponseList1)) {
                            examPageResponses.add(examPageResponseList1);
                        }
                    }
                }

                if (StringUtils.isNotEmpty(examPageResponses)) {
                    String flag = PaperStatusEnum.NOT_TIME.getIndex();
                    getDetailed(examPageResponses, flag);
                    addPage(examPageResponseIPage3, examPageResponses);
                }
            }
            //startTimeSort(examPageResponseIPage3);
            return examPageResponseIPage3;
        }
        //return examPageResponseIPage;
    }

    private void startTimeSort(IPage<ExamPageResponse> examPageResponseIPage) {
        Collections.sort(examPageResponseIPage.getRecords(), new Comparator<ExamPageResponse>() {
            @Override
            public int compare(ExamPageResponse o1, ExamPageResponse o2) {
                int i = o1.getStartTime().compareTo(o2.getStartTime());
                if (i > 0) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
    }

    private void getDetailed(List<ExamPageResponse> examPageResponseList, String flag) {
        for (ExamPageResponse record : examPageResponseList) {
            //根据考试id查询考试的基本信息
            Exam exam = examMapper.selectById(record.getId());
            BeanUtils.copyProperties(exam, record);
            record.setUserId(SecurityUtils.getLoginUser().getUserid());
            //查询用户id
            Examinee examinee = examineeMapper.selectOne(new LambdaQueryWrapper<Examinee>().eq(Examinee::getExamId, record.getId()).eq(Examinee::getUserId, record.getUserId()));
            if (StringUtils.isNotNull(examinee)) {
                record.setExamineeId(examinee.getId());
            }
            //通过考试id查询参加考试的人数
            Integer count = examineeMapper.selectJoinTest(record.getId());
            record.setCount(count);
            //0:时间未到 , 1:开始考试, 2:继续考试 , 3:重新考试 , 4:考试结束
            switch (flag) {
                case "0":
                    record.setPaperStatus(PaperStatusEnum.HAVE_NOT_STARTED.getFlag());
                    break;
                case "1":
                    record.setPaperStatus(PaperStatusEnum.START_TEST.getFlag());
                    break;
                case "2":
                    record.setPaperStatus(PaperStatusEnum.CONTINUE_TO_TEST.getFlag());
                    break;
                case "3":
                    //判断考试次数
                    //查询详情
                    List<ExamPageResponse> examPageResponseList1 = examineeMapper.selectParticulars(record);
                    getInfoIds(record, examPageResponseList1);
                    if (record.getRemainingTimes().toString().equals(PaperStatusEnum.HAVE_NOT_STARTED.getIndex())) {
                        record.setPaperStatus(PaperStatusEnum.NOT_TIME.getFlag());
                    } else {
                        record.setPaperStatus(PaperStatusEnum.RE_EXAMINATION.getFlag());
                    }
                    getScore(record, exam);
                    break;
                case "4":
                    record.setPaperStatus(PaperStatusEnum.EXAM_ENDED.getFlag());
                    break;
                default:
                    //查询详情
                    List<ExamPageResponse> examPageResponseList2 = examineeMapper.selectParticulars(record);
                    getInfoIds(record, examPageResponseList2);
                    record.setPaperStatus(PaperStatusEnum.EXAM_ENDED.getFlag());
                    getScore(record, exam);
            }
        }
    }

    private void getInfoIds(ExamPageResponse record, List<ExamPageResponse> examPageResponseList2) {
        for (ExamPageResponse examPageResponse : examPageResponseList2) {
            if (examPageResponse.getStatus().toString().equals(ExamineeRecordStatusEnum.CORRECTED.getValue())) {
                record.setExamineeRecordId(examPageResponse.getExamineeRecordId());
                record.setRemainingTimes(examPageResponse.getRemainingTimes());
            } else {
                record.setExamineeRecordId(examPageResponseList2.get(0).getExamineeRecordId());
                record.setRemainingTimes(examPageResponseList2.get(0).getRemainingTimes());
            }
        }
    }

    private void getScore(ExamPageResponse record, Exam exam) {
        //查询有效分数
        ExamPageResponse examPageResponseList1 = examineeMapper.selectComplete(record);
        //已完成考试的
        if (StringUtils.isNotNull(examPageResponseList1) && StringUtils.isNotNull(examPageResponseList1.getScore())) {
            if (exam.getPassScore().compareTo(examPageResponseList1.getScore()) < OthersEnum.CALCULATED.getIndex()) {
                //通过
                record.setPass(ScoreEnum.PASS.getIndex());
            } else {
                record.setPass(ScoreEnum.NOT_PASS.getIndex());
            }
            record.setScore(examPageResponseList1.getScore());
        } else {
            record.setPass(ScoreEnum.NOT_PASS.getIndex());
        }

    }

    private void addPage(IPage<ExamPageResponse> examPageResponseIPage, List<ExamPageResponse> examPageResponseList) {
        for (ExamPageResponse examPageResponse : examPageResponseList) {
            for (ExamPageResponse record : examPageResponseIPage.getRecords()) {
                if (record.getId().toString().equals(examPageResponse.getId().toString())) {
                    BeanUtils.copyProperties(examPageResponse, record);
                    break;
                }
            }
        }
    }

    @Override
    public ExamRecord viewLog(ExamPageRequest examPageRequest) {
        examPageRequest.setUserId(SecurityUtils.getLoginUser().getUserid());
        List<ExamRecord> examPageResponses = examineeRecordMapper.selectInfoByExamId(examPageRequest);
        //List<ExamineeRecord> examineeRecords = examineeRecordMapper.selectList(new LambdaQueryWrapper<ExamineeRecord>().eq(ExamineeRecord::getId, examPageRequest.getExamineeRecordId()));
        ExamRecord examRecord = new ExamRecord();
        if (StringUtils.isNotEmpty(examPageResponses)) {
            ArrayList<ExamRecord> list = new ArrayList<>();
            for (ExamRecord examineeRecord : examPageResponses) {
                ExamRecord examRecord1 = new ExamRecord();
                BeanUtils.copyProperties(examineeRecord, examRecord1);
                //获取最高分
                if (examineeRecord.getCalculated().toString().equals(OthersEnum.CALCULATED.getFlag())) {
                    examRecord.setTopScore(examineeRecord.getScore());
                }
                list.add(examRecord1);
            }
            examRecord.setRemainingTimes(examPageResponses.get(0).getRemainingTimes());
            examRecord.setLimitCount(examPageResponses.size());
            examRecord.setExamRecords(list);
        }

        return examRecord;
    }

    @Override
    public ExamineeRecord cheat(ExamPageRequest examPageRequest) {
        return examineeRecordMapper.selectById(examPageRequest.getExamineeRecordId());
    }

    /**
     * 更新考生考试剩余次数
     *
     * @param examineeId
     * @param examLimitCount
     */
    @Override
    public void updateRemainingTimes(Long examineeId, Integer examLimitCount) {
        Long count = this.examineeRecordService.lambdaQuery().eq(ExamineeRecord::getExamineeId, examineeId).count();
        if (count == null) {
            count = 0L;
        }
        int remainingTimes = examLimitCount - count.intValue();
        this.lambdaUpdate().eq(Examinee::getId, examineeId).set(Examinee::getRemainingTimes, remainingTimes).update();
    }

    @Override
    public void decrRemainingTimes(Long examineeId) {
        this.baseMapper.decrRemainingTimes(examineeId);
    }
}

