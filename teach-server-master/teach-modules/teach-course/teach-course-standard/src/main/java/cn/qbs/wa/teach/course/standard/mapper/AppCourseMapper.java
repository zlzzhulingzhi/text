package cn.qbs.wa.teach.course.standard.mapper;

import cn.qbs.wa.teach.course.standard.pojo.dto.CourseCommentViewDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.app.PageCommentDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.app.PageCourseDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseChapterDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.CourseInfoDTO;
import cn.qbs.wa.teach.course.standard.pojo.dto.app.PageCourseVO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * App端课程数据库访问层
 *
 * @author yjx
 */
public interface AppCourseMapper {

    /**
     * (内训)课程中心-分页列表
     * @param page 分页参数
     * @param params 查询参数
     * @return 课程列表
     */
    IPage<PageCourseVO> pageInnerCourse(Page<PageCourseVO> page,  @Param("params") PageCourseDTO params);

    /**
     * (内训)课程中心-分页列表
     * @param page 分页参数
     * @param params 查询参数
     * @return 课程列表
     */
    IPage<PageCourseVO> pageInnerCourseSignUp(Page<PageCourseVO> page, @Param("params") PageCourseDTO params);

    CourseInfoDTO selectDetailByCourseId(Long courseId);

    List<CourseChapterDTO> selectContentById(Long courseId);

    @InterceptorIgnore(tenantLine = "true")
    List<CourseChapterDTO> shopSelectContentById(Long courseId);

    IPage<CourseCommentViewDTO> pageComment(Page<?> page, @Param("params") PageCommentDTO params);
}
