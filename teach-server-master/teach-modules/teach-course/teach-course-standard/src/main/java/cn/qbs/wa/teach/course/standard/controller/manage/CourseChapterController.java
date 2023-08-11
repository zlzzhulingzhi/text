package cn.qbs.wa.teach.course.standard.controller.manage;


import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;

import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import cn.qbs.wa.teach.course.common.entity.CourseChapter;
import cn.qbs.wa.teach.course.standard.pojo.coursechapter.*;
import cn.qbs.wa.teach.course.standard.service.CourseChapterService;
import cn.qbs.wa.teach.course.standard.service.CourseStudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 【课程章节】(CourseChapter)表控制层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */
@Api(tags = "课程章节")
@RestController
@RequestMapping("/chapter")
public class CourseChapterController {

    /**
     * 服务对象
     */
    @Resource
    private CourseChapterService courseChapterService;

    @Resource
    private CourseStudentService courseStudentService;


    /**
     * 新增【课程章节】
     *
     * @param params 新增参数
     */
    @ApiOperation(value = "章节-新增")
    @PostMapping("add")
    //@RequiresPermissions("courseChapter:add")
    @Log(title = "新增【课程章节】", businessType = BusinessType.INSERT)
    public R<CourseChapter> add(@RequestBody @Validated ChapterAddRequest params) {
        return R.ok(this.courseChapterService.add(params));
    }

    /**
     * 查询【课程章节】详情
     *
     * @param params 主键
     */
    @ApiOperation(value = "章节-详情")
    @PostMapping("/detail")
    //@RequiresPermissions("courseChapter:details")
    @Log(title = "【课程章节】详情", businessType = BusinessType.OTHER)
    public R<CourseChapterDetailResponse> detail(@RequestBody @Validated IdRequest params) {
        return R.ok(this.courseChapterService.detail(params.getId()));
    }

    /**
     * 修改【课程章节】
     *
     * @param params 更新参数
     */
    @ApiOperation(value = "章节-更新")
    @PostMapping("/update")
    //@RequiresPermissions("courseChapter:update")
    @Log(title = "更新【课程章节】", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated CourseChapterUpdateRequest params) {
        return R.ok(this.courseChapterService.update(params));
    }

    /**
     * 删除【课程章节】
     *
     * @param params 主键集合
     */
    @ApiOperation(value = "章节-删除")
    @PostMapping("/delete")
    //@RequiresPermissions("courseChapter:delete")
    @Log(title = "删除【课程章节】", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody @Validated IdListRequest params) {
        return R.ok(this.courseChapterService.deleteByIds(params.getIdList()));
    }

}

