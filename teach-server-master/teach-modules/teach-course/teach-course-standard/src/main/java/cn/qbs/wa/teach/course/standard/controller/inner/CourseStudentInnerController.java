package cn.qbs.wa.teach.course.standard.controller.inner;

import cn.hutool.core.collection.CollUtil;
import cn.qbs.wa.teach.common.core.constant.SecurityConstants;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.security.annotation.AccessAuth;
import cn.qbs.wa.teach.course.common.entity.CourseStudent;
import cn.qbs.wa.teach.course.standard.pojo.coursestudent.CourseStudentQuery;
import cn.qbs.wa.teach.course.standard.service.CourseStudentService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 【课程】(Course)表控制层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-16 11:06:52
 */
@Slf4j
@Api(tags = "课程学生内部远程接口")
@RestController
@RequestMapping("inner")
public class CourseStudentInnerController {

    @Resource
    private CourseStudentService courseStudentService;


    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("/getCourseStudent")
    public R<List<CourseStudent>> getCourseStudent(@RequestBody IdRequest request) {
        List<CourseStudent> courseStudentList=courseStudentService.lambdaQuery().eq(CourseStudent::getCourseId,request.getId()).list();
        return R.ok(courseStudentList);
    }
    /**
     * 根据学员用户ID数组、课程名称过滤符合条件的 memberIds
     */
    @PostMapping("/queryStudent")
    R<List<Long>> queryStudent(@RequestBody CourseStudentQuery query) {
        List<CourseStudent> courseStudents = courseStudentService.queryStudentByPlat(query);
        if (CollUtil.isNotEmpty(courseStudents)) {
            return R.ok(courseStudents.stream().map(CourseStudent::getUserId).distinct().collect(Collectors.toList()));
        }
        return R.ok(Collections.emptyList());
    }
}

