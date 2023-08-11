package cn.qbs.wa.teach.course.standard.mapper;

import java.util.List;

import java.io.Serializable;

import cn.qbs.wa.teach.course.standard.pojo.dto.CourseCategoryDTO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.teach.course.common.entity.CourseCategory;
import cn.qbs.wa.teach.course.standard.pojo.coursecategory.CourseCategoryDetailResponse;
import cn.qbs.wa.teach.course.standard.pojo.coursecategory.CourseCategoryPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.coursecategory.CourseCategoryPageResponse;

/**
 * 【课程分类关联关系】(CourseCategory)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:37
 */
public interface CourseCategoryMapper extends BaseMapper<CourseCategory> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<CourseCategory> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<CourseCategory> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<CourseCategory> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<CourseCategory> entities);

    IPage<CourseCategoryPageResponse> page(@Param("page") IPage<?> page, @Param("params") CourseCategoryPageRequest params);

    @InterceptorIgnore(tenantLine = "true")
    CourseCategoryDetailResponse selectDetailById(Serializable id);

    @InterceptorIgnore(tenantLine = "true")
    List<CourseCategoryDTO> listByCourseId(Long courseId);

    Long categoryCount(@Param("idList") List<Long> idList, @Param("lecturerId") Long lecturerId, @Param("studentId") Long studentId);
}

