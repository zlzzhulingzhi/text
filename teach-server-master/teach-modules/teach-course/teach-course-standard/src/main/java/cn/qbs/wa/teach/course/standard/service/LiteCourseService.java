package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.course.standard.pojo.dto.app.PageCourseDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.lite.*;
import cn.qbs.wa.teach.course.standard.pojo.tcoursestudent.TCourseStudentPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.tcoursestudent.TCourseStudentResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 课程小程序接口
 */
public interface LiteCourseService {

    IPage<MyCoursePageResponse> pageMyCourse(MyCoursePageRequest params);

    IPage<PageCourseVO> pageCourse(PageCourseDTO params);

    CourseInfoDTO info(Long courseId);

    Boolean checkCourseSignUp(Long courseId, Long memberId);

    Boolean signUp(SignUpDTO request);

    CourseInfoDTO courseInfo(Long courseId);

    IPage<PageCourseVO> orgPageCourse(PageCourseDTO request);

    IPage<TCourseStudentResponse> orgPagePreStudent(TCourseStudentPageRequest request);
}
