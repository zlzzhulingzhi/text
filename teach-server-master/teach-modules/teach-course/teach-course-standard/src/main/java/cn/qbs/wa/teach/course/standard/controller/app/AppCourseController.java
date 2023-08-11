//package cn.qbs.wa.teach.course.standard.controller.app;
//
//import cn.hutool.core.collection.CollUtil;
//import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
//import cn.qbs.wa.teach.common.core.domain.IdOrgRequest;
//import cn.qbs.wa.teach.common.core.domain.IdRequest;
//import cn.qbs.wa.teach.common.core.domain.R;
//import cn.qbs.wa.teach.common.log.annotation.Log;
//import cn.qbs.wa.teach.common.log.enums.BusinessType;
//import cn.qbs.wa.teach.common.security.annotation.TenantFieldInject;
//import cn.qbs.wa.teach.course.common.entity.CourseComment;
//import cn.qbs.wa.teach.course.common.entity.CourseStudent;
//import cn.qbs.wa.teach.course.common.entity.CourseStudentLesson;
//import cn.qbs.wa.teach.course.standard.pojo.course.CourseContentResponse;
//import cn.qbs.wa.teach.course.standard.pojo.coursecomment.CourseCommentAddRequest;
//import cn.qbs.wa.teach.course.standard.pojo.dto.*;
//import cn.qbs.wa.teach.course.standard.pojo.dto.app.ListCourseDTO;
//import cn.qbs.wa.teach.course.standard.pojo.dto.app.PageCommentDTO;
//import cn.qbs.wa.teach.course.standard.pojo.dto.app.PageCourseDTO;
//import cn.qbs.wa.teach.course.standard.pojo.dto.app.PageCourseVO;
//import cn.qbs.wa.teach.course.standard.service.*;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
///**
// * 【课程】课程中心(APP)
// *
// * @author yjx
// * @version 1.0
// */
//@Api(tags = "课程中心(APP)")
//@Slf4j
////@RestController
////@RequestMapping("/app")
//public class AppCourseController {
//
//    /**
//     * 课程服务对象
//     */
//    @Resource
//    private AppCourseService appCourseService;
//
//    @Resource
//    private CourseService courseService;
//
//    @Resource
//    private CourseStudentService courseStudentService;
//
//    @Resource
//    private CourseStudentLessonService courseStudentLessonService;
//
//    @Resource
//    private CoursePersonalService coursePersonalService;
//
//    @Resource
//    private CourseCommentService courseCommentService;
//
//    @Resource
//    private CommonService commonService;
//
//    /**
//     * 分页查询【课程】
//     */
//    @ApiOperation(value = "课程分页查询")
//    @PostMapping("/page")
//    public R<IPage<PageCourseVO>> page(@RequestBody PageCourseDTO params) {
//        if (params.getOrgId() != null) {
//            SecurityContextHolder.setSelectOrgId(params.getOrgId().toString());
//        }
//        params.setUserId(SecurityContextHolder.getUserId());
//        return R.ok(this.appCourseService.pageCourse(params));
//    }
//
//    /**
//     * 查询课程基础信息
//     */
//    @ApiOperation(value = "课程信息-详情")
//    @PostMapping("/info")
//    public R<CourseInfoDTO> detail(@RequestBody @Validated IdRequest request) {
//        CourseInfoDTO info = this.appCourseService.info(request.getId());
//        //判断当前用户是否可见该课程
//        Long userId = SecurityContextHolder.getUserId();
//        commonService.setCourseVisible(info, userId);
//        Long count = this.courseStudentService.lambdaQuery().eq(CourseStudent::getCourseId, request.getId()).eq(CourseStudent::getUserId, userId).count();
//        if (count != null && count > 0) {
//            info.setIsSignUp(Boolean.TRUE);
//        } else {
//            info.setIsSignUp(Boolean.FALSE);
//        }
//
//        return R.ok(info);
//    }
//
//    /**
//     * 查询课程基础信息
//     */
//    @ApiOperation(value = "课程信息-详情")
//    @PostMapping("/info/v2")
//    public R<CourseInfoDTO> info(@RequestBody @Validated IdRequest request) {
//        CourseInfoDTO info = this.appCourseService.infos(request.getId());
//        //判断当前用户是否可见该课程
//        Long userId = SecurityContextHolder.getUserId();
//        commonService.setCourseVisible(info, userId);
//        CourseStudent courseStudent = this.courseStudentService.lambdaQuery().eq(CourseStudent::getCourseId, request.getId()).eq(CourseStudent::getUserId, userId).one();
//        if (courseStudent != null) {
//            info.setIsSignUp(Boolean.TRUE);
//            info.setLearningRate(courseStudent.getLearningRate());
//        } else {
//            info.setIsSignUp(Boolean.FALSE);
//        }
//
//        return R.ok(info);
//    }
//
//    /**
//     * 查询课程章节目录
//     */
//    @ApiOperation(value = "课程信息-目录")
//    @PostMapping("/content")
//    public R<List<CourseChapterDTO>> detailContent(@RequestBody @Validated IdRequest request) {
//        // 获取课程目录的时候，判断学员是否已报名，若没有报名则不返回视频链接地址
//        if (!courseStudentService.isSignUp(request.getId(), SecurityContextHolder.getStudentId())) {
//            return R.ok(this.appCourseService.contentsByAnon(request.getId()));
//        }
//        CourseContentResponse courseContentResponse = this.courseService.detailContent(request.getId());
//        // 查找当前学员的课程学习进度
//        List<CourseStudentLesson> lessons = courseStudentLessonService.getStudentCourseAllLesson(SecurityContextHolder.getUserId(), request.getId());
//        if (!lessons.isEmpty()) {
//            Map<Long, List<CourseStudentLesson>> chapterMap = lessons.stream().collect(Collectors.groupingBy(CourseStudentLesson::getChapterId));
//            for (CourseChapterDTO courseChapterDTO : courseContentResponse.getChapterList()) {
//                List<CourseStudentLesson> lessonList = chapterMap.get(courseChapterDTO.getChapterId());
//                if (CollUtil.isNotEmpty(lessonList)) {
//                    Map<Long, CourseStudentLesson> map = lessonList.stream().collect(Collectors.toMap(CourseStudentLesson::getComponentId, Function.identity(), (o1, o2) -> o2));
//                    courseChapterDTO.getLessonList().forEach(e -> {
//                                for (CourseComponentDTO courseComponentDTO : e.getComponentList()) {
//                                    CourseStudentLesson courseStudentLesson = map.get(courseComponentDTO.getComponentId());
//                                    if (courseStudentLesson!= null) {
//                                        courseComponentDTO.setPlaybackPosition(courseStudentLesson.getPlaybackPosition());
//                                        courseComponentDTO.setPlaybackProgress(courseStudentLesson.getPlaybackProgress());
//                                        courseComponentDTO.setLearningDuration(courseStudentLesson.getLearningDuration());
//                                    }
//                                }
//                            }
//                    );
//                }
//            }
//        }
//        return R.ok(courseContentResponse.getChapterList());
//    }
//
//    /**
//     * 课程评价
//     */
//    @ApiOperation(value = "课程评价-分页")
//    @PostMapping("/comment")
//    public R<IPage<CourseCommentViewDTO>> page(@RequestBody PageCommentDTO request) {
//        IPage<CourseCommentViewDTO> page = new Page<>();
//        if (request.getCourseId() == null) {
//            return R.ok(page);
//        }
//        return R.ok(this.appCourseService.pageComment(request));
//    }
//
//    /**
//     * 课程讲义
//     */
//    @ApiOperation(value = "课程讲义-列表")
//    @PostMapping("/attachment")
//    public R<List<CourseAttachmentDTO>> attachment(@RequestBody IdRequest request) {
//        List<CourseAttachmentDTO> list = new ArrayList<>();
//        Long courseId = request.getId();
//        if (courseId == null) {
//            return R.ok(list);
//        }
//        // 获取课程目录的时候，判断学员是否已报名，若没有报名则不返回视频链接地址
//        if (!courseStudentService.isSignUp(request.getId(), SecurityContextHolder.getStudentId())) {
//            return R.ok(this.appCourseService.attachmentByAnon(request.getId()));
//        }
//        list = this.appCourseService.attachment(courseId);
//        return R.ok(list);
//    }
//
//    /**
//     * 分页查询【课程】
//     */
//    @TenantFieldInject
//    @ApiOperation(value = "课程分页查询")
//    @PostMapping("/anon/page")
//    public R<IPage<PageCourseVO>> pageByAnon(@RequestBody PageCourseDTO params) {
//        if (params.getOrgId() != null) {
//            SecurityContextHolder.setSelectOrgId(params.getOrgId().toString());
//        }
//        return R.ok(this.appCourseService.pageCourse(params));
//    }
//
//    /**
//     * 查询课程基础信息
//     */
//    @TenantFieldInject
//    @ApiOperation(value = "课程信息-详情")
//    @PostMapping("/anon/info")
//    public R<CourseInfoDTO> detailByAnon(@RequestBody @Validated IdOrgRequest request) {
//        SecurityContextHolder.setSelectOrgId(request.getOrgId().toString());
//        CourseInfoDTO info = this.appCourseService.infoByAnon(request.getId());
//        return R.ok(info);
//    }
//
//    /**
//     * 查询课程基础信息
//     */
//    @TenantFieldInject
//    @ApiOperation(value = "课程信息-详情")
//    @PostMapping("/anon/info/v2")
//    public R<CourseInfoDTO> infoByAnon(@RequestBody @Validated IdOrgRequest request) {
//        SecurityContextHolder.setSelectOrgId(request.getOrgId().toString());
//        CourseInfoDTO info = this.appCourseService.anonInfo(request.getId());
//        return R.ok(info);
//    }
//
//    /**
//     * 查询课程章节目录
//     */
//    @TenantFieldInject
//    @ApiOperation(value = "课程信息-目录")
//    @PostMapping("/anon/content")
//    public R<List<CourseChapterDTO>> detailContentByAnon(@RequestBody @Validated IdOrgRequest request) {
//        SecurityContextHolder.setSelectOrgId(request.getOrgId().toString());
//        List<CourseChapterDTO> chapterDTOList = this.appCourseService.contentsByAnon(request.getId());
//        return R.ok(chapterDTOList);
//    }
//
//    /**
//     * 课程评价
//     */
//    @TenantFieldInject
//    @ApiOperation(value = "课程评价-分页")
//    @PostMapping("/anon/comment")
//    public R<IPage<CourseCommentViewDTO>> pageByAnon(@RequestBody PageCommentDTO request) {
//        SecurityContextHolder.setSelectOrgId(request.getOrgId().toString());
//        IPage<CourseCommentViewDTO> page = new Page<>();
//        if (request.getCourseId() == null) {
//            return R.ok(page);
//        }
//        return R.ok(this.appCourseService.pageComment(request));
//    }
//
//    /**
//     * 课程讲义
//     */
//    @TenantFieldInject
//    @ApiOperation(value = "课程讲义-列表")
//    @PostMapping("/anon/attachment")
//    public R<List<CourseAttachmentDTO>> attachmentByAnon(@RequestBody IdOrgRequest request) {
//        SecurityContextHolder.setSelectOrgId(request.getOrgId().toString());
//        List<CourseAttachmentDTO> list = new ArrayList<>();
//        Long courseId = request.getId();
//        if (courseId == null) {
//            return R.ok(list);
//        }
//        list = this.appCourseService.attachmentByAnon(courseId);
//        return R.ok(list);
//    }
//
//    /**
//     * 推荐课程
//     * 1.总共推荐5个课程；
//     * 2.推荐前三个课程为同个讲师的三个报名最多的课程，若不足三个则推荐最新的直播课；
//     * 3.后两个课程为报名最多的课程。
//     */
//    @ApiOperation(value = "推荐课程")
//    @PostMapping("/recommend")
//    public R<List<PageCourseVO>> recommend(@RequestBody ListCourseDTO params) {
//        params.setUserId(SecurityContextHolder.getUserId());
//        return R.ok(this.appCourseService.recommend(params));
//    }
//
//    /**
//     * 课程学习情况收集
//     */
//    @ApiOperation(value = "课程学习情况收集")
//    @PostMapping("/recording")
//    public R<Boolean> recording(@RequestBody MyCourseStudyingDTO params) {
//        params.setUserId(SecurityContextHolder.getUserId());
//        params.setOrgId(SecurityContextHolder.getOrgId());
//        try {
//            this.coursePersonalService.recording(params);
//        } catch (Exception e) {
//            log.error("课程学习情况收集出错", e);
//        }
//        return R.ok(true);
//    }
//
//    /**
//     * 分页查询【我的课程】
//     */
//    @ApiOperation(value = "我的课程分页查询")
//    @PostMapping("/personal/page")
//    public R<IPage<MyCoursePageDTO>> page(@RequestBody MyCoursePageSearchDTO params) {
//        if (params.getUserId() == null) {
//            params.setUserId(SecurityContextHolder.getUserId());
//        }
//        return R.ok(this.courseStudentService.pageMyCourse(params));
//    }
//
//    /**
//     * 新增【课程评价】
//     *
//     * @param params
//     * @return
//     */
//    @ApiOperation(value = "新增评价")
//    @PostMapping("/add")
//    //@RequiresPermissions("courseComment:add")
//    @Log(title = "新增【课程评价】", businessType = BusinessType.INSERT)
//    public R<Boolean> add(@RequestBody @Validated CourseCommentAddRequest params) {
//        // 先查看该学员是否参与过评价，参与过不允许再次评价
//        Long userId = SecurityContextHolder.getUserId();
//        CourseComment comment = this.courseCommentService.lambdaQuery().eq(CourseComment::getCourseId, params.getCourseId()).eq(CourseComment::getCreateBy, userId).one();
//        if (comment != null) {
//            return R.fail("该课程您已评分过，无法再次评分");
//        }
//        return R.ok(this.courseCommentService.add(params));
//    }
//}
//
