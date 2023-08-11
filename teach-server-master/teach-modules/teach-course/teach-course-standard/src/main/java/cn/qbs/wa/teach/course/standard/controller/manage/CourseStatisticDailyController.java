package cn.qbs.wa.teach.course.standard.controller.manage;


import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.standard.pojo.coursestatisticdaily.CourseStatisticDailyAddRequest;
import cn.qbs.wa.teach.course.standard.pojo.coursestatisticdaily.CourseStatisticDailyListRequest;
import cn.qbs.wa.teach.course.standard.pojo.coursestatisticdaily.CourseStatisticDailyPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.coursestatisticdaily.CourseStatisticDailyPageResponse;
import cn.qbs.wa.teach.course.standard.service.CourseStatisticDailyService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;



/**
 * 【课程学习每日统计】(CourseStatisticDaily)表控制层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:39
 */
@Api(tags = "课程学习每日统计")
@RestController
@RequestMapping("/statistic/daily")
public class CourseStatisticDailyController {

    /**
     * 服务对象
     */
    @Resource
    private CourseStatisticDailyService courseStatisticDailyService;


    /**
     * 新增【课程学习每日统计】
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "课程学习每日统计-新增")
    @PostMapping("/add")
    //@RequiresPermissions("courseStatisticDaily:add")
    //@Log(title = "新增【课程学习每日统计】", businessType = BusinessType.INSERT)
    public R<Boolean> add(@RequestBody @Validated CourseStatisticDailyAddRequest params) {
        return R.ok(this.courseStatisticDailyService.add(params));
    }

    /**
     * 分页查询【课程学习每日统计】
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "课程学习每日统计-分页")
    @PostMapping("/page")
    //@RequiresPermissions("courseStatisticDaily:page")
    //@Log(title = "分页查询【课程学习每日统计】", businessType = BusinessType.OTHER)
    public R<IPage<CourseStatisticDailyPageResponse>> page(@RequestBody @Validated CourseStatisticDailyPageRequest params) {
        return R.ok(this.courseStatisticDailyService.page(params));
    }

    /**
     * 分页查询【课程学习每日统计】
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "课程学习每日统计-列表")
    @PostMapping("/list")
    //@RequiresPermissions("courseStatisticDaily:page")
    //@Log(title = "分页查询【课程学习每日统计】", businessType = BusinessType.OTHER)
    public R<List<CourseStatisticDailyPageResponse>> list(@RequestBody @Validated CourseStatisticDailyListRequest params) {
        if (params.getStartStatisticDate().plusDays(31).isBefore(params.getEndStatisticDate())) {
            return R.fail("日期范围不能超过31天");
        }
        return R.ok(this.courseStatisticDailyService.list(params));
    }


    /**
     * 分页查询【课程学习每日统计】
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "课程学习每日统计-折线图")
    @PostMapping("/line")
    //@RequiresPermissions("courseStatisticDaily:page")
    //@Log(title = "分页查询【课程学习每日统计】", businessType = BusinessType.OTHER)
    public R<List<CourseStatisticDailyPageResponse>> line(@RequestBody @Validated CourseStatisticDailyListRequest params) {
        if (params.getStartStatisticDate().plusDays(31).isBefore(params.getEndStatisticDate())) {
            return R.fail("日期范围不能超过31天");
        }
        return R.ok(this.courseStatisticDailyService.line(params));
    }

}

