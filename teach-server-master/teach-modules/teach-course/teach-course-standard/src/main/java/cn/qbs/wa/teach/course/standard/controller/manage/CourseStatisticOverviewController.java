package cn.qbs.wa.teach.course.standard.controller.manage;

import cn.qbs.wa.teach.common.core.domain.R;

import cn.qbs.wa.teach.course.standard.pojo.coursestatisticoverview.*;
import cn.qbs.wa.teach.course.standard.service.CourseStatisticOverviewService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;

/**
 * 【课程统计信息】(CourseStatisticOverview)表控制层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:39
 */
@ApiIgnore
@RestController
@RequestMapping("courseStatisticOverview")
public class CourseStatisticOverviewController {

    /**
     * 服务对象
     */
    @Resource
    private CourseStatisticOverviewService courseStatisticOverviewService;


    /**
     * 新增【课程统计信息】
     *
     * @param params
     * @return
     */
    @PostMapping("add")
    //@RequiresPermissions("courseStatisticOverview:add")
    //@Log(title = "新增【课程统计信息】", businessType = BusinessType.INSERT)
    public R<Boolean> add(@RequestBody @Validated CourseStatisticOverviewAddRequest params) {
        return R.ok(this.courseStatisticOverviewService.add(params));
    }

    /**
     * 分页查询【课程统计信息】
     *
     * @param params
     * @return
     */
    @PostMapping("page")
    //@RequiresPermissions("courseStatisticOverview:page")
    //@Log(title = "分页查询【课程统计信息】", businessType = BusinessType.OTHER)
    public R<IPage<CourseStatisticOverviewPageResponse>> page(@RequestBody CourseStatisticOverviewPageRequest params) {
        return R.ok(this.courseStatisticOverviewService.page(params));
    }

    /**
     * 查询【课程统计信息】详情
     *
     * @param id 主键
     * @return
     */
    @PostMapping("detail")
    //@RequiresPermissions("courseStatisticOverview:details")
    //@Log(title = "【课程统计信息】详情", businessType = BusinessType.OTHER)
    public R<CourseStatisticOverviewDetailResponse> detail(Long id) {
        return R.ok(this.courseStatisticOverviewService.detail(id));
    }

    /**
     * 修改【课程统计信息】
     *
     * @param params
     * @return
     */
    @PostMapping("update")
    //@RequiresPermissions("courseStatisticOverview:update")
    //@Log(title = "更新【课程统计信息】", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated CourseStatisticOverviewUpdateRequest params) {
        return R.ok(this.courseStatisticOverviewService.update(params));
    }

    /**
     * 删除【课程统计信息】
     *
     * @param idList 主键集合
     * @return
     */
    @PostMapping("delete")
    //@RequiresPermissions("courseStatisticOverview:delete")
    //@Log(title = "删除【课程统计信息】", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestParam("ids") List<Long> idList) {
        return R.ok(this.courseStatisticOverviewService.deleteByIds(idList));
    }

}

