package cn.qbs.wa.teach.course.standard.mapper;

import cn.qbs.wa.teach.course.common.entity.CourseLiveLink;
import cn.qbs.wa.teach.course.standard.pojo.courselivelink.CourseLiveLinkDetailResponse;
import cn.qbs.wa.teach.course.standard.pojo.courselivelink.CourseLiveLinkPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.courselivelink.CourseLiveLinkPageResponse;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 【课程讲义】(CourseLiveLink)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2021-12-29 14:43:50
 */
public interface CourseLiveLinkMapper extends BaseMapper<CourseLiveLink> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<CourseLiveLink> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<CourseLiveLink> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<CourseLiveLink> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<CourseLiveLink> entities);

    IPage<CourseLiveLinkPageResponse> page(@Param("page") IPage<?> page, @Param("params") CourseLiveLinkPageRequest params);

    CourseLiveLinkDetailResponse selectDetailById(@Param("id") Serializable id);

    CourseLiveLinkDetailResponse detailByCourseId(@Param("courseId") Long courseId);

    @InterceptorIgnore(tenantLine = "true")
    List<CourseLiveLink> selectListWithOutOrg(@Param("courseIdList") List<Long> courseIdList);
}

