package cn.qbs.wa.teach.course.standard.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.course.common.entity.CourseAttachment;
import cn.qbs.wa.teach.course.common.entity.CourseStudent;
import cn.qbs.wa.teach.course.common.enums.ComponentTypeEnum;
import cn.qbs.wa.teach.course.common.enums.ShelfStatusEnum;
import cn.qbs.wa.teach.course.standard.mapper.AppCourseMapper;
import cn.qbs.wa.teach.course.standard.pojo.dto.*;
import cn.qbs.wa.teach.course.standard.pojo.dto.app.ListCourseDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.app.PageCommentDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.app.PageCourseDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.app.PageCourseVO;
import cn.qbs.wa.teach.course.standard.service.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yjx
 */
@Service
public class AppCourseServiceImpl implements AppCourseService {

    @Resource
    private RemoteService remoteService;

    @Resource
    private AppCourseMapper appCourseMapper;

    @Resource
    private CourseStudentService courseStudentService;

    @Resource
    private CourseLiveLinkService courseLiveLinkService;

    @Resource
    private CourseAttachmentService courseAttachmentService;

    @Resource
    private CourseComponentAttachmentServiceImpl attachmentService;

    @Override
    public IPage<PageCourseVO> pageCourse(PageCourseDTO params) {
        IPage<PageCourseVO> page;
        Page<PageCourseVO> mpPage = params.createMpPage();
        mpPage.addOrder(new OrderItem("id", false));
        //查询学员的标签和部门
        StudentRelationDTO studentRelation = remoteService.getStudentRelation(SecurityContextHolder.getStudentId());
        params.setDeptIds(studentRelation.getDeptIds());
        params.setGroupIds(studentRelation.getGroupIds());
        if (Boolean.TRUE.equals(params.getInner())) {
            if (params.getIsSignUp() != null) {
                List<CourseStudent> courseList = this.courseStudentService.listCourseByUserId(params.getUserId());
                if (courseList.isEmpty()) {
                    return mpPage;
                }
                params.setCourseIds(courseList.stream().map(CourseStudent::getCourseId).distinct().collect(Collectors.toList()));
                page = this.appCourseMapper.pageInnerCourseSignUp(mpPage, params);
            } else {
                // 内训，展示设置为部分可见的课程以及所有可见的课程
                page = this.appCourseMapper.pageInnerCourse(mpPage, params);
            }
        } else {
            // 外训，是否需要一个对外发布的状态？
            return mpPage;
        }
        return page;
    }

    @Override
    public CourseInfoDTO info(Long courseId) {
        CourseInfoDTO courseInfoDTO = this.detail(courseId);
        if (courseInfoDTO == null) {
            // 已下架或者删除
            CourseInfoDTO down = new CourseInfoDTO();
            down.setShelfStatus(ShelfStatusEnum.DOWN.getCode());
            return down;
        }
        // 远程获取讲师
        remoteService.setLecturers(courseInfoDTO);
        // 直播相关
        //if (CourseTypeEnum.LIVE.getCode().equals(courseInfoDTO.getCourseType()) && courseInfoDTO.getCourseManage() != null) {
        //    if (courseInfoDTO.getCourseManage() == CourseConstants.COURSE_MANAGE_CLOSE) {
        //        CourseLiveLink liveLink = this.courseLiveLinkService.getLiveLinkByCourseId(courseId);
        //        if (liveLink != null) {
        //            courseInfoDTO.setLiveId(liveLink.getLiveId());
        //            setRemoteLiveInfo(courseInfoDTO, liveLink.getLiveId());
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

    private CourseInfoDTO detail(Long courseId) {
        return this.appCourseMapper.selectDetailByCourseId(courseId);
    }

    @Override
    public CourseInfoDTO infos(Long courseId) {
        CourseInfoDTO courseInfoDTO = this.detail(courseId);
        if (courseInfoDTO == null) {
            // 已删除
            CourseInfoDTO down = new CourseInfoDTO();
            down.setShelfStatus(ShelfStatusEnum.DOWN.getCode());
            return down;
        }
        // 远程获取讲师
        remoteService.setLecturers(courseInfoDTO);
        // 直播相关
        //if (CourseTypeEnum.LIVE.getCode().equals(courseInfoDTO.getCourseType()) && courseInfoDTO.getCourseManage() != null) {
        //    if (courseInfoDTO.getCourseManage() == CourseConstants.COURSE_MANAGE_CLOSE) {
        //        CourseLiveLink liveLink = this.courseLiveLinkService.getLiveLinkByCourseId(courseId);
        //        if (liveLink != null) {
        //            courseInfoDTO.setLiveId(liveLink.getLiveId());
        //            setRemoteLiveInfo(courseInfoDTO, liveLink.getLiveId());
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
    public List<CourseChapterDTO> contents(Long courseId) {
        List<CourseChapterDTO> chapterList = this.appCourseMapper.selectContentById(courseId);
        for (CourseChapterDTO courseChapterDTO : chapterList) {
            if (CollUtil.isNotEmpty(courseChapterDTO.getLessonList())) {
                List<CourseLessonDTO> lessonSorts = courseChapterDTO.getLessonList().stream().peek(e -> {
                    if (CollUtil.isNotEmpty(e.getComponentList())
                            && ComponentTypeEnum.ZB.getCode().equals(e.getComponentList().get(0).getComponentTypeCode())) {
                        // TODO 直播课程需要加入直播时间
                        //setRemoteLiveInfo(e, e.getComponentList().get(0).getResourceFileId());
                        //LiveResultDTO liveResultDTO = remoteService.remoteLive(e.getComponentList().get(0).getResourceFileId());
                        //if (liveResultDTO != null) {
                        //    BeanUtils.copyProperties(liveResultDTO, e, "id", "sort");
                        //}
                    }
                }).sorted(Comparator.comparing(CourseLessonDTO::getSort).thenComparing(CourseLessonDTO::getLessonId)).collect(Collectors.toList());
                courseChapterDTO.setLessonList(lessonSorts);
            }
        }
        if (CollUtil.isNotEmpty(chapterList)) {
            chapterList = chapterList.stream().sorted(Comparator.comparing(CourseChapterDTO::getSort).thenComparing(CourseChapterDTO::getChapterId)).collect(Collectors.toList());
        }
        return chapterList;
    }

    @Override
    public List<CourseAttachmentDTO> attachment(Long courseId) {
        List<CourseAttachmentDTO> list = new ArrayList<>();
        //List<CourseAttachment> attachments = this.courseAttachmentService.lambdaQuery()
        //        .select(CourseAttachment::getResourceFileId)
        //        .eq(CourseAttachment::getCourseId, courseId)
        //        .orderByAsc(CourseAttachment::getSort)
        //        .orderByDesc(CourseAttachment::getId)
        //        .list();
        //if (!attachments.isEmpty()) {
        //    Stream<CourseAttachmentDTO> stream = attachments.stream().map(p -> BeanUtil.copyProperties(p, CourseAttachmentDTO.class));
        //    List<Long> ids = attachments.stream().map(CourseAttachment::getResourceFileId).distinct().collect(Collectors.toList());
        //    // 远程过程调用文库列表
        //    List<LibraryResponseDto> remoteLibraryList = remoteService.remoteLibraryList(ids);
        //    if (CollUtil.isNotEmpty(remoteLibraryList)) {
        //        Map<Long, LibraryResponseDto> map = remoteLibraryList.stream().collect(Collectors.toMap(LibraryResponseDto::getId, e -> e));
        //        stream = stream.peek(p -> Optional.ofNullable(map.get(p.getResourceFileId())).ifPresent(e -> {
        //            p.setResourceFileName(e.getLibraryName());
        //            p.setResourceFileType(e.getLibraryType());
        //            p.setDownloads(e.getDownloads());
        //            p.setViews(e.getViews());
        //        }));
        //    }
        //    list = stream.collect(Collectors.toList());
        //}
        return list;
    }

    @Override
    public List<PageCourseVO> recommend(ListCourseDTO params) {
        return null;
    }

    @Override
    public IPage<CourseCommentViewDTO> pageComment(PageCommentDTO params) {
        return this.appCourseMapper.pageComment(params.createMpPage(), params);
    }

    @Override
    public CourseInfoDTO infoByAnon(Long courseId) {
        CourseInfoDTO courseInfoDTO = this.detail(courseId);
        if (courseInfoDTO == null) {
            // 已下架或者删除
            courseInfoDTO = new CourseInfoDTO();
            courseInfoDTO.setShelfStatus(ShelfStatusEnum.DOWN.getCode());
            return courseInfoDTO;
        }
        // 远程获取讲师
        remoteService.setLecturers(courseInfoDTO);
        return courseInfoDTO;
    }

    @Override
    public CourseInfoDTO anonInfo(Long courseId) {
        CourseInfoDTO courseInfoDTO = this.detail(courseId);
        if (ShelfStatusEnum.DOWN.getCode().equals(courseInfoDTO.getShelfStatus())) {
            courseInfoDTO.setShelfStatus(ShelfStatusEnum.DOWN.getCode());
            //return courseInfoDTO;
        }
        courseInfoDTO.setShelfStatus(ShelfStatusEnum.UP.getCode());
        // 远程获取讲师
        remoteService.setLecturers(courseInfoDTO);
        return courseInfoDTO;
    }

    @Override
    public List<CourseChapterDTO> contentsByAnon(Long courseId) {
        List<CourseChapterDTO> chapterList = this.appCourseMapper.selectContentById(courseId);
        for (CourseChapterDTO courseChapterDTO : chapterList) {
            if (CollUtil.isNotEmpty(courseChapterDTO.getLessonList())) {
                List<CourseLessonDTO> lessonSorts = courseChapterDTO.getLessonList().stream().peek(e -> {
                    if (CollUtil.isNotEmpty(e.getComponentList())
                            && ComponentTypeEnum.ZB.getCode().equals(e.getComponentList().get(0).getComponentTypeCode())) {
                        // TODO 直播课程需要加入直播时间
                        //setRemoteLiveInfo(e, e.getComponentList().get(0).getResourceFileId());
                        //e.setPlaybackUrls(null);
                        //e.setComponentList(null);
                    }
                }).sorted(Comparator.comparing(CourseLessonDTO::getSort).thenComparing(CourseLessonDTO::getLessonId)).collect(Collectors.toList());
                courseChapterDTO.setLessonList(lessonSorts);
            }
        }
        if (CollUtil.isNotEmpty(chapterList)) {
            chapterList = chapterList.stream().sorted(Comparator.comparing(CourseChapterDTO::getSort).thenComparing(CourseChapterDTO::getChapterId)).collect(Collectors.toList());
        }
        return chapterList;
    }

    @Override
    public List<CourseChapterDTO> shopContentsByAnon(Long courseId) {
        List<CourseChapterDTO> chapterList = this.appCourseMapper.shopSelectContentById(courseId);
        for (CourseChapterDTO courseChapterDTO : chapterList) {
            if (CollUtil.isNotEmpty(courseChapterDTO.getLessonList())) {
                List<CourseLessonDTO> lessonSorts = courseChapterDTO.getLessonList().stream().peek(e -> {
                    if (CollUtil.isNotEmpty(e.getComponentList())
                            && ComponentTypeEnum.ZB.getCode().equals(e.getComponentList().get(0).getComponentTypeCode())) {
                        // TODO 直播课程需要加入直播时间
                        //setRemoteLiveInfo(e, e.getComponentList().get(0).getResourceFileId());
                        //e.setPlaybackUrls(null);
                        //e.setComponentList(null);
                    }
                }).sorted(Comparator.comparing(CourseLessonDTO::getSort).thenComparing(CourseLessonDTO::getLessonId)).collect(Collectors.toList());
                courseChapterDTO.setLessonList(lessonSorts);
            }
        }
        if (CollUtil.isNotEmpty(chapterList)) {
            chapterList = chapterList.stream().sorted(Comparator.comparing(CourseChapterDTO::getSort).thenComparing(CourseChapterDTO::getChapterId)).collect(Collectors.toList());
        }
        return chapterList;
    }

    @Override
    public List<CourseAttachmentDTO> attachmentByAnon(Long courseId) {
        List<CourseAttachmentDTO> list = new ArrayList<>();
        List<CourseAttachment> attachments = this.courseAttachmentService.lambdaQuery()
                .select(CourseAttachment::getResourceFileName, CourseAttachment::getResourceFileType)
                .eq(CourseAttachment::getCourseId, courseId)
                .orderByAsc(CourseAttachment::getSort)
                .orderByDesc(CourseAttachment::getId)
                .list();
        if (!attachments.isEmpty()) {
            return attachments.stream().map(p -> BeanUtil.copyProperties(p, CourseAttachmentDTO.class)).collect(Collectors.toList());
        }
        return list;
    }

}
