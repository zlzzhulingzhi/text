package cn.qbs.wa.teach.course.standard.mapper;

import cn.qbs.wa.teach.course.standard.entity.TCourse;
import cn.qbs.wa.teach.course.standard.pojo.tcourse.TCoursePageRequest;
import cn.qbs.wa.teach.course.standard.pojo.tcourse.TCoursePageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 课程-报名分享(TCourse)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2022-10-24 19:09:01
 */
public interface TCourseMapper extends BaseMapper<TCourse> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<TCourse> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<TCourse> entities);
    
    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<TCourse> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<TCourse> entities);

    List<TCoursePageResponse> listCourse(@Param("params")TCoursePageRequest params);

}

