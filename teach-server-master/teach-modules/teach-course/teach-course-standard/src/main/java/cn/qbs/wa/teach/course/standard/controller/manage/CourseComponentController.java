package cn.qbs.wa.teach.course.standard.controller.manage;

import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.log.annotation.Log;
import cn.qbs.wa.teach.common.log.enums.BusinessType;
import cn.qbs.wa.teach.course.common.entity.CourseComponent;
import cn.qbs.wa.teach.course.common.enums.ComponentTypeEnum;
import cn.qbs.wa.teach.course.standard.constants.CourseConstants;
import cn.qbs.wa.teach.course.standard.listener.event.CourseTypeChangeEvent;
import cn.qbs.wa.teach.course.standard.listener.event.StudentLessonChangeEvent;
import cn.qbs.wa.teach.course.standard.pojo.coursecomponent.ComponentAddRequest;
import cn.qbs.wa.teach.course.standard.pojo.coursecomponent.ComponentChangeNameRequest;
import cn.qbs.wa.teach.course.standard.pojo.coursecomponent.CourseComponentDetailResponse;
import cn.qbs.wa.teach.course.standard.pojo.coursecomponent.CourseComponentUpdateRequest;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseComponentDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseComponentExtraDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseLessonChangeDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseRelationDTO;
import cn.qbs.wa.teach.course.standard.service.CourseComponentService;
import cn.qbs.wa.teach.course.standard.service.CourseStatisticOverviewService;
import cn.qbs.wa.teach.course.standard.service.RemoteService;
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
 * 【课程讲次内容】(CourseComponent)表控制层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-23 16:14:06
 */
@Api(tags = "讲次内容")
@RestController
@RequestMapping("/component")
public class CourseComponentController {

    /**
     * 服务对象
     */
    @Resource
    private CourseComponentService courseComponentService;

    @Resource
    private CourseStatisticOverviewService courseStatisticOverviewService;

    @Resource
    private RemoteService remoteService;

    @Resource
    private ApplicationContext applicationContext;

    /**
     * 新增【课程讲次内容】
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "附件-新增")
    @PostMapping("/add")
    //@RequiresPermissions("courseComponent:add")
    @Log(title = "新增【课程讲次内容】", businessType = BusinessType.INSERT)
    public R<CourseComponent> add(@RequestBody @Validated ComponentAddRequest params) {
        if (ComponentTypeEnum.SP.getCode().equals(params.getComponentTypeCode())) {
            if (params.getResourceFileDuration() == null) {
                return R.fail("视频时长为空");
            }
        }
        CourseComponent component = this.courseComponentService.add(params);
//        if (component != null && component.getId() != null) {
//            // 课程学员添加课程学习内容
//            CourseLessonChangeDTO courseRelationDTO = new CourseLessonChangeDTO();
//            courseRelationDTO.setComponentId(component.getId());
//            courseRelationDTO.setCourseId(params.getCourseId());
//            courseRelationDTO.setLessonId(params.getLessonId());
//            courseRelationDTO.setChapterId(params.getChapterId());
//            courseRelationDTO.setAction(CourseConstants.COURSE_LESSON_ADD);
//            courseRelationDTO.setOrgId(SecurityContextHolder.getOrgId());
//            courseRelationDTO.setUserId(SecurityContextHolder.getUserId());
//            applicationContext.publishEvent(new StudentLessonChangeEvent(courseRelationDTO));
//
//            // 变更课程类型
//            CourseRelationDTO courseTypeChangeDTO = BeanUtil.copyProperties(courseRelationDTO, CourseRelationDTO.class);
//            applicationContext.publishEvent(new CourseTypeChangeEvent(courseTypeChangeDTO));
//        }
        return R.ok(component);
    }

    /**
     * 查询【课程讲次内容】详情
     *
     * @param params 主键
     * @return
     */
    @ApiOperation(value = "附件-详情")
    @PostMapping("/detail")
    //@RequiresPermissions("courseComponent:details")
    @Log(title = "【课程讲次内容】详情", businessType = BusinessType.OTHER)
    public R<CourseComponentDetailResponse> detail(@RequestBody @Validated IdRequest params) {
        return R.ok(this.courseComponentService.detail(params.getId()));
    }

    /**
     * 修改【课程讲次内容】
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "附件-修改")
    @PostMapping("/update")
    //@RequiresPermissions("courseComponent:update")
    @Log(title = "更新【课程讲次内容】", businessType = BusinessType.UPDATE)
    public R<Boolean> update(@RequestBody @Validated CourseComponentUpdateRequest params) {
        return R.ok(this.courseComponentService.update(params));
    }

    /**
     * 修改【课程讲次内容名称】
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "课程讲次内容名称-修改")
    @PostMapping("/changeName")
    //@RequiresPermissions("courseComponent:changeName")
    @Log(title = "更新【课程讲次内容名称】", businessType = BusinessType.UPDATE)
    public R<Boolean> changeName(@RequestBody @Validated ComponentChangeNameRequest params) {
        return R.ok(this.courseComponentService.changeName(params));
    }

    /**
     * 删除【课程讲次内容】
     *
     * @param params 主键集合
     * @return
     */
    @ApiOperation(value = "附件-删除")
    @PostMapping("/delete")
    //@RequiresPermissions("courseComponent:delete")
    @Log(title = "删除【课程讲次内容】", businessType = BusinessType.DELETE)
    public R<Boolean> delete(@RequestBody @Validated IdListRequest params) {
        List<CourseComponent> courseComponents = this.courseComponentService.listByIds(params.getIdList());
        if (courseComponents.isEmpty()) {
            return R.ok(Boolean.FALSE);
        }
        boolean delete = this.courseComponentService.deleteByIds(params.getIdList());
        if (delete) {
            CourseComponent c = courseComponents.get(0);
            courseStatisticOverviewService.refreshLessonNum(c.getCourseId());
            for (CourseComponent component : courseComponents) {
                //if (ComponentTypeEnum.ZB.getCode().equals(component.getComponentTypeCode())) {
                //    remoteService.remoteAssociateLive(component.getId(), 2);
                //}
                CourseLessonChangeDTO courseRelationDTO = new CourseLessonChangeDTO();
                courseRelationDTO.setLessonId(component.getLessonId());
                courseRelationDTO.setComponentId(component.getId());
                courseRelationDTO.setAction(CourseConstants.COURSE_LESSON_REMOVE);
                courseRelationDTO.setOrgId(SecurityContextHolder.getOrgId());
                courseRelationDTO.setUserId(SecurityContextHolder.getUserId());
                applicationContext.publishEvent(new StudentLessonChangeEvent(courseRelationDTO));
            }

            // 变更课程类型
            CourseRelationDTO courseTypeChangeDTO = new CourseLessonChangeDTO();
            courseTypeChangeDTO.setCourseId(c.getCourseId());
            applicationContext.publishEvent(new CourseTypeChangeEvent(courseTypeChangeDTO));
        }
        return R.ok(delete);
    }

    @ApiOperation(value = "附件内容-列表")
    @PostMapping("/listByLesson")
    //@RequiresPermissions("courseComponent:update")
    @Log(title = "附件内容-列表", businessType = BusinessType.OTHER)
    public R<List<CourseComponentDTO>> listByLesson(@RequestBody @Validated IdRequest request) {
        // 直播课程需要加入直播时间
        List<CourseComponentDTO> courseComponentList = this.courseComponentService.listByLesson(request.getId());
        //for (CourseComponentDTO e : courseComponentList) {
        //    if (ComponentTypeEnum.ZB.getCode().equals(e.getComponentTypeCode())) {
        //        LiveResultDTO liveResultDTO = remoteService.remoteLive(e.getResourceFileId());
        //        if (liveResultDTO != null) {
        //            BeanUtils.copyProperties(liveResultDTO, e, "id", "sort");
        //        }
        //    }
        //}
        return R.ok(courseComponentList);
    }

    @ApiOperation(value = "附件内容-列表-V2")
    @PostMapping("/v2/listByLesson")
    @Log(title = "附件内容-列表", businessType = BusinessType.OTHER)
    public R<List<CourseComponentExtraDTO>> listByLessonV2(@RequestBody @Validated IdRequest request) {
        List<CourseComponentExtraDTO> courseComponentList = this.courseComponentService.listByLessonV2(request.getId());
        return R.ok(courseComponentList);
    }
}

