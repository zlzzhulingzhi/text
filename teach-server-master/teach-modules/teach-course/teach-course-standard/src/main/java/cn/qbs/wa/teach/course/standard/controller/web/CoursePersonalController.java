//package cn.qbs.wa.teach.course.standard.controller.web;
//
//import cn.hutool.core.bean.BeanUtil;
//import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
//import cn.qbs.wa.teach.common.core.domain.R;
//import cn.qbs.wa.teach.course.common.entity.CourseComment;
//import cn.qbs.wa.teach.course.standard.pojo.course.CourseDetailResponse;
//import cn.qbs.wa.teach.course.standard.pojo.coursecomment.CourseCommentRequest;
//import cn.qbs.wa.teach.course.standard.pojo.dto.*;
//import cn.qbs.wa.teach.course.standard.service.CourseCommentService;
//import cn.qbs.wa.teach.course.standard.service.CoursePersonalService;
//import cn.qbs.wa.teach.course.standard.service.CourseStudentService;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author yjx
// */
//@Slf4j
//@Api(tags = "我的课程")
//@RestController
//@RequestMapping("/personal")
//public class CoursePersonalController {
//
//    @Resource
//    private CourseStudentService courseStudentService;
//    @Resource
//    private CoursePersonalService coursePersonalService;
//    @Resource
//    private CourseCommentService courseCommentService;
//
//    /**
//     * 分页查询【我的课程】
//     */
//    @ApiOperation(value = "我的课程分页查询")
//    @PostMapping("/page")
//    public R<IPage<MyCoursePageDTO>> page(@RequestBody MyCoursePageSearchDTO params) {
//        params.setUserId(SecurityContextHolder.getUserId());
//        return R.ok(this.courseStudentService.pageMyCourse(params));
//    }
//
//    /**
//     * 查询课程详情
//     */
//    @ApiOperation(value = "查询课程详情")
//    @PostMapping("/detail")
//    public R<List<CourseLessonSimpleResponseDTO>> detail(@RequestBody List<MyCourseDetailRequestDTO> params) {
//        return R.ok(this.courseStudentService.detail(params));
//    }
//
//    /**
//     * 查询课程详情
//     */
//    @ApiOperation(value = "查询课程详情")
//    @PostMapping("/info")
//    public R<List<CourseDetailResponse>> info(@RequestBody List<MyCourseDetailRequestDTO> params) {
//        return R.ok(this.courseStudentService.info(params));
//    }
//
//    /**
//     * 课程学习情况收集
//     */
//    @ApiOperation(value = "课程学习情况收集")
//    @PostMapping("/recording")
//    public R<Boolean> recording(@RequestBody MyCourseStudyingDTO params) {
//        if (params.getUserId() == null) {
//            params.setUserId(SecurityContextHolder.getUserId());
//        }
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
//     * 直播学习情况收集
//     */
//    @ApiOperation(value = "直播课程学习情况收集")
//    @PostMapping("/liveRecording")
//    public R<Boolean> liveRecording(@RequestBody List<LiveCourseStudyingDTO> params) {
//        try {
//           this.coursePersonalService.LiveRecording(params);
//
//        } catch (Exception e) {
//            log.error("直播课程学习情况收集出错", e);
//        }
//        return R.ok(true);
//    }
//
//    /**
//     * 课程学习情况收集
//     */
//    @ApiOperation(value = "开始课程学习", hidden = true)
//    @PostMapping("/studying")
//    public R<Boolean> studying(@RequestBody MyCourseStudyingDTO params) {
//        if (params.getUserId() == null) {
//            params.setUserId(SecurityContextHolder.getUserId());
//        }
//        params.setOrgId(SecurityContextHolder.getOrgId());
//        this.coursePersonalService.studying(params);
//        return R.ok(true);
//    }
//
//    /**
//     * 分页查询【我的课程】
//     */
//    @ApiOperation(value = "我的课程评价查询")
//    @PostMapping("/comment")
//    public R<List<CourseCommentViewDTO>> commentList(@RequestBody CourseCommentRequest request) {
//        if (request.getUserId() == null) {
//            request.setUserId(SecurityContextHolder.getUserId());
//        }
//        ArrayList<CourseCommentViewDTO> list = new ArrayList<>();
//        this.courseCommentService.lambdaQuery()
//                .eq(CourseComment::getCourseId, request.getCourseId())
//                .eq(CourseComment::getCreateBy, request.getUserId())
//                .oneOpt()
//                .ifPresent(o -> {
//                    CourseCommentViewDTO viewDTO = new CourseCommentViewDTO();
//                    if (0 == o.getDeleted()) {
//                        BeanUtil.copyProperties(o, viewDTO);
//                    } else {
//                        // 学员评价被删除的情况下，也不允许再次评价
//                        viewDTO.setDeleted(o.getDeleted());
//                    }
//                    list.add(viewDTO);
//                });
//        return R.ok(list);
//    }
//
//}
