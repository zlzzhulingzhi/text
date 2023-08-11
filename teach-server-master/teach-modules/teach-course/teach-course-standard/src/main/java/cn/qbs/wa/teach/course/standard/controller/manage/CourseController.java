package cn.qbs.wa.teach.course.standard.controller.manage;

import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.*;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import cn.qbs.wa.teach.course.common.entity.*;
import cn.qbs.wa.teach.course.common.enums.ShelfStatusEnum;
import cn.qbs.wa.teach.course.standard.entity.TCourseStudent;
import cn.qbs.wa.teach.course.standard.pojo.course.*;
import cn.qbs.wa.teach.course.standard.pojo.coursecategory.CourseCategoryAndLecturerId;
import cn.qbs.wa.teach.course.standard.pojo.coursedept.CourseDeptBatchAddRequest;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseLecturerDTO;
import cn.qbs.wa.teach.course.standard.service.*;
import cn.qbs.wa.teach.organization.api.RemoteLecturerService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.lecturer.LecturerDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.lecturer.LecturerPageResultDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.lecturer.LecturerPageSearchDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.lecturer.LecturerSearchDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 【课程】(Course)表控制层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-16 11:06:52
 */
@Slf4j
@Api(tags = "课程")
@RestController
public class CourseController {

    /**
     * 课程服务对象
     */
    @Resource
    private CourseService courseService;

    @Resource
    private CourseInfoService courseInfoService;

    @Resource
    private CourseStudentService courseStudentService;

    @Resource
    private CourseAttachmentService courseAttachmentService;

    @Resource
    private CourseStatisticOverviewService courseStatisticOverviewService;

    @Resource
    private CourseUserVisibleService courseUserVisibleService;

    @Resource
    private CourseChapterService courseChapterService;

    @Resource
    private CourseLessonService courseLessonService;

    @Resource
    private RemoteLecturerService remoteLecturerService;

    @Resource
    private CourseLecturerService courseLecturerService;

    @Resource
    private WCourseService wCourseService;

    /**
     * 分页查询【课程】
     */
    @ApiOperation(value = "课程分页查询")
    @PostMapping("/page")
    //@RequiresPermissions("course:page")
    @Log(title = "课程分页查询", businessType = BusinessType.OTHER)
    public R<IPage<CoursePageResponse>> page(@RequestBody CoursePageRequest params) {
        return R.ok(this.courseService.page(params));
    }

    /**
     * 新增课程基本信息
     */
    @ApiOperation(value = "课程基本信息-新增")
    @PostMapping("/base/add")
    //@RequiresPermissions("course:add")
    @Log(title = "新增课程基本信息", businessType = BusinessType.INSERT)
    public R<Course> add(@RequestBody @Validated CourseAddRequest params) {
        Course course = this.courseService.add(params);
        try {
            // 默认创建课程一章一节的目录
            // 创建匿名章
            Long courseId = course.getId();
            CourseChapter chapter = courseChapterService.createDefault(courseId, 1);
            Long chapterId = chapter.getId();
            // 创建匿名节
            courseLessonService.createDefault(courseId, chapterId);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return R.ok(course);
    }

    /**
     * 查询课程基础信息
     */
    @ApiOperation(value = "课程基本信息-详情")
    @PostMapping("/base/detail")
    //@RequiresPermissions("course:details")
    @Log(title = "课程基础信息", businessType = BusinessType.OTHER)
    public R<CourseDetailResponse> detail(@RequestBody IdRequest request) {
        Long courseId = request.getId();
        if (courseId == null) {
            return R.fail("课程ID不能为空");
        }
        return R.ok(this.courseService.detail(courseId));
    }

    /**
     * 课程上下架功能
     * @param params 修改参数
     */
    @ApiOperation(value = "课程基本信息-设置")
    @PostMapping("/base/setting")
    @Transactional(rollbackFor = Exception.class)
    //@RequiresPermissions("course:update")
    @Log(title = "课程基本信息-设置", businessType = BusinessType.UPDATE)
    public R<Boolean> setting(@RequestBody CourseSettingRequest params) {
        if (params.getId() == null) {
            R.fail("课程ID不能为空");
        }
        if(ShelfStatusEnum.UP.getCode().equals(params.getShelfStatus())){
            WCourse wCourse=new WCourse();
            wCourse.setId(params.getId());
            wCourseService.save(wCourse);
        }
        if(ShelfStatusEnum.DOWN.getCode().equals(params.getShelfStatus())){
            wCourseService.removeById(params.getId());
        }
        boolean update;
        if (params.getPlaybackSpeed() != null || params.getCourseManage() != null || params.getGooded() != null) {
            update = this.courseInfoService.update(Wrappers.<CourseInfo>lambdaUpdate()
                    .eq(CourseInfo::getCourseId, params.getId())
                    .set(params.getPlaybackSpeed() != null, CourseInfo::getPlaybackSpeed, params.getPlaybackSpeed())
                    .set(params.getCourseManage() != null, CourseInfo::getCourseManage, params.getCourseManage())
                    .set(params.getGooded() != null, CourseInfo::getGooded, params.getGooded())
            );
        } else {
            params.setDeleted(null);
            if (ShelfStatusEnum.UP.getCode().equals(params.getShelfStatus())) {
                params.setShelfTime(LocalDateTime.now());
            } else if (ShelfStatusEnum.DOWN.getCode().equals(params.getShelfStatus())) {
                //if (this.courseStudentService.checkCourseStudent(params.getId())) {
                //    return R.fail("该课程有报名学员，无法下架");
                //}
            }
            update = this.courseService.updateById(params);
        }
        return R.ok(update);
    }

    /**
     * 修改【课程】
     */
    @ApiOperation(value = "课程基本信息-修改")
    @PostMapping("/base/update")
    //@RequiresPermissions("course:update")
    @Log(title = "更新【课程】", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated CourseUpdateRequest params) {
//        if (UserVisibleStatusEnum.SOME.getCode().equals(params.getUserVisibleStatus()) && (CollectionUtils.isEmpty(params.getVisibleUsers())
//                && CollectionUtils.isEmpty(params.getCourseUserDeptVisibles()) && CollectionUtils.isEmpty(params.getCourseUserGroupVisibles()))) {
//            return R.fail("请选择可见学员或者部门或者标签");
//        }
        Integer signUpLimit = params.getSignUpLimit();
        CourseStatisticOverview overview = courseStatisticOverviewService.getByCourseId(params.getId());
        /*if (overview != null && overview.getSignUpNum() > signUpLimit) {
            return R.fail("当前课程名额已小于所报名人数");
        }*/
        boolean b = this.courseService.update(params);
        return R.ok(b);
    }

    /**
     * 新增【课程排课】
     */
    @ApiOperation(value = "课程排课-新增")
    @PostMapping("/content/add")
    //@RequiresPermissions("course:add")
    @Log(title = "新增课程排课", businessType = BusinessType.INSERT)
    public R<Course> addContent(@RequestBody @Validated CourseContentAddRequest params) {
        return R.ok(this.courseService.addContent(params));
    }

    /**
     * 修改【课程排课】
     */
    @ApiOperation(value = "课程排课-修改")
    @PostMapping("/content/update")
    //@RequiresPermissions("course:update")
    @Log(title = "修改课程排课", businessType = BusinessType.UPDATE)
    public R<Boolean> updateContent(@RequestBody @Validated CourseContentUpdateRequest params) {
        return R.ok(this.courseService.updateContent(params));
    }

    /**
     * 查询课程章节目录
     */
    @ApiOperation(value = "课程排课-详情")
    @PostMapping("/content/detail")
    //@RequiresPermissions("course:details")
    @Log(title = "课程章节目录", businessType = BusinessType.OTHER)
    public R<CourseContentResponse> detailContent(@RequestBody IdRequest request) {
        Long courseId = request.getId();
        if (courseId == null) {
            return R.fail("课程ID不能为空");
        }
        return R.ok(this.courseService.detailContent(courseId));
    }

    @ApiOperation(value = "课程目录排序")
    @PostMapping("/content/tree/sort")
    //@RequiresPermissions("course:update")
    @Log(title = "课程目录排序", businessType = BusinessType.UPDATE)
    public R<Boolean> sortContentTree(@RequestBody @Validated CourseContentSortRequest request) {
        return R.ok(this.courseService.sortContentTree(request));
    }

    @ApiOperation(value = "课程目录结构")
    @PostMapping("/content/list")
    //@RequiresPermissions("course:update")
    @Log(title = "课程目录结构", businessType = BusinessType.UPDATE)
    public R<CourseContentResponse> contents(@RequestBody @Validated IdRequest request) {
        return R.ok(this.courseService.contents(request.getId()));
    }

    /**
     * 删除【课程】
     */
    @ApiOperation(value = "课程删除")
    @PostMapping("/delete")
    //@RequiresPermissions("course:delete")
    @Log(title = "课程删除", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody @Validated IdListRequest request) {
        for (Long courseId : request.getIdList()) {
            // 先判断该课程是否存在已报名学员，若出现已报名不允许操作
            if (this.courseStudentService.checkCourseStudent(courseId)) {
                return R.fail("该课程有报名学员，无法操作");
            }
        }
        return R.ok(this.courseService.deleteByIds(request.getIdList()));
    }

    /**
     * 删除【课程可见学员】
     */
    @ApiOperation(value = "删除可见学员")
    @PostMapping("/remove/student")
    public R<Boolean> removeStudent(@RequestBody @Validated IdListRequest request) {
        return R.ok(this.courseUserVisibleService.deleteByIds(request.getIdList()));
    }

    @PostMapping("/select/list")
    public R<List<CourseListResponse>> listSelect(@RequestBody CourseListRequest params) {
        return R.ok(this.courseService.listSelect(params));
    }

    /**
     * 根据讲师id分页查询【课程】
     */
    @ApiOperation(value = "机构后台我的课程分页查询")
    @PostMapping("/myCoursePage")
    @Log(title = "课程分页查询", businessType = BusinessType.OTHER)
    public R<IPage<CoursePageByLecturerResponse>> myCoursePage(@RequestBody CoursePageByLecturerRequest params) {
        if (params.getLecturerId() == null) {
            //根据userId远程获取讲师id
            LecturerPageSearchDTO lecturerPageSearchDTO = new LecturerPageSearchDTO();
            lecturerPageSearchDTO.setUserId(SecurityContextHolder.getUserId());
            lecturerPageSearchDTO.setOrgId(SecurityContextHolder.getOrgId());
            R<PageResultComDTO<LecturerPageResultDTO>> pageResultComDTO = remoteLecturerService.page(lecturerPageSearchDTO);
            if (CollUtil.isEmpty(pageResultComDTO.getData().getRecords())){
                return R.ok();
            }
            params.setLecturerId(pageResultComDTO.getData().getRecords().get(0).getId());
        }
        return R.ok(this.courseService.myCoursePage(params));
    }

    /**
     * 根据讲师id分页查询【课程】
     */
    @ApiOperation(value = "前台讲师的课程分页")
    @PostMapping("student/myCoursePage")
    @Log(title = "课程分页查询", businessType = BusinessType.OTHER)
    public R<IPage<CoursePageByLecturerResponse>> lecturerCoursePage(@RequestBody CoursePageByLecturerRequest params) {
        if (params.getLecturerId() == null) {
            //根据userId远程获取讲师id
            LecturerPageSearchDTO lecturerPageSearchDTO = new LecturerPageSearchDTO();
            lecturerPageSearchDTO.setUserId(SecurityContextHolder.getUserId());
            lecturerPageSearchDTO.setOrgId(SecurityContextHolder.getOrgId());
            R<PageResultComDTO<LecturerPageResultDTO>> pageResultComDTO = remoteLecturerService.page(lecturerPageSearchDTO);
            if (CollUtil.isEmpty(pageResultComDTO.getData().getRecords())){
                return R.ok();
            }
            params.setLecturerId(pageResultComDTO.getData().getRecords().get(0).getId());
        }
        if (params.getUserId() == null){
            params.setUserId(SecurityContextHolder.getUserId());
        }
        if (params.getShelfStatus() == null){
            params.setShelfStatus(1);
        }
        return R.ok(this.courseService.myCoursePage(params));
    }

    @PostMapping("/category/count")
    @ApiOperation(value = "获取分类下的数量")
    public R<Long> categoryCourseCount(@RequestBody @Validated CourseCategoryAndLecturerId param) {
        return R.ok(this.courseService.categoryCount(param.getIdList(), param.getLecturerId(), param.getStudentId()));
    }

    @PostMapping("/category/countByLecturerId")
    @ApiOperation(value = "根据讲师id获取分类下的数量")
    public R<Long> categoryCourseCountByLecturerId(@RequestBody @Validated CourseCategoryAndLecturerId param) {
        LecturerSearchDTO lecturerSearchDTO = new LecturerSearchDTO();
        lecturerSearchDTO.setUserId(SecurityContextHolder.getUserId());
        R<List<LecturerDTO>> lecturerList = remoteLecturerService.listLecturers(lecturerSearchDTO);
        if (CollectionUtils.isEmpty(lecturerList.getData())){
            return R.ok(0L);
        }
        param.setLecturerId(lecturerList.getData().get(0).getId());
        return R.ok(this.courseService.categoryCount(param.getIdList(), param.getLecturerId(), param.getStudentId()));
    }

    @PostMapping("/group/count")
    @ApiOperation(value = "获取标签分类下的课程数量")
    public R<Long> groupCount(@RequestBody @Validated IdListParam param) {
        return R.ok(this.courseService.groupCount(param.getIdList()));
    }

    @PostMapping("/dept/count")
    @ApiOperation(value = "获取部门分类下的课程数量")
    public R<Long> deptCount(@RequestBody @Validated IdListParam param) {
        return R.ok(this.courseService.deptCount(param.getIdList()));
    }

    /**
     * 机构后台部门下的课程分页
     */
    @ApiOperation(value = "部门下的课程分页")
    @PostMapping("/pageByDept")
    @Log(title = "部门下的课程分页", businessType = BusinessType.OTHER)
    public R<IPage<CoursePageResponse>> pageByDept(@RequestBody CoursePageRequest params) {
        return R.ok(this.courseService.pageByDept(params));
    }

    /**
     * 机构后台标签下的课程分页
     */
    @ApiOperation(value = "标签下的课程分页")
    @PostMapping("/pageByGroup")
    @Log(title = "标签下的课程分页", businessType = BusinessType.OTHER)
    public R<IPage<CoursePageResponse>> pageByGroup(@RequestBody CoursePageRequest params) {
        return R.ok(this.courseService.pageByGroup(params));
    }

    /**
     * 机构后台部门下的课程批量新增
     */
    @ApiOperation(value = "部门批量新增课程")
    @PostMapping("/deptBatchAdd")
    @Log(title = "部门批量新增课程", businessType = BusinessType.OTHER)
    public R<Boolean> deptBatchAdd(@RequestBody CourseDeptBatchAddRequest params) {
        return R.ok(this.courseService.deptBatchAdd(params));
    }

    /**
     * 机构后台部门下的课程批量移除
     */
    @ApiOperation(value = "部门批量移除课程")
    @PostMapping("/deptBatchRemove")
    @Log(title = "部门批量移除课程", businessType = BusinessType.OTHER)
    public R<Boolean> deptBatchRemove(@RequestBody CourseRemoveDeptRequest params) {
        return R.ok(this.courseService.deptBatchRemove(params));
    }

    /**
     * 机构后台标签下的课程批量新增
     */
    @ApiOperation(value = "标签批量新增课程")
    @PostMapping("/groupBatchAdd")
    @Log(title = "标签批量新增课程", businessType = BusinessType.OTHER)
    public R<Boolean> groupBatchAdd(@RequestBody CourseAddGroupRequest params) {
        return R.ok(this.courseService.groupBatchAdd(params));
    }

    /**
     * 机构后台标签下的课程批量移除
     */
    @ApiOperation(value = "标签批量移除课程")
    @PostMapping("/groupBatchRemove")
    @Log(title = "标签批量移除课程", businessType = BusinessType.OTHER)
    public R<Boolean> groupBatchRemove(@RequestBody CourseRemoveGroupRequest params) {
        return R.ok(this.courseService.groupBatchRemove(params));
    }

    /**
     * 修改课程讲师信息
     */
    @ApiOperation(value = "修改课程讲师信息")
    @PostMapping("/updateCourseLecturer")
    @Log(title = "修改课程讲师信息", businessType = BusinessType.OTHER)
    public R<Boolean> updateCourseLecturer(@RequestBody CourseLecturerDTO params) {
        if (params.getId() != null){
            CourseLecturer courseLecturer = new CourseLecturer();
            BeanUtils.copyProperties(params, courseLecturer);
            return R.ok(courseLecturerService.updateById(courseLecturer));
        }
        if (params.getLecturerId() != null){
            return R.ok(courseLecturerService.update(Wrappers.<CourseLecturer>lambdaUpdate()
                    .set(CourseLecturer::getLecturerName, params.getLecturerName())
                    .eq(CourseLecturer::getLecturerId, params.getLecturerId())));
        }
        return R.fail();
    }

    @PostMapping("getOrgList")
    @ApiOperation(value = "获取机构课程列表")
    public R<IPage<CoursePageResponse>> getOrgList(@RequestBody CoursePageRequest params) {
        return R.ok(this.courseService.getOrgList(params));
    }
}

