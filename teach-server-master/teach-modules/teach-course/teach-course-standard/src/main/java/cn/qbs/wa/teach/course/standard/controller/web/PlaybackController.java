//package cn.qbs.wa.teach.course.standard.controller.web;
//
//import cn.qbs.wa.teach.common.core.domain.IdRequest;
//import cn.qbs.wa.teach.common.core.domain.R;
//import cn.qbs.wa.teach.course.common.entity.CourseStudentStudyExactRecord;
//import cn.qbs.wa.teach.course.standard.pojo.dto.PlaybackCourseStudyingDTO;
//import cn.qbs.wa.teach.course.standard.pojo.playback.PlaybackStatistic;
//import cn.qbs.wa.teach.course.standard.pojo.playback.WatchPlaybackStudentPageRequest;
//import cn.qbs.wa.teach.course.standard.pojo.playback.WatchPlaybackStudentPageResponse;
//import cn.qbs.wa.teach.course.standard.service.CourseStudentStudyExactRecordService;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//
///**
// * 回放
// * @Author zcm
// * @Date 2022/6/23 16:56
// * @Version 1.0
// */
//@RestController
//@RequestMapping("/playback")
//public class PlaybackController {
//
//    @Resource
//    private CourseStudentStudyExactRecordService courseStudentStudyExactRecordService;
//
//
//    /**
//     * 记录回放观看记录
//     * @param params
//     * @return
//     */
//    @ApiOperation(value = "记录回放观看记录")
//    @PostMapping("/recording")
//    public R<CourseStudentStudyExactRecord> statistic(@RequestBody @Validated PlaybackCourseStudyingDTO params) {
//        return R.ok(this.courseStudentStudyExactRecordService.addOrUpdate(params));
//    }
//
//    /**
//     * 统计回放观看数据
//     *
//     * @param params
//     * @return
//     */
//    @PostMapping("/statistic")
//    @ApiOperation("统计回放观看数据")
//    public R<PlaybackStatistic> statistic(@RequestBody @Validated IdRequest params) {
//        return R.ok(this.courseStudentStudyExactRecordService.getStatisticData(params.getId()));
//    }
//
//    /**
//     * 分页查询观看回放学员
//     * @param params
//     * @return
//     */
//    @PostMapping("/watchLiveStudentPage")
//    @ApiOperation("分页查询观看回放学员")
//    public R<IPage<WatchPlaybackStudentPageResponse>> watchLiveStudentPage(@RequestBody @Validated WatchPlaybackStudentPageRequest params) {
//        return R.ok(this.courseStudentStudyExactRecordService.watchPlaybackStudentPage(params));
//    }
//
//}
