package cn.qbs.wa.teach.course.standard.mapper;

import java.util.List;

import java.io.Serializable;

import cn.qbs.wa.teach.course.standard.pojo.coursecomponentattachment.CourseComponentAttachmentDetailResponse;
import cn.qbs.wa.teach.course.standard.pojo.coursestudent.CourseStudentQuery;
import cn.qbs.wa.teach.course.standard.pojo.dto.*;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.teach.course.common.entity.CourseStudent;
import cn.qbs.wa.teach.course.standard.pojo.coursestudent.CourseStudentDetailResponse;
import cn.qbs.wa.teach.course.standard.pojo.coursestudent.CourseStudentPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.coursestudent.CourseStudentPageResponse;

/**
 * 【课程学员】(CourseStudent)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:40
 */
public interface CourseStudentMapper extends BaseMapper<CourseStudent> {

    IPage<CourseStudentPageResponse> page(@Param("page") IPage<?> page, @Param("params") CourseStudentPageRequest params);

    @InterceptorIgnore(tenantLine = "true")
    IPage<CourseStudentPageResponse> pageV2(@Param("page") IPage<?> page, @Param("params") CourseStudentPageRequest params);

    CourseStudentDetailResponse selectDetailById(Serializable id);

    /**
     * 取得课程学员列表数据
     * @param params 请求参数
     * @return 该课程的学员信息
     */
    List<CourseStudentPageResponse> getCourseStudentList(@Param("params") CourseStudentPageRequest params);

    /**
     * 我的课程分页查询
     * @param mpPage
     * @param params
     * @return
     */
    IPage<MyCoursePageDTO> pageMyCourse(Page<?> mpPage, @Param("params") MyCoursePageSearchDTO params);

    /**
     * 根据用户ID，课程ID查询课程最近一条学习记录
     * @param userId
     * @param courseId
     * @return
     */
    CourseLessonSimpleDTO getLastCourseStudyRecord(@Param("userId") Long userId, @Param("courseId") Long courseId);

    List<CourseComponentAttachmentDetailResponse> getLiveId(MyCoursePageDTO myCourse);

    List<CourseLessonSimpleResponseDTO> getLastCourseStudyRecordById(@Param("params") List<MyCourseDetailRequestDTO> params, @Param("userId") Long userId);

    @InterceptorIgnore(tenantLine = "true")
    List<CourseStudent> queryStudentByPlat(@Param("params") CourseStudentQuery query);
}

