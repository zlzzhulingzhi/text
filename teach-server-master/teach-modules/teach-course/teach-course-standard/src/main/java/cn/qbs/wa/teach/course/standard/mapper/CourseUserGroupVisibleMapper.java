package cn.qbs.wa.teach.course.standard.mapper;

import java.util.List;

import java.io.Serializable;

import cn.qbs.wa.teach.course.standard.pojo.course.CourseRemoveDeptRequest;
import cn.qbs.wa.teach.course.standard.pojo.course.CourseRemoveGroupRequest;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.teach.course.common.entity.CourseUserGroupVisible;
import cn.qbs.wa.teach.course.standard.pojo.courseusergroupvisible.CourseUserGroupVisibleDetailResponse;
import cn.qbs.wa.teach.course.standard.pojo.courseusergroupvisible.CourseUserGroupVisiblePageRequest;
import cn.qbs.wa.teach.course.standard.pojo.courseusergroupvisible.CourseUserGroupVisiblePageResponse;

/**
 * 【课程可见学员分组】(CourseUserGroupVisible)表数据库访问层
 *
 * @author makejava
 * @since 2022-05-09 16:07:59
 */
public interface CourseUserGroupVisibleMapper extends BaseMapper<CourseUserGroupVisible> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<CourseUserGroupVisible> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<CourseUserGroupVisible> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<CourseUserGroupVisible> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<CourseUserGroupVisible> entities);

    IPage<CourseUserGroupVisiblePageResponse> page(@Param("page") IPage<?> page, @Param("params") CourseUserGroupVisiblePageRequest params);

    CourseUserGroupVisibleDetailResponse selectDetailById(Serializable id);

    @InterceptorIgnore(tenantLine = "true")
    List<CourseUserGroupVisible> listByCourseId(Long courseId);

    int groupBatchRemove(@Param("params") CourseRemoveGroupRequest params);
}

