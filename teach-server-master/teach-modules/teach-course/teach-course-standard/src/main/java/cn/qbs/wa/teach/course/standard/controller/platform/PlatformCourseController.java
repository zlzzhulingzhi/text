package cn.qbs.wa.teach.course.standard.controller.platform;

import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.PlatformIdListRequest;
import cn.qbs.wa.teach.common.core.domain.PlatformIdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import cn.qbs.wa.teach.common.security.annotation.AutoSelectOrg;
import cn.qbs.wa.teach.course.common.entity.Course;
import cn.qbs.wa.teach.course.common.entity.CourseInfo;
import cn.qbs.wa.teach.course.common.enums.UserVisibleStatusEnum;
import cn.qbs.wa.teach.course.standard.pojo.course.*;
import cn.qbs.wa.teach.course.standard.service.CourseInfoService;
import cn.qbs.wa.teach.course.standard.service.CourseService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;

/**
 * 【课程】(Course)表控制层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-16 11:06:52
 */
//@ApiIgnore
@Api(tags = "平台课程"/*, hidden = true*/)
@RestController
@RequestMapping("/platform")
public class PlatformCourseController {

    /**
     * 课程服务对象
     */
    @Resource
    private CourseService courseService;

    @Resource
    private CourseInfoService courseInfoService;

    /**
     * 分页查询【课程】
     *//*
    @ApiOperation(value = "课程分页查询")
    @PostMapping("/page")
    //@RequiresPermissions("course:page")
    @Log(title = "课程分页查询", businessType = BusinessType.OTHER)
    public R<IPage<CoursePageResponse>> page(@RequestBody CoursePageRequest params) {
        if (params.getOrgId() == null) {
            return R.ok(params.createMpPage());
        }
        SecurityContextHolder.setSelectOrgId(params.getOrgId().toString());
        return R.ok(this.courseService.page(params));
    }

    *//**
     * 分页查询【课程】
     *//*
    @ApiOperation(value = "平台管理-课程选择器")
    @PostMapping("/admin/page")
    //@RequiresPermissions("course:page")
    @Log(title = "课程分页查询", businessType = BusinessType.OTHER)
    public R<IPage<CoursePageResponse>> pageAdmin(@RequestBody CoursePageRequest params) {
        return R.ok(this.courseService.pageAdmin(params));
    }

    *//**
     * 新增课程基本信息
     *//*
    @ApiOperation(value = "课程基本信息-新增")
    @PostMapping("/base/add")
    //@RequiresPermissions("course:add")
    @Log(title = "新增课程基本信息", businessType = BusinessType.INSERT)
    public R<Course> add(@RequestBody @Validated CourseAddRequest params) {
        if (params.getOrgId() == null) {
            return R.fail("请选择机构");
        }
        SecurityContextHolder.setSelectOrgId(params.getOrgId().toString());
        if (UserVisibleStatusEnum.SOME.getCode().equals(params.getUserVisibleStatus()) && CollectionUtils.isEmpty(params.getVisibleUsers())) {
            return R.fail("请选择可见用户");
        }
        return R.ok(this.courseService.add(params));
    }
*/
    /**
     * 查询课程基础信息
     */
    @ApiOperation(value = "课程基本信息-详情")
    @PostMapping("/base/detail")
    //@RequiresPermissions("course:details")
    @Log(title = "课程基础信息", businessType = BusinessType.OTHER)
    public R<CourseDetailResponse> detail(@RequestBody @Validated PlatformIdRequest params) {
        SecurityContextHolder.setSelectOrgId(params.getOrgId().toString());
        return R.ok(this.courseService.detail(params.getId()));
    }

   /* *//**
     * 课程上下架功能
     * @param params 修改参数
     *//*
    @ApiOperation(value = "课程基本信息-设置")
    @PostMapping("/base/setting")
    //@RequiresPermissions("course:update")
    @Log(title = "课程基本信息-设置", businessType = BusinessType.UPDATE)
    public R<Boolean> page(@RequestBody CourseSettingRequest params) {
        if (params.getOrgId() == null) {
            return R.fail("请选择机构");
        }
        SecurityContextHolder.setSelectOrgId(params.getOrgId().toString());
        if (params.getId() == null) {
            R.fail("课程ID不能为空");
        }
        boolean update;
        if (params.getPlaybackSpeed() != null) {
            update = courseInfoService.update(Wrappers.<CourseInfo>lambdaUpdate().eq(CourseInfo::getCourseId, params.getId()).set(CourseInfo::getPlaybackSpeed, params.getPlaybackSpeed()));
        } else {
            params.setDeleted(null);
            update = courseService.updateById(params);
        }
        return R.ok(update);
    }

    *//**
     * 修改【课程】
     *//*
    @ApiOperation(value = "课程基本信息-修改")
    @PostMapping("/base/update")
    //@RequiresPermissions("course:update")
    @Log(title = "更新【课程】", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated CourseUpdateRequest params) {
        if (params.getOrgId() == null) {
            return R.fail("请选择机构");
        }
        SecurityContextHolder.setSelectOrgId(params.getOrgId().toString());
        if (UserVisibleStatusEnum.SOME.getCode().equals(params.getUserVisibleStatus()) && CollectionUtils.isEmpty(params.getVisibleUsers())) {
            return R.fail("请选择可见用户");
        }
        return R.ok(this.courseService.update(params));
    }

    *//**
     * 查询课程章节目录
     *//*
    @ApiOperation(value = "课程排课-详情")
    @PostMapping("/content/detail")
    //@RequiresPermissions("course:details")
    @Log(title = "课程章节目录", businessType = BusinessType.OTHER)
    public R<CourseContentResponse> detailContent(@RequestBody @Validated PlatformIdRequest params) {
        SecurityContextHolder.setSelectOrgId(params.getOrgId().toString());
        return R.ok(this.courseService.detailContent(params.getId()));
    }

    *//**
     * 删除【课程】
     *//*
    @ApiOperation(value = "课程删除")
    @PostMapping("/delete")
    //@RequiresPermissions("course:delete")
    @Log(title = "课程删除", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody @Validated PlatformIdListRequest params) {
        SecurityContextHolder.setSelectOrgId(params.getOrgId().toString());
        return R.ok(this.courseService.deleteByIds(params.getIdList()));
    }

    *//**
     * 复制课程
     *//*
    @ApiOperation(value = "课程复制")
    @PostMapping("/copy")
    //@RequiresPermissions("course:add")
    @Log(title = "课程复制", businessType = BusinessType.INSERT)
    public R<Course> copy(@RequestBody @Validated PlatformCopyCourseRequest params) {
        SecurityContextHolder.setSelectOrgId(params.getToOrgId().toString());
        return R.ok(this.courseService.copyToOrg(params.getId(), params.getCourseName(), params.getToOrgId()));
    }


    *//**
     * 根据讲师id分页查询【课程】
     *//*
    @ApiOperation(value = "讲师的课程分页查询")
    @PostMapping("/lectureCoursePage")
    @AutoSelectOrg
    public R<IPage<CoursePageByLecturerResponse>> myCoursePage(@RequestBody CoursePageByLecturerRequest params) {
        return R.ok(this.courseService.myCoursePage(params));
    }*/
}

