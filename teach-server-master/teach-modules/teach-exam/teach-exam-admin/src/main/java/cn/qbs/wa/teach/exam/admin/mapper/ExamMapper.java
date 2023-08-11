package cn.qbs.wa.teach.exam.admin.mapper;

import cn.qbs.wa.teach.exam.admin.pojo.api.ApiExamineeeListRequest;
import cn.qbs.wa.teach.exam.admin.pojo.api.ApiExamineeeListResponse;
import cn.qbs.wa.teach.exam.admin.pojo.exam.*;
import cn.qbs.wa.teach.exam.common.pojo.ExamAndCertDetailResponse;
import cn.qbs.wa.teach.exam.common.entity.Exam;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 考试表(Exam)表数据库访问层
 *
 * @author zcm
 * @since 2021-12-14 11:40:51
 */
public interface ExamMapper extends BaseMapper<Exam> {

    @Deprecated
    IPage<ExamPageResponse> page(@Param("page") IPage<?> page, @Param("params") ExamPageRequest params);

    IPage<ExamPageResponseV2> pageV2(@Param("page") IPage<?> page, @Param("params") ExamPageRequestV2 params);

    IPage<ExamPageResponseV3> pageV3(@Param("page") IPage<?> page, @Param("params") ExamPageRequestV3 params);

    ExamDetailResponse selectDetailById(Serializable id);

    IPage<ExamUserRecordPageResponse> assignUserExamRecordPage(@Param("page") IPage<?> page, @Param("params") ExamUserRecordPageRequest params);

    IPage<ExamUserRecordPageResponse> assignUserExamRecordPageAllVisible(@Param("page") IPage<?> page, @Param("params") ExamUserRecordPageRequest params);

    ExamStatistics statistics(@Param("examId") Long examId);


    ExamPageResponse selectSubmitCount(@Param("params")ExamPageResponse record,@Param("flag")int flag);

    /**
     * 阅卷分页查询
     * @param page
     * @param params
     * @return
     */
    IPage<MarkingPageResponse> markingPage(@Param("page") IPage<?> page, @Param("params") MarkingPageRequest params);

    /**
     * 阅卷-考试记录分页查询
     * @param page
     * @param params
     * @return
     */
    IPage<MarkingExamRecordPageResponse> examRecordPage(@Param("page") IPage<?> page, @Param("params") MarkingExamRecordPageRequest params, @Param("studentIdList") List<Long> studentIdList);

    ExamStatistics countCorrect(@Param("examId") Long examId);

    List<ApiExamineeeListResponse> selectExamineeList(@Param("params") ApiExamineeeListRequest params);

    List<ExamRuleDTO> selectExamRuleByExamIdList(@Param("examIdList") List<Long> examIdList);

    /**
     * 查询超时未启动考试
     * @param status
     * @param size
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    List<Exam> selectTimeoutNotStarted(@Param("status") int status, @Param("size") int size);

    /**
     * 查询超时未结束考试
     * @param status
     * @param size
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    List<Exam> selectTimeoutNotEnded(@Param("status") int status, @Param("size") int size);

    ExamAndCertDetailResponse selectCertRuleByExamineeId(Long examineeId);

    List<ExamRuleDTO> selectExamRuleByExamIdListAndCode(@Param("examIdList") List<Long> examIdList, @Param("ruleCode") String ruleCode);

    List<StatisticsInExam> statisticsInExamByExamIdList(@Param("examIdList") List<Long> examIdList);

    List<StatisticsAfterExam> statisticsAfterExamByExamIdList(@Param("examIdList") List<Long> examIdList);

}

