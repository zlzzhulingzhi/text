package cn.qbs.wa.train.screen.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.train.screen.entity.*;
import cn.qbs.wa.train.screen.pojo.screen.*;
import cn.qbs.wa.train.screen.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 大屏展示接口
 *
 * @author Administrator
 */
@Api(tags = "大屏展示")
@RestController
@RequestMapping("/show")
public class ScreenController {

    @Resource
    private ScreenNoticeService noticeService;

    @Resource
    private ScreenBannerService bannerService;

    @Resource
    private ScreenAttendClassService attendClassService;

    @Resource
    private ScreenDataOverviewService dataOverviewService;

    @Resource
    private ScreenStatDataDynamicService statDataDynamicService;

    @Resource
    private ScreenStatStudentMonthlyService statStudentMonthlyService;

    @ApiOperation("数据总览")
    @GetMapping("/data-overview")
    public R<List<DataOverviewVO>> dataOverview() {
        List<ScreenDataOverview> list = dataOverviewService.lambdaQuery().orderByAsc(ScreenDataOverview::getSort).list();
        return R.ok(BeanUtil.copyToList(list, DataOverviewVO.class));
    }

    @ApiOperation("基地风貌")
    @GetMapping("/banner")
    public R<List<BannerVO>> banner() {
        List<ScreenBanner> list = bannerService.lambdaQuery().eq(ScreenBanner::getDisplay, Constants.DB_TRUE).orderByAsc(ScreenBanner::getSort).list();
        return R.ok(BeanUtil.copyToList(list, BannerVO.class));
    }

    @ApiOperation("重要通知")
    @GetMapping("/notice")
    public R<List<NoticeVO>> notice() {
        List<ScreenNotice> list = noticeService.lambdaQuery().eq(ScreenNotice::getDisplay, Constants.DB_TRUE).orderByAsc(ScreenNotice::getSort).orderByDesc(ScreenNotice::getPublishDate).list();
        return R.ok(BeanUtil.copyToList(list, NoticeVO.class));
    }

    @ApiOperation("今日培训班")
    @GetMapping("/attend-class")
    public R<List<AttendClassVO>> attendClass() {
        List<ScreenAttendClass> list = attendClassService.list();
        return R.ok(BeanUtil.copyToList(list, AttendClassVO.class));
    }

    @ApiOperation("动态数据统计")
    @GetMapping("/stat-dynamic")
    public R<Map<String, List<StatDataDynamicVO>>> statDynamic(String module) {
        List<ScreenStatDataDynamic> list = statDataDynamicService.lambdaQuery().eq(StrUtil.isNotBlank(module),ScreenStatDataDynamic::getModule, module).eq(ScreenStatDataDynamic::getDisplay, Constants.DB_TRUE).list();
        // 先 module 分组归类, 然后各自分组内排序、转换对象
        Map<String, List<StatDataDynamicVO>> map = list.stream().collect(
                Collectors.groupingBy(
                        ScreenStatDataDynamic::getModule,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                l -> l.stream().sorted(Comparator.comparingInt(ScreenStatDataDynamic::getSort)).map(e -> new StatDataDynamicVO(e.getDataName(), e.getUsingNum(), e.getFreeNum())).distinct().collect(Collectors.toList())
                        )
                )
        );
        return R.ok(map);
    }

    @ApiOperation("每月学员数量统计")
    @GetMapping("/stat-stu-monthly")
    public R<List<StudentMonthlyVO>> statStudentMonthly() {
        // 查询最近12个月统计数据
        List<ScreenStatStudentMonthly> list = statStudentMonthlyService.lambdaQuery()
                .orderByDesc(ScreenStatStudentMonthly::getYear)
                .orderByDesc(ScreenStatStudentMonthly::getMonth)
                .last("LIMIT 12")
                .list();
        List<StudentMonthlyVO> voList = list.stream()
                .sorted(Comparator.comparingInt(ScreenStatStudentMonthly::getYear).thenComparingInt(ScreenStatStudentMonthly::getMonth))
                .map(e -> new StudentMonthlyVO(e.getYear() + StrUtil.DASHED + String.format("%02d", e.getMonth()), e.getNum()))
                .collect(Collectors.toList());
        return R.ok(voList);
    }

}
