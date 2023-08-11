package cn.qbs.wa.teach.course.standard.mapper;

import cn.qbs.wa.teach.course.common.entity.Course;
import cn.qbs.wa.teach.course.standard.pojo.course.*;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseChapterDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseListSearchDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.CoursePageSearchDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.CoursePageViewDTO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 【课程】(Course)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:36
 */
public interface CourseMapper extends BaseMapper<Course> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Course> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Course> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Course> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Course> entities);

    /**
     * 课程分页
     *
     * @param page   分页对象
     * @param params 查询参数
     * @return
     */
    IPage<CoursePageResponse> page(@Param("page") IPage<?> page, @Param("params") CoursePageRequest params);

    /**
     * 部门下的课程分页
     *
     * @param page   分页对象
     * @param params 查询参数
     * @return
     */
    IPage<CoursePageResponse> pageByDept(@Param("page") IPage<?> page, @Param("params") CoursePageRequest params);

    /**
     * 部门下的课程分页
     *
     * @param page   分页对象
     * @param params 查询参数
     * @return
     */
    IPage<CoursePageResponse> pageByDeptAndLecturer(Page<?> page, @Param("params") CoursePageRequest params);

    /**
     * 标签下的课程分页
     *
     * @param page   分页对象
     * @param params 查询参数
     * @return
     */
    IPage<CoursePageResponse> pageByGroup(@Param("page") IPage<?> page, @Param("params") CoursePageRequest params);

    /**
     * 标签下的课程分页
     *
     * @param page   分页对象
     * @param params 查询参数
     * @return
     */
    IPage<CoursePageResponse> pageByGroupAndLecturer(Page<?> page, @Param("params") CoursePageRequest params);

    /**
     * 查询课程详细信息
     *
     * @param id 课程ID
     * @return 课程详细信息
     */
    @InterceptorIgnore(tenantLine = "true")
    CourseDetailResponse selectDetailById(@Param("id") Serializable id);

    @InterceptorIgnore(tenantLine = "true")
    CourseDetailResponse getCourseName(@Param("id") Serializable id);

    @InterceptorIgnore(tenantLine = "true")
    List<CourseDetailResponse> getCourse(@Param("params")CourseListRequest params);

    /**
     * 商城远程接口
     * */
    @InterceptorIgnore(tenantLine = "true")
    Course shopGetCourseById(@Param("id") Serializable id);

    /**
     * 查询课程详细信息
     *
     * @param id 课程ID
     * @return 课程章节内容信息
     */
    @InterceptorIgnore(tenantLine = "true")
    List<CourseChapterDTO> selectContentById(@Param("id") Long id);

    /**
     * 查询课程详细信息
     *
     * @param id 课程ID
     * @return 课程详细信息
     */
    @InterceptorIgnore(tenantLine = "true")
    Course selectByIdWithoutOrg(Long id);

    /**
     * (内训)课程中心-分页列表
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return 课程列表
     */
    IPage<CoursePageViewDTO> pageInnerCourse(Page<?> page, @Param("params") CoursePageSearchDTO params);

    /**
     * (内训)课程中心-分页列表
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return 课程列表
     */
    IPage<CoursePageViewDTO> pageInnerCourseSignUp(Page<?> page, @Param("params") CoursePageSearchDTO params);

    /**
     * (外训)课程中心-分页列表
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return 课程列表
     */
    @InterceptorIgnore(tenantLine = "true")
    IPage<CoursePageViewDTO> pagePublishCourse(Page<?> page, @Param("params") CoursePageSearchDTO params);

    IPage<CoursePageViewDTO> pageRecommendCourse(Page<?> mpPage, @Param("params") CourseListSearchDTO params);

    IPage<CoursePageViewDTO> pageCourseByIndex(Page<?> mpPage, @Param("params") CoursePageIndexRequest params);

    IPage<CoursePageByChildResponse> pageByChild(Page<?> mpPage, @Param("params") CoursePageRequest params);

    @InterceptorIgnore(tenantLine = "true")
    IPage<CoursePageResponse> pageAdmin(Page<Object> mpPage, CoursePageRequest params);

    List<CourseListResponse> listSelect(@Param("params") CourseListRequest params);

    @InterceptorIgnore(tenantLine = "true")
    List<CourseListResponse> shopListSelect(@Param("params") CourseListRequest params);

    IPage<CoursePageViewDTO> pageInnerCourseByIndex(Page<Object> mpPage, CoursePageIndexRequest params);

    IPage<CoursePageByChildResponse> pageInnerByChild(Page<Object> mpPage, CoursePageRequest params);

    List<CourseChapterDTO> selectCatalogById(@Param("courseId") Long courseId);

    IPage<CoursePageResponse> pageByLecturer(Page<?> page, @Param("params") CoursePageRequest params);

    IPage<CoursePageByLecturerResponse> myCoursePage(Page<?> page, @Param("params") CoursePageByLecturerRequest params);

    Long deptCount(@Param("deptIdList") List<Long> deptIdList);

    Long groupCount(@Param("groupIdList") List<Long> groupIdList);

    @InterceptorIgnore(tenantLine = "true")
    IPage<CoursePageResponse> shopCoursePageByLecturer(Page<?> page, @Param("params") CoursePageRequest params);
    @InterceptorIgnore(tenantLine = "true")
    IPage<CoursePageResponse> shopCoursePage(@Param("page") IPage<?> page, @Param("params") CoursePageRequest params);

    IPage<CoursePageResponse> getOrgList(@Param("page") IPage<?> page, @Param("params") CoursePageRequest params);

    @InterceptorIgnore(tenantLine = "true")
    void updateShelfStatus(@Param("entity") Course entity);
}

