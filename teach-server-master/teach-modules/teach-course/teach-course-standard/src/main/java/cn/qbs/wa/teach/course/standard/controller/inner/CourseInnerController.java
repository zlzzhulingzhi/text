package cn.qbs.wa.teach.course.standard.controller.inner;

import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.common.core.constant.SecurityConstants;
import cn.qbs.wa.teach.common.core.context.SecurityContextHolder;
import cn.qbs.wa.teach.common.core.domain.IdListRequest;
import cn.qbs.wa.teach.common.core.domain.IdOrgRequest;
import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.common.core.domain.R;
import cn.qbs.wa.teach.common.security.annotation.AccessAuth;
import cn.qbs.wa.teach.course.common.entity.Course;
import cn.qbs.wa.teach.course.common.entity.CourseLesson;
import cn.qbs.wa.teach.course.common.entity.CourseStudent;
import cn.qbs.wa.teach.course.standard.pojo.category.CategoryListRequest;
import cn.qbs.wa.teach.course.standard.pojo.category.CategoryListResponse;
import cn.qbs.wa.teach.course.standard.pojo.course.*;
import cn.qbs.wa.teach.course.standard.pojo.courseinfo.CourseInfoDetailResponse;
import cn.qbs.wa.teach.course.standard.pojo.coursestudent.CourseStudentAddRequest;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseChapterDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseInfoDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.SignUpDTO;
import cn.qbs.wa.teach.course.standard.service.*;
import cn.qbs.wa.teach.organization.api.RemoteTeacherOrganizationService;
import cn.qbs.wa.teach.organization.api.RemoteStudentService;
import cn.qbs.wa.teach.organization.api.pojo.DTO.organization.OrganizationListResultDTO;
import cn.qbs.wa.teach.organization.api.pojo.DTO.student.StudentDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 【课程】(Course)表控制层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-16 11:06:52
 */
@Slf4j
@Api(tags = "课程内部远程接口")
@RestController
@RequestMapping("inner")
public class CourseInnerController {

    @Resource
    private CourseInnerService courseInnerService;

    @Resource
    private RemoteStudentService remoteStudentService;

    @Resource
    private CourseStudentService courseStudentService;

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("/sign-up")
    public R<Boolean> signUp(@RequestBody @Validated SignUpDTO signUpDTO) {

        // 先根据课程ID查询课程信息
        Course course = courseInnerService.getById(signUpDTO.getCourseId());
        if (course == null) {
            return R.fail("抱歉，课程已下架");
        }

        Long userId = signUpDTO.getUserId();
        // 存在的情况下，无需再次新增
        Long courseId = course.getId();
        CourseStudent one = this.courseStudentService.lambdaQuery().eq(CourseStudent::getCourseId, courseId).eq(CourseStudent::getUserId, userId).one();
        if (one != null) {
            return R.ok(true);
        }

        Long orgId = course.getOrgId();
        // 确认该课程所属机构是否存在当前 user_id 学员，没有则注册新学员
        StudentDTO studentDTO = BeanUtil.copyProperties(signUpDTO, StudentDTO.class);
        studentDTO.setOrgId(orgId);
        R<StudentDTO> r = remoteStudentService.registerFromInner(studentDTO);
        if (!r.isOk()) {
            log.error("registerFromInner 接口异常 {}", r.getMsg());
            return R.fail(r.getMsg());
        }

        CourseStudentAddRequest params = new CourseStudentAddRequest();
        params.setCourseId(courseId);
        params.setOrgId(orgId);
        params.setUserId(userId);
        params.setStudentId(r.getData().getId());
        return R.ok(this.courseStudentService.add(params));
    }

    /**
     * 查询课程基础信息
     */
    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("/info")
    public R<CourseInfoDetailResponse> courseInfo(@RequestBody IdRequest request) {
        return R.ok(this.courseInnerService.courseInfo(request.getId()));
    }

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("/getIdList")
    public R<List<Long>> getIdByCourseName(@RequestBody CoursePageRequest params) {
        return R.ok(this.courseInnerService.getIdByCourseName(params));
    }

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("/list")
    public R<List<Course>> listByIds(@RequestBody IdListRequest params) {
        return R.ok(this.courseInnerService.listByIds(params.getIdList()));
    }

    @AccessAuth({SecurityConstants.INNER})
    @PostMapping("/getCourseLesson")
    public R<List<CourseLesson>> getCourseLesson(@RequestBody IdRequest request) {
        return R.ok(this.courseInnerService.getCourseLesson(request));
    }

}

