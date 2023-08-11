package cn.qbs.wa.teach.course.standard.mapper;

import java.util.List;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.teach.course.common.entity.CourseLecturer;
import cn.qbs.wa.teach.course.standard.pojo.courselecturer.CourseLecturerDetailResponse;
import cn.qbs.wa.teach.course.standard.pojo.courselecturer.CourseLecturerPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.courselecturer.CourseLecturerPageResponse;

/**
 * 【课程讲师】(CourseLecturer)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-11-15 14:50:38
 */
public interface CourseLecturerMapper extends BaseMapper<CourseLecturer> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<CourseLecturer> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<CourseLecturer> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<CourseLecturer> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<CourseLecturer> entities);

    IPage<CourseLecturerPageResponse> page(@Param("page") IPage<?> page, @Param("params") CourseLecturerPageRequest params);

    @InterceptorIgnore(tenantLine = "true")
    CourseLecturerDetailResponse selectDetailById(Serializable id);

    @InterceptorIgnore(tenantLine = "true")
    List<CourseLecturer> listByCourseId(Long courseId);

    @InterceptorIgnore(tenantLine = "true")
    List<CourseLecturer> listByCourseName(String courseName);
}

