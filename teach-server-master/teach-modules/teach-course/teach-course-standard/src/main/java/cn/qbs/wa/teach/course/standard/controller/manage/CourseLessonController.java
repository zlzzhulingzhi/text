package cn.qbs.wa.teach.course.standard.controller.manage;

import cn.qbs.wa.teach.common.core.constant.Constants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import cn.qbs.wa.teach.course.common.entity.CourseChapter;
import cn.qbs.wa.teach.course.standard.constants.CourseConstants;
import cn.qbs.wa.teach.course.common.entity.CourseLesson;
import cn.qbs.wa.teach.course.standard.listener.event.StudentLessonChangeEvent;
import cn.qbs.wa.teach.course.standard.pojo.courselesson.CourseLessonDetailResponse;
import cn.qbs.wa.teach.course.standard.pojo.courselesson.CourseLessonUpdateRequest;
import cn.qbs.wa.teach.course.standard.pojo.courselesson.LessonAddRequest;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseLessonChangeDTO;
import cn.qbs.wa.teach.course.standard.service.CourseChapterService;
import cn.qbs.wa.teach.course.standard.service.CourseLessonService;
import cn.qbs.wa.teach.course.standard.service.CourseStudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 【课程讲次】(CourseLesson)表控制层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-23 16:15:34
 */
@Api(tags = "课程讲次")
@RestController
@RequestMapping("/lesson")
public class CourseLessonController {

    /**
     * 服务对象
     */
    @Resource
    private CourseLessonService courseLessonService;

    @Resource
    private CourseStudentService courseStudentService;

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private CourseChapterService courseChapterService;

    /**
     * 新增【课程讲次】
     *
     * @param params
     */
    @ApiOperation(value = "讲次-新增")
    @PostMapping("/add")
    //@RequiresPermissions("courseLesson:add")
    @Log(title = "新增【课程讲次】", businessType = BusinessType.INSERT)
    public R<CourseLesson> add(@RequestBody @Validated LessonAddRequest params) {
        Long count= courseChapterService.lambdaQuery().eq(CourseChapter::getCourseId,params.getCourseId()).count();
        if(count!=null && count<=0){
            // 创建匿名章
            CourseChapter chapter = courseChapterService.createDefault(params.getCourseId(), 1);
            params.setChapterId(chapter.getId());
        }
        CourseLesson lesson = this.courseLessonService.add(params);
        //if (lesson != null && lesson.getId() != null) {
        //    CourseLessonChangeDTO courseRelationDTO = new CourseLessonChangeDTO();
        //    courseRelationDTO.setLessonId(lesson.getId());
        //    courseRelationDTO.setCourseId(params.getCourseId());
        //    courseRelationDTO.setChapterId(params.getChapterId());
        //    courseRelationDTO.setAction(CourseConstants.COURSE_LESSON_ADD);
        //    courseRelationDTO.setOrgId(SecurityContextHolder.getOrgId());
        //    courseRelationDTO.setUserId(SecurityContextHolder.getUserId());
        //    applicationContext.publishEvent(new StudentLessonChangeEvent(courseRelationDTO));
        //}
        return R.ok(lesson);
    }

    /**
     * 查询【课程讲次】详情
     *
     * @param params 主键
     */
    @ApiOperation(value = "讲次-详情")
    @PostMapping("/detail")
    //@RequiresPermissions("courseLesson:details")
    @Log(title = "【课程讲次】详情", businessType = BusinessType.OTHER)
    public R<CourseLessonDetailResponse> detail(@RequestBody @Validated IdRequest params) {
        return R.ok(this.courseLessonService.detail(params.getId()));
    }

    /**
     * 修改【课程讲次】
     *
     * @param params
     */
    @ApiOperation(value = "讲次-修改")
    @PostMapping("/update")
    //@RequiresPermissions("courseLesson:update")
    @Log(title = "更新【课程讲次】", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated CourseLessonUpdateRequest params) {
        return R.ok(this.courseLessonService.update(params));
    }

    /**
     * 删除【课程讲次】
     *
     * @param params 主键集合
     */
    @ApiOperation(value = "讲次-删除")
    @PostMapping("/delete")
    //@RequiresPermissions("courseLesson:delete")
    @Log(title = "删除【课程讲次】", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody @Validated IdListRequest params) {
        boolean delete = this.courseLessonService.deleteByIds(params.getIdList());
        //if (delete) {
        //    params.getIdList().forEach(id -> {
        //        CourseLessonChangeDTO courseRelationDTO = new CourseLessonChangeDTO();
        //        courseRelationDTO.setLessonId(id);
        //        courseRelationDTO.setAction(CourseConstants.COURSE_LESSON_REMOVE);
        //        courseRelationDTO.setOrgId(SecurityContextHolder.getOrgId());
        //        courseRelationDTO.setUserId(SecurityContextHolder.getUserId());
        //        applicationContext.publishEvent(new StudentLessonChangeEvent(courseRelationDTO));
        //    });
        //}
        return R.ok(delete);
    }

}

