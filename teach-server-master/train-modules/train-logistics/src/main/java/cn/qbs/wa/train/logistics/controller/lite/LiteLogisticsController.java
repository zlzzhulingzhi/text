package cn.qbs.wa.train.logistics.controller.lite;

import cn.hutool.core.util.ObjectUtil;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.core.utils.AesUtil;
import cn.qbs.wa.train.logistics.entity.Classroom;
import cn.qbs.wa.train.logistics.entity.MatterReport;
import cn.qbs.wa.train.logistics.entity.Organization;
import cn.qbs.wa.train.logistics.pojo.classroom.LiteClassroomPageRequest;
import cn.qbs.wa.train.logistics.pojo.classroom.LiteClassroomResponse;
import cn.qbs.wa.train.logistics.pojo.classroomschedule.LiteClassroomScheduleRequest;
import cn.qbs.wa.train.logistics.pojo.classroomschedule.LiteClassroomScheduleResponse;
import cn.qbs.wa.train.logistics.pojo.clazz.LiteClazzPageRequest;
import cn.qbs.wa.train.logistics.pojo.clazz.LiteClazzResponse;
import cn.qbs.wa.train.logistics.pojo.dormitory.LiteDormitoryPageRequest;
import cn.qbs.wa.train.logistics.pojo.dormitory.LiteDormitoryResponse;
import cn.qbs.wa.train.logistics.pojo.dormitory.LiteDormitoryStatRequest;
import cn.qbs.wa.train.logistics.pojo.dormitory.LiteDormitoryStatResponse;
import cn.qbs.wa.train.logistics.pojo.dormitoryschedule.LiteDormitoryScheduleRequest;
import cn.qbs.wa.train.logistics.pojo.dormitoryschedule.LiteDormitoryScheduleResponse;
import cn.qbs.wa.train.logistics.pojo.matter.LiteMatterReportPageRequest;
import cn.qbs.wa.train.logistics.pojo.matter.LiteMatterReportResponse;
import cn.qbs.wa.train.logistics.pojo.matter.MatterReportSaveRequest;
import cn.qbs.wa.train.logistics.pojo.organization.LiteOrganizationListResponse;
import cn.qbs.wa.train.logistics.service.MatterReportService;
import cn.qbs.wa.train.logistics.service.lite.LiteLogisticsService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * @author yjx
 */
@RestController
@RequestMapping("/lite")
@Api(tags = "小程序后台功能接口")
public class LiteLogisticsController {

    @Resource
    private MatterReportService matterReportService;

    @Resource
    private LiteLogisticsService liteLogisticsService;

    @PostMapping("/org/matter-report")
    @ApiOperation("机构-报事报修-新增")
    public R<MatterReport> saveMatterReport(@RequestBody MatterReportSaveRequest params) {
        params.setContactNumber(AesUtil.encode(params.getContactNumber()));
        return R.ok(matterReportService.save(params));
    }

    @PostMapping("/org/matter-report/detail")
    @ApiOperation("机构-报事报修-详情")
    public R<LiteMatterReportResponse> detailOrgMatterReport(@RequestBody @Validated IdRequest params) {
        LiteMatterReportResponse detail = matterReportService.detail(params.getId());
        if (detail.getOrgId() != null && !detail.getOrgId().equals(SecurityContextHolder.getOrgId())) {
            return R.ok(new LiteMatterReportResponse());
        }
        return R.ok(detail);
    }

    @PostMapping("/org/matter-report/page")
    @ApiOperation("机构-报事报修-分页")
    public R<IPage<LiteMatterReportResponse>> pageOrgMatterReport(@RequestBody LiteMatterReportPageRequest params) {
        params.setOrgId(SecurityContextHolder.getOrgId());
        return R.ok(liteLogisticsService.pageMatterReport(params));
    }

    @PostMapping("/matter-report/classroom/page")
    @ApiOperation("报事报修-教室-分页")
    public R<IPage<LiteClassroomResponse>> pageClassroom(@RequestBody LiteClassroomPageRequest params) {
        return R.ok(liteLogisticsService.pageClassroom(params));
    }

    @PostMapping("/matter-report/classroom/list")
    @ApiOperation("报事报修-教室列表")
    public R<List<Classroom>> listMatterReportClassroom() {
        Long orgId = SecurityContextHolder.getOrgId();
        if (orgId == 0L) {
            orgId = null;
        }
        return R.ok(liteLogisticsService.listMatterReportClassroom(orgId));
    }

    @PostMapping("/plat/matter-report/org/list")
    @ApiOperation("平台-报事报修-机构列表")
    public R<List<Organization>> listMatterReportOrg() {
        if (SecurityContextHolder.getOrgId() != 0L) {
            return R.ok(Collections.emptyList());
        }
        return R.ok(liteLogisticsService.listMatterReportOrg());
    }

    @PostMapping("/plat/matter-report/page")
    @ApiOperation("平台-报事报修-分页")
    public R<IPage<LiteMatterReportResponse>> pagePlatMatterReport(@RequestBody LiteMatterReportPageRequest params) {
        if (SecurityContextHolder.getOrgId() != 0L) {
            return R.ok(params.createMpPage());
        }
        return R.ok(liteLogisticsService.pageMatterReport(params));
    }

    @PostMapping("/plat/matter-report/detail")
    @ApiOperation("平台-报事报修-详情")
    public R<LiteMatterReportResponse> detailPlatMatterReport(@RequestBody @Validated IdRequest params) {
        if (SecurityContextHolder.getOrgId() != 0L) {
            return R.ok();
        }
        return R.ok(matterReportService.detail(params.getId()));
    }

    @PostMapping("/plat/classroom/page")
    @ApiOperation("平台-教室-导览")
    public R<IPage<LiteClassroomResponse>> pageClassroomWithDate(@RequestBody LiteClassroomPageRequest params) {
        if (params.getUseDate() == null || params.getUseDate().isBefore(LocalDate.now())) {
            return R.ok(params.createMpPage());
        }
        return R.ok(liteLogisticsService.pageClassroomWithDate(params));
    }

    @PostMapping("/plat/classroom/schedule")
    @ApiOperation("平台-教室-预定列表")
    public R<List<LiteClassroomScheduleResponse>> listClassroomSchedule(@RequestBody @Validated LiteClassroomScheduleRequest params) {
        LocalDate dateStart = params.getDateStart();
        if (dateStart == null || dateStart.isBefore(LocalDate.now())) {
            return R.ok(Collections.emptyList());
        }
        // 不能超过一个月
        if (params.getDateEnd() == null || params.getDateEnd().minusMonths(1).isAfter(dateStart)) {
            params.setDateEnd(dateStart.plusMonths(1));
        }
        params.setDateStart(dateStart.plusDays(1));
        return R.ok(liteLogisticsService.listClassroomSchedule(params));
    }

    @PostMapping("/plat/dormitory/page")
    @ApiOperation("平台-宿舍-导览")
    public R<IPage<LiteDormitoryResponse>> pageDormitory(@RequestBody LiteDormitoryPageRequest params) {
        if (params.getUseDate() == null || params.getUseDate().isBefore(LocalDate.now())) {
            return R.ok(params.createMpPage());
        }
        return R.ok(liteLogisticsService.pageDormitory(params));
    }

    @PostMapping("/plat/dormitory/schedule")
    @ApiOperation("平台-宿舍-预定列表")
    public R<List<LiteDormitoryScheduleResponse>> listDormitorySchedule(@RequestBody @Validated LiteDormitoryScheduleRequest params) {
        LocalDate dateStart = params.getDateStart();
        if (dateStart == null || dateStart.isBefore(LocalDate.now())) {
            return R.ok(Collections.emptyList());
        }
        // 不能超过一个月
        if (params.getDateEnd() == null || params.getDateEnd().minusMonths(1).isAfter(dateStart)) {
            params.setDateEnd(dateStart.plusMonths(1));
        }
        params.setDateStart(dateStart.plusDays(1));
        return R.ok(liteLogisticsService.listDormitorySchedule(params));
    }

    @PostMapping("/plat/dormitory/page/v2")
    @ApiOperation("平台-宿舍-导览-v2")
    public R<IPage<LiteDormitoryResponse>> pageDormitoryV2(@RequestBody LiteDormitoryPageRequest params) {
        if (params.getUseDate() == null) {
            return R.ok(params.createMpPage());
        }
        return R.ok(liteLogisticsService.pageDormitoryV2(params));
    }

    @PostMapping("/plat/dormitory/stat")
    @ApiOperation("平台-宿舍-入住统计")
    public R<LiteDormitoryStatResponse> statDormitoryInUse(@RequestBody LiteDormitoryStatRequest params) {
        if (params.getUseDate() == null) {
            return R.ok(new LiteDormitoryStatResponse());
        }
        return R.ok(liteLogisticsService.statDormitoryInUse(params));
    }

    @PostMapping("/plat/clazz/page")
    @ApiOperation("平台-开班信息-分页")
    public R<IPage<LiteClazzResponse>> pageClazz(@RequestBody LiteClazzPageRequest params) {
        if (!ObjectUtil.isAllNotEmpty(params.getStartDate(), params.getEndDate())) {
            // 默认加载最近1个月的数据
            LocalDate now = LocalDate.now();
            params.setStartDate(now.minusDays(29));
            params.setEndDate(now);
        }
        return R.ok(liteLogisticsService.pageClazz(params));
    }

    @PostMapping("org/list")
    @ApiOperation("教务机构列表")
    public R<List<LiteOrganizationListResponse>> listOrg() {
        return R.ok(this.liteLogisticsService.listOrg());
    }

}
