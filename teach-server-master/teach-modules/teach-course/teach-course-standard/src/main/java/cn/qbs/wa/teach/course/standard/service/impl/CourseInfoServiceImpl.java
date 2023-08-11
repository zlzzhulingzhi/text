package cn.qbs.wa.teach.course.standard.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.exception.ServiceException;
import cn.qbs.wa.teach.course.common.entity.*;
import cn.qbs.wa.teach.course.common.enums.CourseModeEnum;
import cn.qbs.wa.teach.course.common.enums.CourseTypeEnum;
import cn.qbs.wa.teach.course.common.enums.ShelfStatusEnum;
import cn.qbs.wa.teach.course.standard.constants.CourseConstants;
import cn.qbs.wa.teach.course.standard.mapper.CourseInfoMapper;
import cn.qbs.wa.teach.course.standard.pojo.courseinfo.*;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseInfoDTO;
import cn.qbs.wa.teach.course.standard.service.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 【课程信息】(CourseInfo)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:38
 */
@Slf4j
@Service("courseInfoService")
public class CourseInfoServiceImpl extends ServiceImpl<CourseInfoMapper, CourseInfo> implements CourseInfoService {

    @Resource
    private CourseCategoryService courseCategoryService;
    @Resource
    private CourseLiveLinkService courseLiveLinkService;
    @Resource
    private CourseChapterService courseChapterService;
    @Resource
    private CourseLessonService courseLessonService;
    @Resource
    private CourseComponentService courseComponentService;
    @Resource
    private CourseComponentAttachmentService attachmentService;
    @Resource
    private RemoteServiceImpl remoteService;

    @Override
    public boolean add(CourseInfoAddRequest params) {
        CourseInfo courseInfo = new CourseInfo();
        BeanUtils.copyProperties(params, courseInfo);
        return this.save(courseInfo);
    }

    @Override
    public IPage<CourseInfoPageResponse> page(CourseInfoPageRequest params) {
        return this.baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public CourseInfoDetailResponse detail(Long courseId) {
        CourseInfoDetailResponse courseInfoDetailResponse = this.baseMapper.selectDetailByCourseId(courseId);
        if (courseInfoDetailResponse != null) {
            courseInfoDetailResponse.setCategories(this.courseCategoryService.listByCourseId(courseId));
        }
        return courseInfoDetailResponse;
    }

    @Override
    public boolean update(CourseInfoUpdateRequest params) {
        CourseInfo courseInfo = new CourseInfo();
        BeanUtils.copyProperties(params, courseInfo);
        return this.updateById(courseInfo);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public CourseInfo getByCourseId(Long courseId) {
        return this.baseMapper.getByCourseId(courseId);
    }

    @Override
    public CourseInfoDTO info(Long courseId) {
        CourseInfoDTO courseInfoDTO = new CourseInfoDTO();
        CourseInfoDetailResponse detail = this.detail(courseId);
        if (ShelfStatusEnum.DOWN.getCode().equals(detail.getShelfStatus())) {
            courseInfoDTO.setShelfStatus(ShelfStatusEnum.DOWN.getCode());
            return courseInfoDTO;
        }
        BeanUtils.copyProperties(detail, courseInfoDTO);
        //if (CourseTypeEnum.LIVE.getCode().equals(courseInfoDTO.getCourseType()) && courseInfoDTO.getCourseManage() != null) {
        //   if (courseInfoDTO.getCourseManage() == CourseConstants.COURSE_MANAGE_CLOSE) {
        //        CourseLiveLink liveLink = this.courseLiveLinkService.getLiveLinkByCourseId(courseId);
        //        if (liveLink != null) {
        //            courseInfoDTO.setLiveId(liveLink.getLiveId());
        //            LiveResultDTO liveResultDTO = remoteService.remoteLive(liveLink.getLiveId());
        //            if (liveResultDTO != null) {
        //                BeanUtil.copyProperties(liveResultDTO, courseInfoDTO, "id", "sort");
        //            }
        //        }
        //    } else if (courseInfoDTO.getCourseManage() == CourseConstants.COURSE_MANAGE_OPEN) {
        //        List<CourseComponentAttachment> list = attachmentService.lambdaQuery().eq(CourseComponentAttachment::getCourseId, courseId).list();
        //        if (!list.isEmpty()) {
        //            List<Long> liveIds = list.stream().map(CourseComponentAttachment::getResourceFileId).collect(Collectors.toList());
        //            List<LiveResultDTO> lives = remoteService.remoteLive(liveIds);
        //            if (CollUtil.isNotEmpty(lives)) {
        //                lives.stream().filter(e -> e.getStartTime() != null).min(Comparator.comparing(LiveResultDTO::getStartTime)).ifPresent(e -> {
        //                    courseInfoDTO.setStartTime(e.getStartTime());
        //                    courseInfoDTO.setEndTime(e.getEndTime());
        //                });
        //            }
        //        }
        //    }
        //}
        return courseInfoDTO;
    }

    @Override
    public CourseInfoDTO infos(Long courseId) {
        CourseInfoDTO courseInfoDTO = new CourseInfoDTO();
        CourseInfoDetailResponse detail = this.detail(courseId);
        if (ShelfStatusEnum.DOWN.getCode().equals(detail.getShelfStatus())) {
            courseInfoDTO.setShelfStatus(ShelfStatusEnum.DOWN.getCode());
            //return courseInfoDTO;
        }
        courseInfoDTO.setShelfStatus(ShelfStatusEnum.UP.getCode());
        BeanUtils.copyProperties(detail, courseInfoDTO);
        //if (CourseTypeEnum.LIVE.getCode().equals(courseInfoDTO.getCourseType()) && courseInfoDTO.getCourseManage() != null) {
        //    if (courseInfoDTO.getCourseManage() == CourseConstants.COURSE_MANAGE_CLOSE) {
        //        CourseLiveLink liveLink = this.courseLiveLinkService.getLiveLinkByCourseId(courseId);
        //        if (liveLink != null) {
        //            courseInfoDTO.setLiveId(liveLink.getLiveId());
        //            LiveResultDTO liveResultDTO = remoteService.remoteLive(liveLink.getLiveId());
        //            if (liveResultDTO != null) {
        //                BeanUtil.copyProperties(liveResultDTO, courseInfoDTO, "id", "sort");
        //            }
        //        }
        //    } else if (courseInfoDTO.getCourseManage() == CourseConstants.COURSE_MANAGE_OPEN) {
        //        List<CourseComponentAttachment> list = attachmentService.lambdaQuery().eq(CourseComponentAttachment::getCourseId, courseId).list();
        //        if (!list.isEmpty()) {
        //            List<Long> liveIds = list.stream().map(CourseComponentAttachment::getResourceFileId).collect(Collectors.toList());
        //            List<LiveResultDTO> lives = remoteService.remoteLive(liveIds);
        //            if (CollUtil.isNotEmpty(lives)) {
        //                lives.stream().filter(e -> e.getStartTime() != null).min(Comparator.comparing(LiveResultDTO::getStartTime)).ifPresent(e -> {
        //                    courseInfoDTO.setStartTime(e.getStartTime());
        //                    courseInfoDTO.setEndTime(e.getEndTime());
        //                });
        //            }
        //        }
        //    }
        //}
        return courseInfoDTO;
    }

    @Override
    public void updateDurationByCourseId(Long courseId, Long courseDuration) {
        this.baseMapper.updateDurationByCourseId(courseId, courseDuration);
    }

    @Override
    public void updateByCourseId(CourseInfo courseInfo) {
        CourseInfo info = this.getByCourseId(courseInfo.getCourseId());
        if (info != null) {
            courseInfo.setId(info.getId());
            this.updateById(courseInfo);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean settingMode(CourseModeSettingRequest params) {
        Long courseId = params.getCourseId();
        CourseInfo info = this.getByCourseId(courseId);
        if (info == null) {
            throw new ServiceException("课程信息不存在");
        }
        // 原课程模式
        Integer currMode = info.getCourseMode();
        Integer changeMode = params.getCourseMode();
        if (currMode.equals(changeMode)) {
            return Boolean.FALSE;
        }
        CourseInfo courseInfo = new CourseInfo();
        courseInfo.setId(info.getId());
        courseInfo.setCourseMode(changeMode);
        // 1.设置章节模式
        this.updateById(courseInfo);
        // 2.调整课程目录结构
        // 需要调整的情况：
        // 2.1 章节模式 -> 节模式 (将所有课次(节)的章ID全部指向第一个章，其余章删除)
        // 2.2 章节模式 -> 无章节模式 (将所有课内容的章ID全部指向第一个章，所有课内容的节ID指向第一节，其余章节删除)
        // 2.3 节模式 -> 无章节模式 (所有课内容的节ID指向第一节，其余节删除)
        if (CourseModeEnum.CHAPTER.getCode().equals(currMode)) {
            // 查询课程的所有章与节
            List<CourseChapter> chapters = courseChapterService.listByCourseId(courseId);
            if (chapters.isEmpty()) {
                return Boolean.TRUE;
            }
            if (CourseModeEnum.LESSON.getCode().equals(changeMode)) {
                // 2.1 章节模式 -> 节模式
                if (chapters.size() > 1) {
                    courseChapterService.deleteByCourseId(courseId);
                    List<CourseLesson> lessons = courseLessonService.listByCourseId(courseId);
                    if (!lessons.isEmpty()) {
                        // 创建匿名章
                        CourseChapter chapter = courseChapterService.createDefault(courseId, lessons.size());
                        Long chapterId = chapter.getId();
                        List<CourseLesson> updateCollection = lessons.stream().map(l -> {
                            CourseLesson lesson = new CourseLesson();
                            lesson.setId(l.getId());
                            lesson.setChapterId(chapterId);
                            return lesson;
                        }).collect(Collectors.toList());
                        courseLessonService.updateBatchById(updateCollection);
                    }
                }
            } else if (CourseModeEnum.NONE.getCode().equals(changeMode)) {
                // 2.2 章节模式 -> 无章节模式
                // 删除原有章节
                courseChapterService.deleteByCourseId(courseId);
                courseLessonService.deleteByCourseId(courseId);
                // 创建匿名章
                CourseChapter chapter = courseChapterService.createDefault(courseId, 1);
                Long chapterId = chapter.getId();
                // 创建匿名节
                CourseLesson lesson = courseLessonService.createDefault(courseId, chapterId);
                batchUpdateLesson(courseId, lesson);
            }
        } else if (CourseModeEnum.LESSON.getCode().equals(currMode)) {
            // 2.3 节模式 -> 无章节模式
            if (CourseModeEnum.NONE.getCode().equals(changeMode)) {
                // 删除原有节
                courseLessonService.deleteByCourseId(courseId);
                // 查询课程的所有章与节
                List<CourseChapter> chapters = courseChapterService.listByCourseId(courseId);
                if (chapters.isEmpty()) {
                    return Boolean.TRUE;
                }
                CourseChapter chapter = chapters.get(0);
                // 创建匿名节
                CourseLesson lesson = courseLessonService.createDefault(courseId, chapter.getId());
                batchUpdateLesson(courseId, lesson);
            }
        }
        return Boolean.TRUE;
    }

    private void batchUpdateLesson(Long courseId, CourseLesson lesson) {
        Long lessonId = lesson.getId();
        List<CourseComponent> components = courseComponentService.listByCourseId(courseId);
        if (!components.isEmpty()) {
            List<CourseComponent> updateCollection = components.stream().map(c -> {
                CourseComponent component = new CourseComponent();
                component.setId(c.getId());
                component.setLessonId(lessonId);
                return component;
            }).collect(Collectors.toList());
            courseComponentService.updateBatchById(updateCollection);
        }
    }

    @Override
    public CourseInfoDTO baseInfo(Long courseId) {
        return this.baseMapper.baseInfo(courseId);
    }

    @Override
    public boolean signAuth(SiginAuthRequest params) {
        CourseInfo info = this.getByCourseId(params.getCourseId());
        if (params.getSignUpAuthValue().equals(info.getSignUpAuthValue())){
            return true;
        }
        return false;
    }
}

