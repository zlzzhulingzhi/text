package cn.qbs.wa.teach.course.standard.controller.manage;


import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.R;

import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import cn.qbs.wa.teach.course.common.entity.CourseComment;
import cn.qbs.wa.teach.course.standard.pojo.coursecomment.*;
import cn.qbs.wa.teach.course.standard.service.CourseCommentService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;



/**
 * 【课程评价】(CourseComment)表控制层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */
@Api(tags = "课程评价")
@RestController
@RequestMapping("/comment")
public class CourseCommentController {

    /**
     * 服务对象
     */
    @Resource
    private CourseCommentService courseCommentService;


    /**
     * 新增【课程评价】
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "新增评价")
    @PostMapping("/add")
    //@RequiresPermissions("courseComment:add")
    @Log(title = "新增【课程评价】", businessType = BusinessType.INSERT)
    public R<Boolean> add(@RequestBody @Validated CourseCommentAddRequest params) {
        // 先查看该学员是否参与过评价，参与过不允许再次评价
        Long userId = SecurityContextHolder.getUserId();
        CourseComment comment = this.courseCommentService.lambdaQuery().eq(CourseComment::getCourseId, params.getCourseId()).eq(CourseComment::getCreateBy, userId).one();
        if (comment != null) {
            return R.fail("该课程您已评分过，无法再次评分");
        }
        return R.ok(this.courseCommentService.add(params));
    }

    /**
     * 分页查询【课程评价】
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "分页查询评价")
    @PostMapping("/page")
    //@RequiresPermissions("courseComment:page")
    //@Log(title = "分页查询【课程评价】", businessType = BusinessType.OTHER)
    public R<IPage<CourseCommentPageResponse>> page(@RequestBody CourseCommentPageRequest params) {
        return R.ok(this.courseCommentService.page(params));
    }

    /**
     * 修改【课程评价】
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "修改评价")
    @PostMapping("update")
    //@RequiresPermissions("courseComment:update")
    @Log(title = "更新【课程评价】", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated CourseCommentUpdateRequest params) {
        return R.ok(this.courseCommentService.update(params));
    }

    /**
     * 删除【课程评价】
     *
     * @param request 主键集合
     * @return
     */
    @ApiOperation(value = "删除评价")
    @PostMapping("/delete")
    //@RequiresPermissions("courseComment:delete")
    @Log(title = "删除【课程评价】", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody @Validated IdListRequest request) {
        return R.ok(this.courseCommentService.deleteByIds(request.getIdList()));
    }

}

