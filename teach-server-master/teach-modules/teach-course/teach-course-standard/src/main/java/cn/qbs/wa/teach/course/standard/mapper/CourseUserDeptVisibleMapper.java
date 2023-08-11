package cn.qbs.wa.teach.course.standard.mapper;

import java.util.List;

import java.io.Serializable;

import cn.qbs.wa.teach.course.standard.pojo.course.CourseRemoveDeptRequest;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.teach.course.common.entity.CourseUserDeptVisible;
import cn.qbs.wa.teach.course.standard.pojo.courseuserdeptvisible.CourseUserDeptVisibleDetailResponse;
import cn.qbs.wa.teach.course.standard.pojo.courseuserdeptvisible.CourseUserDeptVisiblePageRequest;
import cn.qbs.wa.teach.course.standard.pojo.courseuserdeptvisible.CourseUserDeptVisiblePageResponse;

/**
 * 【课程可见学员部门】(CourseUserDeptVisible)表数据库访问层
 *
 * @author makejava
 * @since 2022-05-09 16:06:54
 */
public interface CourseUserDeptVisibleMapper extends BaseMapper<CourseUserDeptVisible> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<CourseUserDeptVisible> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<CourseUserDeptVisible> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<CourseUserDeptVisible> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<CourseUserDeptVisible> entities);

    IPage<CourseUserDeptVisiblePageResponse> page(@Param("page") IPage<?> page, @Param("params") CourseUserDeptVisiblePageRequest params);

    CourseUserDeptVisibleDetailResponse selectDetailById(Serializable id);

    @InterceptorIgnore(tenantLine = "true")
    List<CourseUserDeptVisible> listByCourseId(Long courseId);

    int deptBatchRemove(@Param("params") CourseRemoveDeptRequest params);
}

