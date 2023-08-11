package cn.qbs.wa.train.allowance.mapper;

import cn.qbs.wa.train.allowance.pojo.stat.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StatAllowanceMapper {

    SummaryAllowanceResponse orgAllowanceSummaryCourse(@Param("params") OrgAllowancePageRequest params);

    SummaryAllowanceResponse orgAllowanceSummaryCourse2(@Param("params") OrgAllowancePageRequest params);

    List<AllowanceFeeResponse> orgAllowanceSummaryCourseFee(@Param("params") OrgAllowancePageRequest params);

    SummaryAllowanceResponse orgAllowanceSummaryActivity(@Param("params") OrgAllowancePageRequest params);

    IPage<OrgAllowanceResponse> pageActivity(@Param("page") IPage<?> page,@Param("params") OrgAllowancePageRequest params);

    IPage<OrgAllowanceResponse> pageCourse(@Param("page") IPage<?> page,@Param("params") OrgAllowancePageRequest params);

    IPage<OrgAllowanceResponse> pageCourse2(@Param("page") IPage<?> page,@Param("params") OrgAllowancePageRequest params);

    IPage<OrgAllowanceResponse> pageActivityAndCourse(@Param("page") IPage<?> page,@Param("params") OrgAllowancePageRequest params);

    List<AllowanceFeeResponse> pageCourseFee(@Param("params") OrgAllowancePageRequest params);

    IPage<OrgAllowanceSubResponse> pageSubActivity(@Param("page") IPage<?> page, @Param("params") OrgAllowancePageRequest params);

    IPage<OrgAllowanceSubResponse> pageSubCourse(@Param("page") IPage<?> page, @Param("params") OrgAllowancePageRequest params);

    List<AllowanceFeeResponse> pageSubCourseFee(@Param("params") OrgAllowancePageRequest params);

    SummaryAllowanceResponse studentAllowanceSummary(@Param("params") StudentAllowancePageRequest params);

    long studentAllowancePageCount(@Param("params") StudentAllowancePageRequest params);

    IPage<StudentAllowanceResponse> studentAllowancePage(IPage<?> page, @Param("params") StudentAllowancePageRequest params);

    IPage<StudentAllowanceSubResponse> studentAllowanceSubPage(IPage<?> page, @Param("params") StudentAllowancePageRequest params);

    List<Long> listAllowanceOrg();
    
    List<Long> listActivityAllowanceOrg();

    List<Long> listCourseAllowanceOrg();

    SummaryAllowanceResponse classAllowanceSummary(@Param("params") ClazzAllowancePageRequest params);

    SummaryStudentResponse classStudentSummary(@Param("params") ClazzAllowancePageRequest params);

    long classAllowancePageCount(@Param("params") ClazzAllowancePageRequest params);

    IPage<ClazzAllowanceResponse> classAllowancePage(IPage<?> page, @Param("params") ClazzAllowancePageRequest params);

    List<ClazzAllowanceStuResponse> studentLessonSummaryByIds(@Param("allowanceIds") List<Long> allowanceIds);

    IPage<ClazzAllowanceSubResponse> classAllowanceSubPage(IPage<?> page, @Param("applyAllowanceId") Long applyAllowanceId);

    SummaryAllowanceResponse activityAllowanceSummary(@Param("params") ClazzAllowancePageRequest params);

    IPage<ActivityAllowanceResponse> activityAllowancePage(IPage<?> page, @Param("params") ClazzAllowancePageRequest params);

}
