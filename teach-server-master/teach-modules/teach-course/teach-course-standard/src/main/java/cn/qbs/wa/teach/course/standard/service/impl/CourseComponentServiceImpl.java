package cn.qbs.wa.teach.course.standard.service.impl;

import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.course.common.entity.CourseComponent;
import cn.qbs.wa.teach.course.common.entity.CourseComponentAttachment;
import cn.qbs.wa.teach.course.common.enums.ComponentTypeEnum;
import cn.qbs.wa.teach.course.standard.entity.CourseComponentExtra;
import cn.qbs.wa.teach.course.standard.mapper.CourseComponentMapper;
import cn.qbs.wa.teach.course.standard.pojo.coursecomponent.*;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseComponentDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseComponentExtraDTO;
import cn.qbs.wa.teach.course.standard.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 【课程讲次内容】(CourseComponent)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */
@Slf4j
@Service("courseComponentService")
public class CourseComponentServiceImpl extends ServiceImpl<CourseComponentMapper, CourseComponent> implements CourseComponentService {

    @Resource
    private CourseComponentAttachmentService courseComponentAttachmentService;

    @Resource
    private CourseStatisticOverviewService courseStatisticOverviewService;

    @Resource
    private CourseComponentExtraService courseComponentExtraService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public CourseComponent add(ComponentAddRequest componentAddRequest) {
        CourseComponent component = new CourseComponent();
        BeanUtils.copyProperties(componentAddRequest, component);
        this.save(component);
        Long componentId = component.getId();
        if (ComponentTypeEnum.EXTRA.getCode().equals(componentAddRequest.getComponentTypeCode())) {
            CourseComponentExtra courseComponentExtra = new CourseComponentExtra();
            BeanUtils.copyProperties(componentAddRequest, courseComponentExtra);
            courseComponentExtra.setComponentId(componentId);
            this.courseComponentExtraService.save(courseComponentExtra);
        } else {
            CourseComponentAttachment componentAttachment = new CourseComponentAttachment();
            BeanUtils.copyProperties(componentAddRequest, componentAttachment);
            componentAttachment.setComponentId(componentId);
            this.courseComponentAttachmentService.save(componentAttachment);
        }
        //if (ComponentTypeEnum.SP.getCode().equals(componentAddRequest.getComponentTypeCode())) {
        //    this.courseInfoService.updateDurationByCourseId(componentAddRequest.getCourseId(), componentAddRequest.getResourceFileDuration().longValue());
        //}
        //if (ComponentTypeEnum.ZB.getCode().equals(componentAddRequest.getComponentTypeCode()) && component.getId() != null) {
        //    associateLive(componentAddRequest.getCourseId(), component.getId(), componentAddRequest.getResourceFileId());
        //}
        // 课程的课时数 +1
        courseStatisticOverviewService.refreshLessonNum(componentAddRequest.getCourseId());

        return component;
    }

    //private void associateLive(Long courseId, Long businessId, Long liveId) {
    //    String courseName = "";
    //    List<Long> lecturerIds = new ArrayList<>();
    //    if (courseId != null) {
    //        Course course = courseService.getById(courseId);
    //        if (course != null) {
    //            courseName = course.getCourseName();
    //            List<CourseLecturer> lecturers = courseLecturerService.listByCourseId(courseId);
    //            if (!lecturers.isEmpty()) {
    //                lecturerIds = lecturers.stream().map(CourseLecturer::getLecturerId).collect(Collectors.toList());
    //            }
    //        }
    //    }
    //    String msg = remoteService.remoteAssociateLive(businessId, 2, courseName, liveId, lecturerIds);
    //    if (msg != null) {
    //        throw new ServiceException(msg);
    //    }
    //}

    @Override
    public IPage<CourseComponentPageResponse> page(CourseComponentPageRequest params) {
        return this.baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public CourseComponentDetailResponse detail(Serializable id) {
        return this.baseMapper.selectDetailById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(CourseComponentUpdateRequest params) {

        if (ComponentTypeEnum.EXTRA.getCode().equals(params.getComponentTypeCode())) {
            CourseComponentExtra componentExtra=this.courseComponentExtraService.lambdaQuery().eq(CourseComponentExtra::getComponentId,params.getId()).one();
            CourseComponentExtra courseComponentExtra = new CourseComponentExtra();
            BeanUtils.copyProperties(params, courseComponentExtra);
            courseComponentExtra.setId(componentExtra.getId());
            this.courseComponentExtraService.updateById(courseComponentExtra);
            CourseComponent component = new CourseComponent();
            BeanUtils.copyProperties(params, component);
            this.updateById(component);
        }else {
            // 先删除再新增
            this.deleteByIds(Collections.singletonList(params.getId()));
            this.add(params);
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean deleteByIds(List<Long> idList) {
        List<CourseComponentAttachment> courseComponentAttachmentList = this.courseComponentAttachmentService.lambdaQuery()
                .in(CourseComponentAttachment::getComponentId, idList).list();
        List<CourseComponentExtra> componentExtraList=this.courseComponentExtraService.lambdaQuery().in(CourseComponentExtra::getComponentId,idList).list();
        if (!componentExtraList.isEmpty()) {
            this.courseComponentExtraService.removeByIds(componentExtraList.stream().map(CourseComponentExtra::getId).collect(Collectors.toList()));
        }
        List<CourseComponentAttachment> list = this.courseComponentAttachmentService.lambdaQuery().in(CourseComponentAttachment::getComponentId, idList).list();
        if (!list.isEmpty()) {
            //for (CourseComponentAttachment attachment : list) {
            //    if (attachment.getResourceFileDuration() != null) {
            //        long decrDuration = -attachment.getResourceFileDuration().longValue();
            //        this.courseInfoService.updateDurationByCourseId(attachment.getCourseId(), decrDuration);
            //    }
            //}
            this.courseComponentAttachmentService.removeByIds(list.stream().map(CourseComponentAttachment::getId).collect(Collectors.toList()));
        }
        List<CourseComponent> components = this.listByIds(idList);
        for (CourseComponent component : components) {
            // 删除与直播相关的关联关系
            //if (ComponentTypeEnum.ZB.getCode().equals(component.getComponentTypeCode())) {
            //    remoteService.remoteAssociateLive(component.getId(), 2);
            //}
        }

        Map<String, Object> data = new HashMap<>(4);
        for (CourseComponentAttachment courseComponentAttachment : courseComponentAttachmentList) {
            //data.put("fileId", courseComponentAttachment.getResourceFileId());
            //data.put("orgId", courseComponentAttachment.getOrgId());
            //data.put("businessType", FileBusinessTypeEnum.COURSE_COMPONENT.getValue());
            //data.put("businessId", courseComponentAttachment.getComponentId());
            //tdmqProducerTemplate.sendAsyncStringMsg(TopicsConstants.FILE_QUOTED_DELETED, JSON.toJSONString(data));
        }
        return this.removeByIds(idList);
    }

    @Override
    public boolean deleteByCourseId(Long courseId) {
        return this.remove(Wrappers.<CourseComponent>lambdaQuery().eq(CourseComponent::getCourseId, courseId));
    }

    @Override
    public void deleteByLessonId(Long lessonId) {
        LambdaQueryWrapper<CourseComponent> wrapper = Wrappers.<CourseComponent>lambdaQuery().eq(CourseComponent::getLessonId, lessonId);
        List<CourseComponent> courseComponents = this.list(wrapper);
        this.remove(wrapper);
        for (CourseComponent component : courseComponents) {
            // 删除与直播相关的关联关系
            //if (ComponentTypeEnum.ZB.getCode().equals(component.getComponentTypeCode())) {
            //    remoteService.remoteAssociateLive(component.getId(), 2);
            //}
        }
    }

    @Override
    public void deleteByObjs(List<CourseComponent> courseComponents) {
        this.removeByIds(courseComponents.stream().map(CourseComponent::getId).collect(Collectors.toList()));
        for (CourseComponent component : courseComponents) {
            // 删除与直播相关的关联关系
            //if (ComponentTypeEnum.ZB.getCode().equals(component.getComponentTypeCode())) {
            //    remoteService.remoteAssociateLive(component.getId(), 2);
            //}
        }
    }

    @Override
    public List<CourseComponent> listByCourseId(Long courseId) {
        return this.list(Wrappers.<CourseComponent>lambdaQuery().eq(CourseComponent::getCourseId, courseId));
    }

    @Override
    public List<CourseComponent> listByLessonId(Long lessonId) {
        return this.list(Wrappers.<CourseComponent>lambdaQuery().eq(CourseComponent::getLessonId, lessonId));
    }

    @Override
    public List<CourseComponentDTO> listByLesson(Long lessonId) {
        return this.baseMapper.listByLesson(lessonId);
    }

    @Override
    public List<CourseComponentExtraDTO> listByLessonV2(Long lessonId) {
        return this.baseMapper.listByLessonV2(lessonId);
    }

    @Override
    public boolean changeName(ComponentChangeNameRequest params) {
        CourseComponent courseComponent = this.getById(params.getId());
        if (courseComponent == null) {
            throw new IllegalParamsException("查不到课程讲次内容！");
        }

        return this.lambdaUpdate().eq(CourseComponent::getId, params.getId())
                .set(CourseComponent::getComponentName, params.getName())
                .set(CourseComponent::getUpdateBy, SecurityContextHolder.getUserId())
                .set(CourseComponent::getUpdateTime, LocalDateTime.now())
                .update();
    }

}

