//package cn.qbs.wa.teach.course.standard.controller.web;
//
//import cn.hutool.core.bean.BeanUtil;
//import cn.hutool.core.collection.CollUtil;
//import cn.hutool.core.util.CharsetUtil;
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.crypto.asymmetric.KeyType;
//import cn.hutool.crypto.asymmetric.RSA;
//import cn.qbs.wa.teach.common.core.config.RSAConfig;
//import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
//import cn.qbs.wa.teach.common.core.domain.IdOrgRequest;
//import cn.qbs.wa.teach.common.core.domain.IdRequest;
//import cn.qbs.wa.teach.common.core.domain.R;
//import cn.qbs.wa.teach.common.security.annotation.AutoSelectOrg;
//import cn.qbs.wa.teach.common.security.annotation.TenantFieldInject;
//import cn.qbs.wa.teach.course.common.entity.CourseAttachment;
//import cn.qbs.wa.teach.course.common.entity.CourseStudent;
//import cn.qbs.wa.teach.course.common.entity.CourseStudentLesson;
//import cn.qbs.wa.teach.course.common.entity.CourseStudentStudySnapshot;
//import cn.qbs.wa.teach.course.common.enums.ShelfStatusEnum;
//import cn.qbs.wa.teach.course.standard.enums.SignUpAuthTypeEnum;
//import cn.qbs.wa.teach.course.standard.pojo.course.CourseContentResponse;
//import cn.qbs.wa.teach.course.standard.pojo.course.CoursePageByChildResponse;
//import cn.qbs.wa.teach.course.standard.pojo.course.CoursePageIndexRequest;
//import cn.qbs.wa.teach.course.standard.pojo.course.CoursePageRequest;
//import cn.qbs.wa.teach.course.standard.pojo.coursecomment.CourseCommentPageRequest;
//import cn.qbs.wa.teach.course.standard.pojo.coursecomment.CourseCommentPageResponse;
//import cn.qbs.wa.teach.course.standard.pojo.dto.*;
//import cn.qbs.wa.teach.course.standard.pojo.dto.app.PageCommentDTO;
//import cn.qbs.wa.teach.course.standard.service.*;
//import cn.qbs.wa.teach.library.api.pojo.DTO.library.LibraryResponseDto;
//import cn.qbs.wa.teach.mall.api.RemoteGoodsService;
//import cn.qbs.wa.teach.mall.api.pojo.DTO.GoodsDTO;
//import cn.qbs.wa.teach.mall.api.pojo.DTO.GoodsSourceDTO;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
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
//import java.util.Optional;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
///**
// * 【课程】对外接口(课程中心)
// *
// * @author makejava
// * @version 1.0
// * @date 2021-11-16 11:06:52
// */
//@Api(tags = "课程中心")
//@RestController
//@RequestMapping("/center")
//public class CourseCenterController {
//
//    /**
//     * 课程服务对象
//     */
//    @Resource
//    private CourseService courseService;
//
//    @Resource
//    private CourseInfoService courseInfoService;
//
//    @Resource
//    private CourseCommentService courseCommentService;
//
//    @Resource
//    private CourseStudentService courseStudentService;
//
//    @Resource
//    private CourseStudentLessonService courseStudentLessonService;
//
//    @Resource
//    private CourseAttachmentService courseAttachmentService;
//
//    @Resource
//    private RemoteService remoteService;
//
//    @Resource
//    private CourseStudentStudySnapshotService studentStudySnapshotService;
//
//    @Resource
//    private RemoteGoodsService remoteGoodsService;
//
//    @Resource
//    private AppCourseService appCourseService;
//
//    @Resource
//    private CommonService commonService;
//
//    /**
//     * 分页查询【课程】
//     */
//    @ApiOperation(value = "课程分页查询")
//    @PostMapping("/page")
//    //@RequiresPermissions("course:page")
//    public R<IPage<CoursePageViewDTO>> page(@RequestBody CoursePageSearchDTO params) {
//        params.setUserId(SecurityContextHolder.getUserId());
//        return R.ok(this.courseService.pageCenter(params));
//    }
//
//    /**
//     * 查询课程基础信息
//     */
//    @ApiOperation(value = "课程基本信息-详情")
//    @PostMapping("/info")
//    //@RequiresPermissions("course:details")
//    public R<CourseInfoDTO> detail(@RequestBody @Validated IdRequest request) {
//        Long userId = SecurityContextHolder.getUserId();
//        CourseInfoDTO info = this.courseInfoService.info(request.getId());
//        //判断当前用户是否可见该课程
//        commonService.setCourseVisible(info, userId);
//
//        if (ShelfStatusEnum.DOWN.getCode().equals(info.getShelfStatus())) {
//            return R.ok(info);
//        }
//        // 获取讲师信息
//        remoteService.setLecturers(info);
//        // 判断当前登录用户是否加入该课程
//        CourseStudent courseStudent = this.courseStudentService.lambdaQuery().eq(CourseStudent::getCourseId, request.getId()).eq(CourseStudent::getUserId, userId).one();
//        if (courseStudent != null) {
//            info.setIsSignUp(Boolean.TRUE);
//        } else {
//            info.setIsSignUp(Boolean.FALSE);
//        }
//        // 查询学员最近的一次学习记录,用以开始学习按钮跳转功能
//        CourseStudentStudySnapshot studentCourse = studentStudySnapshotService.getStudentCourse(userId, request.getId());
//        if (studentCourse != null) {
//            info.setLessonId(studentCourse.getLessonId());
//        }
//        R<GoodsDTO> r = remoteGoodsService.detailBySource(new GoodsSourceDTO(request.getId(), "course"));
//        GoodsDTO goods = r.getRemoteData();
//        if (goods != null) {
//            info.setGoodsId(goods.getId());
//            if (goods.getNum() <= 0) {
//                info.setSellOut(Boolean.TRUE);
//            }
//        } else if (r.getCode() == R.FAIL) {
//            return R.fail(r.getMsg());
//        }
//        return R.ok(info);
//    }
//
//    /**
//     * 查询课程基础信息
//     */
//    @ApiOperation(value = "课程基本信息-详情")
//    @PostMapping("/info/v2")
//    //@RequiresPermissions("course:details")
//    public R<CourseInfoDTO> info(@RequestBody @Validated IdRequest request) {
//        Long userId = SecurityContextHolder.getUserId();
//        CourseInfoDTO info = this.courseInfoService.infos(request.getId());
//        //判断当前用户是否可见该课程
//        commonService.setCourseVisible(info, userId);
////        if (ShelfStatusEnum.DOWN.getCode().equals(info.getShelfStatus())) {
////            return R.ok(info);
////        }
//        // 获取讲师信息
//        remoteService.setLecturers(info);
//        // 判断当前登录用户是否加入该课程
//        CourseStudent courseStudent = this.courseStudentService.lambdaQuery().eq(CourseStudent::getCourseId, request.getId()).eq(CourseStudent::getUserId, userId).one();
//        if (courseStudent != null) {
//            info.setIsSignUp(Boolean.TRUE);
//        } else {
//            info.setIsSignUp(Boolean.FALSE);
//        }
//        // 查询学员最近的一次学习记录,用以开始学习按钮跳转功能
//        CourseStudentStudySnapshot studentCourse = studentStudySnapshotService.getStudentCourse(userId, request.getId());
//        if (studentCourse != null) {
//            info.setLessonId(studentCourse.getLessonId());
//        }
//        R<GoodsDTO> r = remoteGoodsService.detailBySource(new GoodsSourceDTO(request.getId(), "course"));
//        GoodsDTO goods = r.getRemoteData();
//        if (goods != null) {
//            info.setGoodsId(goods.getId());
//            if (goods.getNum() <= 0) {
//                info.setSellOut(Boolean.TRUE);
//            }
//        } else if (r.getCode() == R.FAIL) {
//            return R.fail(r.getMsg());
//        }
//        return R.ok(info);
//    }
//
//    /**
//     * 查询课程章节目录
//     */
//    @ApiOperation(value = "课程排课-详情")
//    @PostMapping("/content")
//    //@RequiresPermissions("course:details")
//    public R<List<CourseChapterDTO>> detailContent(@RequestBody @Validated IdRequest request) {
//        // 获取课程目录的时候，判断学员是否已报名，若没有报名则不返回视频链接地址
//        if (!courseStudentService.isSignUp(request.getId(), SecurityContextHolder.getStudentId())) {
//            return R.ok(this.appCourseService.contentsByAnon(request.getId()));
//        }
//        CourseContentResponse courseContentResponse = this.courseService.detailContent(request.getId());
//        // 查找当前学员的课程学习进度
//        List<CourseStudentLesson> lessons = courseStudentLessonService.getStudentCourseAllLesson(SecurityContextHolder.getUserId(), request.getId());
//        if (!lessons.isEmpty()) {
//            Map<Long, CourseStudentLesson> map = lessons.stream().collect(Collectors.toMap(CourseStudentLesson::getComponentId, Function.identity(), (o1, o2) -> o2));
//            for (CourseChapterDTO courseChapterDTO : courseContentResponse.getChapterList()) {
//                for (CourseLessonDTO courseLessonDTO : courseChapterDTO.getLessonList()) {
//                    for (CourseComponentDTO courseComponentDTO : courseLessonDTO.getComponentList()) {
//                        CourseStudentLesson courseStudentLesson = map.get(courseComponentDTO.getComponentId());
//                        if (courseStudentLesson != null) {
//                            courseComponentDTO.setPlaybackPosition(courseStudentLesson.getPlaybackPosition());
//                            courseComponentDTO.setPlaybackProgress(courseStudentLesson.getPlaybackProgress());
//                            courseComponentDTO.setLearningDuration(courseStudentLesson.getLearningDuration());
//                        }
//                    }
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
//    public R<IPage<CourseCommentViewDTO>> page(@RequestBody CourseCommentPageRequest request) {
//        IPage<CourseCommentViewDTO> page = new Page<>();
//        if (request.getCourseId() == null) {
//            return R.ok(page);
//        }
//        IPage<CourseCommentPageResponse> pageR = this.courseCommentService.page(request);
//        if (!pageR.getRecords().isEmpty()) {
//            page = pageR.convert(p -> BeanUtil.copyProperties(p, CourseCommentViewDTO.class));
//        }
//        return R.ok(page);
//    }
//
//    /**
//     * 课程讲义
//     */
//    @ApiOperation(value = "课程讲义")
//    @PostMapping("/attachment")
//    public R<List<CourseAttachmentDTO>> attachment(@RequestBody IdRequest params) {
//        List<CourseAttachmentDTO> list = new ArrayList<>();
//        Long courseId = params.getId();
//        if (courseId == null) {
//            return R.ok(list);
//        }
//        // 获取课程目录的时候，判断学员是否已报名，若没有报名则不返回视频链接地址
//        if (!courseStudentService.isSignUp(params.getId(), SecurityContextHolder.getStudentId())) {
//            return R.ok(this.appCourseService.attachmentByAnon(params.getId()));
//        }
//        List<CourseAttachment> attachments = this.courseAttachmentService.lambdaQuery()
//                .select(CourseAttachment::getResourceFileId)
//                .eq(CourseAttachment::getCourseId, courseId)
//                .orderByAsc(CourseAttachment::getSort)
//                .orderByDesc(CourseAttachment::getId)
//                .list();
//        if (!attachments.isEmpty()) {
//            Stream<CourseAttachmentDTO> stream = attachments.stream().map(p -> BeanUtil.copyProperties(p, CourseAttachmentDTO.class));
//            List<Long> ids = attachments.stream().map(CourseAttachment::getResourceFileId).distinct().collect(Collectors.toList());
//            // 远程过程调用文库列表
//            List<LibraryResponseDto> libraryResponseDtos = remoteService.remoteLibraryList(ids);
//            if (CollUtil.isNotEmpty(libraryResponseDtos)) {
//                Map<Long, LibraryResponseDto> map = libraryResponseDtos.stream().collect(Collectors.toMap(LibraryResponseDto::getId, e -> e));
//                stream = stream.peek(p -> Optional.ofNullable(map.get(p.getResourceFileId())).ifPresent(e -> {
//                    p.setResourceFileName(e.getLibraryName());
//                    p.setResourceFileType(e.getLibraryType());
//                    p.setDownloads(e.getDownloads());
//                    p.setViews(e.getViews());
//                }));
//            }
//            list = stream.collect(Collectors.toList());
//        }
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
//    //@RequiresPermissions("course:page")
//    public R<List<CoursePageViewDTO>> recommend(@RequestBody CourseListSearchDTO params) {
//        if (params.getUserId() == null) {
//            params.setUserId(SecurityContextHolder.getUserId());
//        }
//        return R.ok(this.courseService.recommend(params));
//    }
//
//    /**
//     * 分页查询【课程】
//     */
//    @TenantFieldInject
//    @ApiOperation(value = "课程分页查询")
//    @PostMapping("/anon/page")
//    //@RequiresPermissions("course:page")
//    public R<IPage<CoursePageViewDTO>> pageByAnon(@RequestBody CoursePageSearchDTO params) {
//        if (params.getOrgId() != null) {
//            SecurityContextHolder.setSelectOrgId(params.getOrgId().toString());
//        }
//        if (!Boolean.FALSE.equals(params.getInner())) {
//            // 内训需要传入 orgId
//            if (params.getOrgId() == null) {
//                params.setOrgId(SecurityContextHolder.getOrgId());
//            }
//        }
//        return R.ok(this.courseService.pageCenter(params));
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
//
//    /**
//     * 分页查询【课程】
//     */
//    @ApiOperation(value = "首页-课程分页查询")
//    @PostMapping("/index/page")
//    //@RequiresPermissions("course:page")
//    @AutoSelectOrg
//    public R<IPage<CoursePageViewDTO>> pageByIndex(@RequestBody CoursePageIndexRequest params) {
//        return R.ok(this.courseService.pageCourseByIndex(params));
//    }
//
//    /**
//     * 分页查询插件
//     *
//     * @param params
//     * @return
//     */
//    @PostMapping("/index/pageByChild")
//    //@RequiresPermissions("wCourse:page")
//    //@Log(title = "分页查询插件-课程表", businessType = BusinessType.OTHER)
//    @AutoSelectOrg
//    public R<IPage<CoursePageByChildResponse>> pageByChild(@RequestBody CoursePageRequest params) {
//        return R.ok(this.courseService.pageByChild(params));
//    }
//
//    @PostMapping("/signUp/check")
//    public R<CourseInfoDTO> signUpCheck(@RequestBody @Validated IdRequest request) {
//        Long courseId = request.getId();
//        CourseInfoDTO info = this.courseInfoService.baseInfo(courseId);
//        if (info == null) {
//            return R.fail("课程已下架");
//        }
//        if (info.getSignUpAuthType().equals(SignUpAuthTypeEnum.NEED.getCodeValue())){
//            //加密报名验证值
//            RSA rsa = new RSA(RSAConfig.privateKey, RSAConfig.publicKey);
//            byte[] encrypt = rsa.encrypt(StrUtil.bytes(info.getSignUpAuthValue(), CharsetUtil.CHARSET_UTF_8), KeyType.PrivateKey);
//            info.setSignUpAuthValue(new String(encrypt));
//        }
//        if (ShelfStatusEnum.DOWN.getCode().equals(info.getShelfStatus())) {
//            return R.ok(info);
//        }
//        Long userId = SecurityContextHolder.getUserId();
//        // 判断当前登录用户是否加入该课程
//        CourseStudent courseStudent = this.courseStudentService.lambdaQuery().eq(CourseStudent::getCourseId, courseId).eq(CourseStudent::getUserId, userId).one();
//        if (courseStudent != null) {
//            info.setIsSignUp(Boolean.TRUE);
//        } else {
//            info.setIsSignUp(Boolean.FALSE);
//        }
//        commonService.setCourseVisible(info, userId);
//        // 课程售罄
//        R<GoodsDTO> r = remoteGoodsService.detailBySource(new GoodsSourceDTO(courseId, "course"));
//        GoodsDTO goods = r.getRemoteData();
//        if (goods != null) {
//            info.setGoodsId(goods.getId());
//            if (goods.getNum() <= 0) {
//                info.setSellOut(Boolean.TRUE);
//            }
//        }
//
//        return R.ok(info);
//    }
//}
//
//
