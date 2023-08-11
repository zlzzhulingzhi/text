package cn.qbs.wa.teach.course.standard.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.course.common.entity.CourseComponentAttachment;
import cn.qbs.wa.teach.course.standard.mapper.CourseComponentAttachmentMapper;
import cn.qbs.wa.teach.course.standard.mapper.CourseStudentLessonMapper;
import cn.qbs.wa.teach.course.standard.pojo.coursecomponentattachment.*;
import cn.qbs.wa.teach.course.standard.pojo.dto.ResourceFileDurationRecordDTO;
import cn.qbs.wa.teach.course.standard.service.CourseComponentAttachmentService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 【课程讲次内容附件】(CourseComponentAttachment)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:38
 */
@Slf4j
@Service("courseComponentAttachmentService")
public class CourseComponentAttachmentServiceImpl extends ServiceImpl<CourseComponentAttachmentMapper, CourseComponentAttachment> implements CourseComponentAttachmentService {

    @Autowired
    private CourseStudentLessonMapper courseStudentLessonMapper;

    @Override
    public boolean add(CourseComponentAttachmentAddRequest params) {
        CourseComponentAttachment courseComponentAttachment = new CourseComponentAttachment();
        BeanUtils.copyProperties(params, courseComponentAttachment);
        return this.save(courseComponentAttachment);
    }

    @Override
    public IPage<CourseComponentAttachmentPageResponse> page(CourseComponentAttachmentPageRequest params) {
        return this.baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public CourseComponentAttachmentDetailResponse detail(Serializable id) {
        return this.baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(CourseComponentAttachmentUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        CourseComponentAttachment courseComponentAttachment = new CourseComponentAttachment();
        BeanUtils.copyProperties(params, courseComponentAttachment);
        return this.updateById(courseComponentAttachment);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public boolean deleteByCourseId(Long courseId) {
        return this.remove(Wrappers.<CourseComponentAttachment>lambdaQuery().eq(CourseComponentAttachment::getCourseId, courseId));
    }

    @Override
    public void deleteByLessonId(Long lessonId) {
        this.remove(Wrappers.<CourseComponentAttachment>lambdaQuery().eq(CourseComponentAttachment::getLessonId, lessonId));
    }

    @Override
    public void deleteByComponentId(List<Long> componentIds) {
        this.remove(Wrappers.<CourseComponentAttachment>lambdaQuery().in(CourseComponentAttachment::getComponentId, componentIds));
    }

    @Override
    public List<CourseComponentAttachment> getByComponentId(Long componentId) {
        return this.lambdaQuery().eq(CourseComponentAttachment::getComponentId, componentId).list();
    }

    @Override
    public void resourceFileDurationRecording(ResourceFileDurationRecordDTO params) {
        List<CourseComponentAttachment> componentAttachmentList = list(new LambdaQueryWrapper<CourseComponentAttachment>().select(CourseComponentAttachment::getId).eq(CourseComponentAttachment::getComponentId, params.getComponentId()));
        if (CollUtil.isNotEmpty(componentAttachmentList)) {
            componentAttachmentList.forEach(i -> i.setResourceFileDuration(params.getResourceFileDuration().intValue()));
            updateBatchById(componentAttachmentList);
        }

    }
}

