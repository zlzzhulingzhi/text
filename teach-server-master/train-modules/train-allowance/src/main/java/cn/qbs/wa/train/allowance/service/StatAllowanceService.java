package cn.qbs.wa.train.allowance.service;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.out.union.admin.api.DTO.UniOrgInfoResponseDTO;
import cn.qbs.wa.train.allowance.pojo.stat.*;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

public interface StatAllowanceService {

    /**
     * 机构资助统计-汇总
     * @param params
     */
    SummaryAllowanceResponse orgAllowanceSummary(OrgAllowancePageRequest params);

    /**
     * 机构资助统计-主分页
     * @param params
     */
    IPage<OrgAllowanceResponse> orgAllowancePage(OrgAllowancePageRequest params);

    /**
     * 机构资助统计-子分页
     * @param params
     */
    IPage<OrgAllowanceSubResponse> orgAllowanceSubPage(OrgAllowancePageRequest params);

    FeeChartsResponse yearlyFeeChart(StatAllowanceYearlyPageRequest request);

    StudyChartsResponse yearlyStudyChart(StatAllowanceYearlyPageRequest request);

    FeeResponse pieFeeChart(YearlyPieRequest yearlyPieRequest);

    StudyResponse pieStudyChart(YearlyPieRequest yearlyPieRequest);

    IPage<StatAllowanceYearlyResponse> yearlyList(StatAllowanceYearlyPageRequest request);

    SummaryAllowanceResponse classAllowanceSummary(ClazzAllowancePageRequest params);

    IPage<ClazzAllowanceResponse> classAllowancePage(ClazzAllowancePageRequest params);

    IPage<ClazzAllowanceSubResponse> classAllowanceSubPage(ClazzAllowanceSubPageRequest params);

    SummaryAllowanceResponse activityAllowanceSummary(ClazzAllowancePageRequest params);

    IPage<ActivityAllowanceResponse> activityAllowancePage(ClazzAllowancePageRequest params);

    SummaryAllowanceResponse studentAllowanceSummary(StudentAllowancePageRequest params);

    IPage<StudentAllowanceResponse> studentAllowancePage(StudentAllowancePageRequest params);

    IPage<StudentAllowanceSubResponse> studentAllowanceSubPage(StudentAllowancePageRequest params);

    List<UniOrgInfoResponseDTO> orgList(StatAllowanceTypeRequest params);
}
