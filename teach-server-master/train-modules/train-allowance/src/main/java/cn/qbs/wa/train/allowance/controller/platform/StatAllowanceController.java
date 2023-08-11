package cn.qbs.wa.train.allowance.controller.platform;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.security.annotation.Logical;
import cn.qbs.wa.teach.common.security.annotation.RequiresPermissions;
import cn.qbs.wa.teach.out.union.admin.api.DTO.UniMemberDTO;
import cn.qbs.wa.teach.out.union.admin.api.DTO.UniMemberTCourseStudentRequestDTO;
import cn.qbs.wa.teach.out.union.admin.api.DTO.UniOrgInfoResponseDTO;
import cn.qbs.wa.teach.out.union.admin.api.RemoteUnionMemberService;
import cn.qbs.wa.train.allowance.enums.FlowStatusEnum;
import cn.qbs.wa.train.allowance.pojo.stat.*;
import cn.qbs.wa.train.allowance.service.StatAllowanceService;
import cn.qbs.wa.train.allowance.service.impl.StatAllowanceRefreshService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author yjx
 */
@RestController
@RequestMapping("/stat/allowance")
@Api(tags = "资助统计功能")
public class StatAllowanceController {

    @Resource
    private StatAllowanceService statAllowanceService;

    @Resource
    private StatAllowanceRefreshService statAllowanceRefreshService;

    @Resource
    private RemoteUnionMemberService remoteUnionMemberService;

    @PostMapping("/org/summary")
    @ApiOperation("机构资助统计-汇总")
    @RequiresPermissions("Allowance:Statistic:Apply")
    public R<SummaryAllowanceResponse> orgAllowanceSummary(@RequestBody OrgAllowancePageRequest params) {
        return R.ok(this.statAllowanceService.orgAllowanceSummary(params));
    }

    @PostMapping("/org/page")
    @ApiOperation("机构资助统计-分页")
    @RequiresPermissions("Allowance:Statistic:Apply")
    public R<IPage<OrgAllowanceResponse>> orgAllowancePage(@RequestBody OrgAllowancePageRequest params) {
        return R.ok(this.statAllowanceService.orgAllowancePage(params));
    }

    @PostMapping("/org/sub-page")
    @ApiOperation("机构资助统计-子分页")
    @RequiresPermissions("Allowance:Statistic:Apply")
    public R<IPage<OrgAllowanceSubResponse>> orgAllowanceSubPage(@RequestBody OrgAllowancePageRequest params) {
        return R.ok(this.statAllowanceService.orgAllowanceSubPage(params));
    }

    @PostMapping("/org/list")
    @ApiOperation("资助统计-机构列表")
    @RequiresPermissions(value = {"Allowance:Statistic:Apply","Allowance:Statistic:Amount","Allowance:Statistic:Student"}, logical = Logical.OR)
    public R<List<UniOrgInfoResponseDTO>> orgList(@RequestBody StatAllowanceTypeRequest params) {
        return R.ok(this.statAllowanceService.orgList(params));
    }

    @PostMapping("/class/summary")
    @ApiOperation("课程资助统计-汇总")
    @RequiresPermissions("Allowance:Statistic:Amount")
    public R<SummaryAllowanceResponse> classAllowanceSummary(@RequestBody ClazzAllowancePageRequest params) {
        initQueryParam(params);
        return R.ok(this.statAllowanceService.classAllowanceSummary(params));
    }

    @PostMapping("/class/page")
    @ApiOperation("课程资助统计-分页")
    @RequiresPermissions("Allowance:Statistic:Amount")
    public R<IPage<ClazzAllowanceResponse>> classAllowancePage(@RequestBody ClazzAllowancePageRequest params) {
        initQueryParam(params);
        return R.ok(this.statAllowanceService.classAllowancePage(params));
    }

    @PostMapping("/class/sub-page")
    @ApiOperation("课程资助统计-子分页")
    @RequiresPermissions("Allowance:Statistic:Amount")
    public R<IPage<ClazzAllowanceSubResponse>> classAllowanceSubPage(@RequestBody ClazzAllowanceSubPageRequest params) {
        return R.ok(this.statAllowanceService.classAllowanceSubPage(params));
    }

    @PostMapping("/activity/summary")
    @ApiOperation("活动资助统计-汇总")
    @RequiresPermissions("Allowance:Statistic:Amount")
    public R<SummaryAllowanceResponse> activityAllowanceSummary(@RequestBody ClazzAllowancePageRequest params) {
        initQueryParam(params);
        return R.ok(this.statAllowanceService.activityAllowanceSummary(params));
    }

    @PostMapping("/activity/page")
    @ApiOperation("活动资助统计-分页")
    @RequiresPermissions("Allowance:Statistic:Amount")
    public R<IPage<ActivityAllowanceResponse>> activityAllowancePage(@RequestBody ClazzAllowancePageRequest params) {
        initQueryParam(params);
        return R.ok(this.statAllowanceService.activityAllowancePage(params));
    }

    @PostMapping("/student/summary")
    @ApiOperation("学员资助统计-汇总")
    @RequiresPermissions("Allowance:Statistic:Student")
    public R<SummaryAllowanceResponse> studentAllowanceSummary(@RequestBody StudentAllowancePageRequest params) {
        if (initStudentQueryParam(params)) {
            SummaryAllowanceResponse summaryAllowanceResponse = new SummaryAllowanceResponse();
            summaryAllowanceResponse.setTotalLessonNum(0);
            summaryAllowanceResponse.setTotalLessonNum(0);
            summaryAllowanceResponse.setTotalFee(BigDecimal.ZERO);
            return R.ok(summaryAllowanceResponse);
        }
        return R.ok(this.statAllowanceService.studentAllowanceSummary(params));
    }

    @PostMapping("/student/page")
    @ApiOperation("学员资助统计-分页")
    @RequiresPermissions("Allowance:Statistic:Student")
    public R<IPage<StudentAllowanceResponse>> studentAllowancePage(@RequestBody StudentAllowancePageRequest params) {
        if (initStudentQueryParam(params)) {
            return R.ok(params.createMpPage());
        }
        return R.ok(this.statAllowanceService.studentAllowancePage(params));
    }

    @PostMapping("/student/sub-page")
    @ApiOperation("学员资助统计-子分页")
    @RequiresPermissions("Allowance:Statistic:Student")
    public R<IPage<StudentAllowanceSubResponse>> studentAllowanceSubPage(@RequestBody StudentAllowancePageRequest params) {
        if (params.getMemberId() == null) {
            return R.ok(params.createMpPage());
        }
        initStudentQueryParam(params);
        return R.ok(this.statAllowanceService.studentAllowanceSubPage(params));
    }

    private boolean initStudentQueryParam(StudentAllowancePageRequest params) {
        if (StrUtil.isNotBlank(params.getStudentPhone())) {
            UniMemberTCourseStudentRequestDTO search = new UniMemberTCourseStudentRequestDTO();
            search.setPhone(params.getStudentPhone());
            R<List<UniMemberDTO>> r = remoteUnionMemberService.getByNameOrPhone(search);
            if (r.isOk() && CollUtil.isNotEmpty(r.getData())) {
                Long memberId = r.getData().get(0).getId();
                if (memberId == null) {
                    return true;
                }
                params.setMemberId(memberId);
            } else {
                return true;
            }
        }
        initQueryParam(params);
        return false;
    }

    private void initQueryParam(StatAllowancePageRequest params) {
        if (!ObjectUtil.isAllNotEmpty(params.getDateStart(), params.getDateEnd())) {
            // 默认加载最近1个月的数据
            LocalDate now = LocalDate.now();
            params.setDateStart(now.minusDays(29));
            params.setDateEnd(now);
        }
        if (params.getFlowStatus() == null) {
            params.setFlowStatusList(Arrays.asList(FlowStatusEnum.ING.getCode(), FlowStatusEnum.PASS.getCode()));
        } else {
            params.setFlowStatusList(Collections.singletonList(params.getFlowStatus()));
        }
    }

    @PostMapping("/yearly/fee-chart")
    @ApiOperation("年度资助统计-费用统计")
    @RequiresPermissions("Allowance:Statistic:Org")
    public R<FeeChartsResponse> yearlyFeeChart(@RequestBody StatAllowanceYearlyPageRequest request) {
        return R.ok(this.statAllowanceService.yearlyFeeChart(request));
    }

    @PostMapping("/yearly/study-chart")
    @ApiOperation("年度资助统计-课时人次统计")
    @RequiresPermissions("Allowance:Statistic:Org")
    public R<StudyChartsResponse> yearlyStudyChart(@RequestBody StatAllowanceYearlyPageRequest request) {
        return R.ok(this.statAllowanceService.yearlyStudyChart(request));
    }

    @PostMapping("/yearly/pie/fee-chart")
    @ApiOperation("年度资助统计-饼图-费用统计")
    @RequiresPermissions("Allowance:Statistic:Org")
    public R<FeeResponse> pieFeeChart(@RequestBody YearlyPieRequest yearlyPieRequest) {
        return R.ok(this.statAllowanceService.pieFeeChart(yearlyPieRequest));
    }

    /*@PostMapping("/yearly/pie/study-chart")
    @ApiOperation("年度资助统计-饼图-课时人次统计")
    @RequiresPermissions("Allowance:Statistic:Apply")
    public R<StudyResponse> pieStudyChart(@RequestBody YearlyPieRequest yearlyPieRequest) {
        return R.ok(this.statAllowanceService.pieStudyChart(yearlyPieRequest));
    }*/

    @PostMapping("/yearly/list")
    @ApiOperation("年度资助统计-列表")
    @RequiresPermissions("Allowance:Statistic:Org")
    public R<IPage<StatAllowanceYearlyResponse>> yearlyList(@RequestBody StatAllowanceYearlyPageRequest request) {
        return R.ok(this.statAllowanceService.yearlyList(request));
    }

    @PostMapping("/refresh")
    @ApiOperation("手动刷新统计报表数据")
    @RequiresPermissions("Allowance:Statistic:Org")
    public R<Boolean> refresh() {
        return R.ok(this.statAllowanceRefreshService.refreshAll(), "刷新成功");
    }
}
