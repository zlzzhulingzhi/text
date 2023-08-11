package cn.qbs.wa.teach.course.standard.controller.manage;


import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.course.standard.pojo.coursestudentlesson.CourseStudentLessonUpdateRequest;
import cn.qbs.wa.teach.course.standard.service.CourseStudentLessonService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;


/**
 * 【学员课程课次信息】(CourseStudentLesson)表控制层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-12-07 14:52:09
 */
@ApiIgnore
@RestController
@RequestMapping("/student/lesson")
public class CourseStudentLessonController {

    /**
     * 服务对象
     */
    @Resource
    private CourseStudentLessonService courseStudentLessonService;


    /**
     * 修改【学员课程课次信息】
     *
     * @param params
     * @return
     */
    @PostMapping("/update")
    public R<Boolean> update(@RequestBody @Validated CourseStudentLessonUpdateRequest params) {
        return R.ok(this.courseStudentLessonService.update(params));
    }


}

