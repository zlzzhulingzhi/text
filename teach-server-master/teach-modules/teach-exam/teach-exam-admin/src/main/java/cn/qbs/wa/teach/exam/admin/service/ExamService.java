package cn.qbs.wa.teach.exam.admin.service;

import cn.qbs.wa.teach.common.core.domain.EnableRequest;
import cn.qbs.wa.teach.common.core.domain.PageResultComDTO;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.exam.admin.pojo.exam.*;
import cn.qbs.wa.teach.exam.common.entity.Exam;
import cn.qbs.wa.teach.exam.common.pojo.ExamAndCertDetailResponse;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentSearchDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;
import java.util.List;

/**
 * 考试表(Exam)表服务接口
 *
 * @author zcm
 * @since 2021-12-14 11:40:51
 */
public interface ExamService extends IService<Exam> {

    /**
     * 新增考试表
     * @param params
     * @return
     */
    Long add(ExamAddRequest params);

    /**
     * 检查考试结束时间或入场结束时间
     * */
    void checkExamTime(Long examId);

    /**
     * 分页查询考试表
     * @param params
     * @return
     */
    @Deprecated
    IPage<ExamPageResponse> page(ExamPageRequest params);

    /**
     * 分页查询考试
     * @param params
     * @return
     */
    IPage<ExamPageResponseV2> pageV2(ExamPageRequestV2 params);

    /**
     * 分页查询考试
     * @param params
     * @return
     */
    IPage<ExamPageResponseV3> pageV3(ExamPageRequestV3 params);

    /**
     * 分页查询指派用户的考试记录
     * @param params
     * @return
     */
    IPage<ExamUserRecordPageResponse> assignUserExamRecordPage(ExamUserRecordPageRequest params);

    PageResultComDTO<StudentDTO>  examAssignUserPage(ExamAssignUserPageRequest pageRequest);

    boolean assignUser(ExamAssignUserRequest pageRequest);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    ExamDetailResponse detail(Serializable id);

    /**
     * 更新考试表
     * @param params
     * @return
     */
    boolean update(ExamUpdateRequest params);

    boolean onShelf(List<Long> examIdList);

    void onShelf(Long examId);

    void afterOnShelf(Long examId);

    boolean offShelf(List<Long> examIdList);

    /**
     * 下架
     * @param examId
     * @param ignoreExamEndTime 是否忽略考试结束时间
     * @return
     */
    boolean offShelf(Long examId, boolean ignoreExamEndTime);

    boolean enable(EnableRequest request);

    ExamStatistics statistics(Long examId);

    void start(Long examId);

    void end(Long examId);

    ExamAndCertDetailResponse selectCertRuleByExamineeId(Long examineeId);

}

