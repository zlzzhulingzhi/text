package cn.qbs.wa.train.screen.controller;

import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.security.annotation.RequiresRoles;
import cn.qbs.wa.train.screen.entity.*;
import cn.qbs.wa.train.screen.pojo.imports.*;
import cn.qbs.wa.train.screen.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "导入")
@RestController
@RequestMapping("/import")
public class ScreenImportController {

    @Resource
    private ScreenImportService screenImportService;

    @Resource
    private ScreenNoticeService screenNoticeService;

    @Resource
    private ScreenAttendClassService screenAttendClassService;

    @Resource
    private ScreenStatDataDynamicService screenStatDataDynamicService;

    @Resource
    private ScreenStatStudentMonthlyService screenStatStudentMonthlyService;

    @Resource
    private ScreenDataOverviewService screenDataOverviewService;

    @ApiOperation(value = "预导入通知信息")
    @PostMapping("previewNotice")
    @RequiresRoles({"plat_manager"})
    public R importPreNotice(MultipartFile file){
        return R.ok(screenImportService.importPreNotice(file));
    }

    @ApiOperation(value = "预导入今日培训班级信息")
    @PostMapping("previewAttendClass")
    @RequiresRoles({"plat_manager"})
    public R importPreAttendClass(MultipartFile file){
        return R.ok(screenImportService.importPreAttendClass(file));
    }

    @ApiOperation(value = "预导入动态数据统计信息")
    @PostMapping("previewStatDataDynamic")
    @RequiresRoles({"plat_manager"})
    public R importPreStatDataDynamic(MultipartFile file){
        return R.ok(screenImportService.importPreStatDataDynamic(file));
    }

    @ApiOperation(value = "预导入每月学员数量统计信息")
    @PostMapping("previewStatStudentMonthly")
    @RequiresRoles({"plat_manager"})
    public R importPreStatStudentMonthly(MultipartFile file){
        return R.ok(screenImportService.importPreStatStudentMonthly(file));
    }

    @ApiOperation(value = "预导入数据总览信息")
    @PostMapping("previewDataOverview")
    @RequiresRoles({"plat_manager"})
    public R importPreDataOverview(MultipartFile file){
        return R.ok(screenImportService.importPreDataOverview(file));
    }

    @ApiOperation(value = "批量插入通知信息")
    @PostMapping("importBatchNotice")
    @RequiresRoles({"plat_manager"})
    public R importBatchNotice(@RequestBody ScreenNoticeImportsRequest request){
        List<ScreenNotice> screenNoticeList=screenNoticeService.list(new QueryWrapper<>());
        if(!screenNoticeList.isEmpty()){
            List<Long> ids=new ArrayList<>();
            for (ScreenNotice screenNotice:screenNoticeList) {
                ids.add(screenNotice.getId());
            }
            //删除全表数据
            screenNoticeService.removeByIds(ids);
        }
        List<ScreenNotice> screenNotices=new ArrayList<>();
        for (ScreenNoticeDataParseResult screenNoticeDataParseResult:request.getScreenNoticeDataParseResultList()) {
            ScreenNotice screenNotice=new ScreenNotice();
            LocalDate publishDate = LocalDate.parse(screenNoticeDataParseResult.getPublishDate(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            BeanUtils.copyProperties(screenNoticeDataParseResult, screenNotice);
            screenNotice.setPublishDate(publishDate);
            screenNotices.add(screenNotice);
        }
        return R.ok(screenNoticeService.saveBatch(screenNotices));
    }

    @ApiOperation(value = "批量插入今日培训班级信息")
    @PostMapping("importBatchAttendClass")
    @RequiresRoles({"plat_manager"})
    public R importBatchAttendClass(@RequestBody ScreenAttendClassImportsRequest request){
        List<ScreenAttendClass> screenAttendClassList=screenAttendClassService.list(new QueryWrapper<>());
        if(!screenAttendClassList.isEmpty()){
            List<Long> ids=new ArrayList<>();
            for (ScreenAttendClass screenAttendClass:screenAttendClassList) {
                ids.add(screenAttendClass.getId());
            }
            //删除全表数据
            screenAttendClassService.removeByIds(ids);
        }
        List<ScreenAttendClass> screenAttendClasses=new ArrayList<>();
        for (ScreenAttendClassDataParseResult screenAttendClassDataParseResult:request.getScreenAttendClassDataParseResultList()) {
            ScreenAttendClass screenAttendClass=new ScreenAttendClass();
            BeanUtils.copyProperties(screenAttendClassDataParseResult, screenAttendClass);
            screenAttendClasses.add(screenAttendClass);
        }
        return R.ok(screenAttendClassService.saveBatch(screenAttendClasses));
    }

    @ApiOperation(value = "批量插入动态数据统计信息")
    @PostMapping("importBatchStatDataDynamic")
    @RequiresRoles({"plat_manager"})
    public R importBatchStatDataDynamic(@RequestBody ScreenStatDataDynamicImportsRequest request){
        List<ScreenStatDataDynamic> screenStatDataDynamicList=screenStatDataDynamicService.lambdaQuery().eq(ScreenStatDataDynamic::getModule,request.getModule()).list();
        if(!screenStatDataDynamicList.isEmpty()){
            List<Long> ids=new ArrayList<>();
            for (ScreenStatDataDynamic screenStatDataDynamic:screenStatDataDynamicList) {
                ids.add(screenStatDataDynamic.getId());
            }
            //删除全表数据
            screenStatDataDynamicService.removeByIds(ids);
        }
        List<ScreenStatDataDynamic> screenStatDataDynamics=new ArrayList<>();
        for (ScreenStatDataDynamicDataParseResult screenStatDataDynamicDataParseResult:request.getScreenStatDataDynamicDataParseResultList()) {
            ScreenStatDataDynamic screenStatDataDynamic=new ScreenStatDataDynamic();
            BeanUtils.copyProperties(screenStatDataDynamicDataParseResult, screenStatDataDynamic);
            screenStatDataDynamic.setModule(request.getModule());
            screenStatDataDynamics.add(screenStatDataDynamic);
            /*//查询是否有同显示名称，没有添加数据，有更新数据
            List<ScreenStatDataDynamic> screenStatDataDynamicList=
                    screenStatDataDynamicService.lambdaQuery().eq(ScreenStatDataDynamic::getDataName,screenStatDataDynamic.getDataName()).list();
            if(screenStatDataDynamicList.isEmpty()){
                screenStatDataDynamicService.save(screenStatDataDynamic);
            }else {
                screenStatDataDynamic.setId(screenStatDataDynamicList.get(Constants.DB_FAIL).getId());
                if(!screenStatDataDynamicList.get(Constants.DB_FAIL).getFreeNum().equals(screenStatDataDynamic.getFreeNum()) ||
                        !screenStatDataDynamicList.get(Constants.DB_FAIL).getUsingNum().equals(screenStatDataDynamic.getUsingNum())){
                    screenStatDataDynamicService.updateById(screenStatDataDynamic);
                }

            }*/
        }
        return R.ok(screenStatDataDynamicService.saveBatch(screenStatDataDynamics));
    }

    @ApiOperation(value = "批量插入每月学员数量统计信息")
    @PostMapping("importBatchStatStudentMonthly")
    @RequiresRoles({"plat_manager"})
    public R importBatchStatStudentMonthly(@RequestBody ScreenStatStudentMonthlyImportsRequest request){
        List<ScreenStatStudentMonthly> screenStatStudentMonthlyList=screenStatStudentMonthlyService.list(new QueryWrapper<>());
        if(!screenStatStudentMonthlyList.isEmpty()){
            List<Long> ids=new ArrayList<>();
            for (ScreenStatStudentMonthly screenStatStudentMonthly:screenStatStudentMonthlyList) {
                ids.add(screenStatStudentMonthly.getId());
            }
            //删除全表数据
            screenStatStudentMonthlyService.removeByIds(ids);
        }
        List<ScreenStatStudentMonthly> screenStatStudentMonthlys=new ArrayList<>();
        for (ScreenStatStudentMonthlyDataParseResult screenStatStudentMonthlyDataParseResult:request.getScreenStatStudentMonthlyDataParseResultList()) {
            ScreenStatStudentMonthly screenStatStudentMonthly=new ScreenStatStudentMonthly();
            BeanUtils.copyProperties(screenStatStudentMonthlyDataParseResult, screenStatStudentMonthly);
            screenStatStudentMonthly.setYear(Integer.valueOf(screenStatStudentMonthlyDataParseResult.getYear()));
            screenStatStudentMonthlys.add(screenStatStudentMonthly);
            /*List<ScreenStatStudentMonthly> screenStatStudentMonthlyList=
                    screenStatStudentMonthlyService.lambdaQuery().eq(ScreenStatStudentMonthly::getYear,screenStatStudentMonthly.getYear()).
                            eq(ScreenStatStudentMonthly::getMonth,screenStatStudentMonthly.getMonth()).list();
            if(screenStatStudentMonthlyList.isEmpty()){
                screenStatStudentMonthlys.add(screenStatStudentMonthly);
            }else {
                screenStatStudentMonthly.setId(screenStatStudentMonthlyList.get(Constants.DB_FAIL).getId());
                screenStatStudentMonthlyService.updateById(screenStatStudentMonthly);
            }*/

        }
        return R.ok(screenStatStudentMonthlyService.saveBatch(screenStatStudentMonthlys));
    }

    @ApiOperation(value = "批量插入数据总览信息")
    @PostMapping("importBatchDataOverview")
    @RequiresRoles({"plat_manager"})
    public R importBatchDataOverview(@RequestBody ScreenDataOverviewImportsRequest request) {
        List<ScreenDataOverview> screenDataOverviews = new ArrayList<>();
        for (ScreenDataOverviewDataParseResult screenDataOverviewDataParseResult : request.getScreenDataOverviewDataParseResultList()) {
            ScreenDataOverview screenDataOverview = new ScreenDataOverview();
            BeanUtils.copyProperties(screenDataOverviewDataParseResult, screenDataOverview);
            screenDataOverviews.add(screenDataOverview);
        }
        if (screenDataOverviews.isEmpty()) {
            return R.fail("导入数据为空!");
        }
        List<ScreenDataOverview> screenDataOverviewList = screenDataOverviewService.list();
        if (!screenDataOverviewList.isEmpty()) {
            //删除全表数据
            screenDataOverviewService.removeByIds(screenDataOverviewList.stream().map(ScreenDataOverview::getId).collect(Collectors.toList()));
        }
        return R.ok(screenDataOverviewService.saveBatch(screenDataOverviews));
    }
}
