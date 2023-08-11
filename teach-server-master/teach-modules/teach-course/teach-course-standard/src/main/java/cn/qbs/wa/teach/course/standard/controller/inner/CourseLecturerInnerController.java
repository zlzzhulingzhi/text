package cn.qbs.wa.teach.course.standard.controller.inner;

import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.constant.SecurityConstants;
import cn.qbs.wa.teach.common.core.domain.R;

import cn.qbs.wa.teach.common.security.annotation.AccessAuth;
import cn.qbs.wa.teach.course.common.entity.CourseLecturer;
import cn.qbs.wa.teach.course.standard.pojo.course.CourseListRequest;
import cn.qbs.wa.teach.course.standard.pojo.courselecturer.CourseLecturerPageRequest;
import cn.qbs.wa.teach.course.standard.service.CourseLecturerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 【课程讲师】(CourseLecturer)表控制层
 *
 * @author makejava
 * @since 2022-11-08 10:29:09
 */
@Api(tags = "课程讲师内部远程接口")
@RestController
@RequestMapping("inner/courseLecturer")
public class CourseLecturerInnerController {

    /**
     * 服务对象
     */
    @Resource
    private CourseLecturerService courseLecturerService;

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("count")
    @ApiOperation("获取讲师与课程关联数量")
    public R<Long> count(@RequestBody CourseLecturerPageRequest request) {
        Long count=courseLecturerService.lambdaQuery().eq(CourseLecturer::getLecturerId,request.getLecturerId()).count();
        return R.ok(count);
    }

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("/queryLecturerIds")
    R<List<Long>> queryLecturerIds(@RequestBody CourseListRequest request) {
        List<CourseLecturer> list = courseLecturerService.listByCourseName(request.getCourseName());
        if (CollUtil.isEmpty(list)) {
            return R.ok(Collections.emptyList());
        }
        return R.ok(list.stream().map(CourseLecturer::getLecturerId).distinct().collect(Collectors.toList()));
    }
}

