package cn.qbs.wa.train.screen.controller;

import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.security.annotation.RequiresRoles;
import cn.qbs.wa.train.screen.pojo.screenattendclass.ScreenAttendClassPageRequest;
import cn.qbs.wa.train.screen.pojo.screenattendclass.ScreenAttendClassPageResponse;
import cn.qbs.wa.train.screen.pojo.screenbanner.ScreenBannerAddRequest;
import cn.qbs.wa.train.screen.pojo.screenbanner.ScreenBannerPageRequest;
import cn.qbs.wa.train.screen.pojo.screenbanner.ScreenBannerPageResponse;
import cn.qbs.wa.train.screen.pojo.screenbanner.ScreenBannerUpdateRequest;
import cn.qbs.wa.train.screen.pojo.screendataoverview.ScreenDataOverviewPageRequest;
import cn.qbs.wa.train.screen.pojo.screendataoverview.ScreenDataOverviewPageResponse;
import cn.qbs.wa.train.screen.pojo.screennotice.ScreenNoticePageRequest;
import cn.qbs.wa.train.screen.pojo.screennotice.ScreenNoticePageResponse;
import cn.qbs.wa.train.screen.pojo.screenplanorg.ScreenPlanOrgPageRequest;
import cn.qbs.wa.train.screen.pojo.screenplanorg.ScreenPlanOrgPageResponse;
import cn.qbs.wa.train.screen.pojo.screenstatdatadynamic.ScreenStatDataDynamicPageRequest;
import cn.qbs.wa.train.screen.pojo.screenstatdatadynamic.ScreenStatDataDynamicPageResponse;
import cn.qbs.wa.train.screen.pojo.screenstatstudentmonthly.ScreenStatStudentMonthlyPageRequest;
import cn.qbs.wa.train.screen.pojo.screenstatstudentmonthly.ScreenStatStudentMonthlyPageResponse;
import cn.qbs.wa.train.screen.service.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "大屏数据")
@RestController
@RequestMapping("/manage")
public class ScreenManageController {

    @Autowired
    private ScreenAttendClassService screenAttendClassService;

    @Autowired
    private ScreenStatStudentMonthlyService screenStatStudentMonthlyService;

    @Autowired
    private ScreenStatDataDynamicService screenStatDataDynamicService;

    @Autowired
    private ScreenPlanOrgService screenPlanOrgService;

    @Autowired
    private ScreenNoticeService screenNoticeService;

    @Autowired
    private ScreenDataOverviewService screenDataOverviewService;

    @Autowired
    private ScreenBannerService screenBannerService;

    @PostMapping("pageAttendClass")
    @ApiOperation("分页查询今日培训班级")
    public R<IPage<ScreenAttendClassPageResponse>> pageScreenAttendClass(@RequestBody ScreenAttendClassPageRequest params) {
        return R.ok(this.screenAttendClassService.page(params));
    }

    @PostMapping("pageStatStudentMonthly")
    @ApiOperation("分页查询每月学员数量统计")
    public R<IPage<ScreenStatStudentMonthlyPageResponse>> pageScreenStatStudentMonthly(@RequestBody ScreenStatStudentMonthlyPageRequest params) {
        return R.ok(this.screenStatStudentMonthlyService.page(params));
    }

    @PostMapping("pageStatDataDynamic")
    @ApiOperation("分页查询动态数据统计")
    public R<IPage<ScreenStatDataDynamicPageResponse>> pageScreenStatDataDynamic(@RequestBody ScreenStatDataDynamicPageRequest params) {
        return R.ok(this.screenStatDataDynamicService.page(params));
    }

    @PostMapping("pagePlanOrg")
    @ApiOperation("分页查询万人计划机构")
    public R<IPage<ScreenPlanOrgPageResponse>> pageScreenPlanOrg(@RequestBody ScreenPlanOrgPageRequest params) {
        return R.ok(this.screenPlanOrgService.page(params));
    }

    @PostMapping("pageNotice")
    @ApiOperation("分页查询通知")
    public R<IPage<ScreenNoticePageResponse>> pageNotice(@RequestBody ScreenNoticePageRequest params) {
        return R.ok(this.screenNoticeService.page(params));
    }

    @PostMapping("pageDataOverview")
    @ApiOperation("分页查询数据总览")
    public R<IPage<ScreenDataOverviewPageResponse>> pageScreenDataOverview(@RequestBody ScreenDataOverviewPageRequest params) {
        return R.ok(this.screenDataOverviewService.page(params));
    }

    @PostMapping("pageBanner")
    @ApiOperation("分页查询宣传栏")
    public R<IPage<ScreenBannerPageResponse>> pageScreenDataOverview(@RequestBody ScreenBannerPageRequest params) {
        return R.ok(this.screenBannerService.page(params));
    }

    @PostMapping("addBanner")
    @ApiOperation("添加宣传栏")
    @RequiresRoles({"plat_manager"})
    public R<Boolean> addScreenDataOverview(@RequestBody @Validated ScreenBannerAddRequest params) {
        return R.ok(this.screenBannerService.add(params));
    }

    @PostMapping("updateBanner")
    @ApiOperation("修改宣传栏")
    @RequiresRoles({"plat_manager"})
    public R<Boolean> update(@RequestBody @Validated ScreenBannerUpdateRequest params) {
        return R.ok(this.screenBannerService.update(params));
    }

    @PostMapping("deleteBanner")
    @ApiOperation("删除宣传栏")
    @RequiresRoles({"plat_manager"})
    public R<Boolean> delete(@RequestBody IdListRequest request) {
        return R.ok(this.screenBannerService.deleteByIds(request.getIdList()));
    }
}
