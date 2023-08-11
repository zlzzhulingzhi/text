package cn.qbs.wa.train.logistics.mapper;

import java.util.List;

import java.io.Serializable;

import cn.qbs.wa.train.logistics.entity.Clazz;
import cn.qbs.wa.train.logistics.pojo.clazzstudent.ClazzStudentMap;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.train.logistics.entity.ClazzStudent;
import cn.qbs.wa.train.logistics.pojo.clazzstudent.ClazzStudentDetailResponse;
import cn.qbs.wa.train.logistics.pojo.clazzstudent.ClazzStudentPageRequest;
import cn.qbs.wa.train.logistics.pojo.clazzstudent.ClazzStudentPageResponse;

/**
 * 班级学员表(ClazzStudent)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-08 17:28:14
 */
public interface ClazzStudentMapper extends BaseMapper<ClazzStudent> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ClazzStudent> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ClazzStudent> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ClazzStudent> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<ClazzStudent> entities);

    IPage<ClazzStudentPageResponse> page(@Param("page") IPage<?> page, @Param("params") ClazzStudentPageRequest params);

    ClazzStudentDetailResponse selectDetailById(Serializable id);

    /**
     * 根据会员用户ID会最近开班的班级
     * @param memberIds 会员用户ID
     * @return 班级信息
     */
    List<ClazzStudentMap> queryClazzLast(@Param("memberIds") List<Long> memberIds);
}

