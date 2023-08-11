package cn.qbs.wa.train.logistics.mapper;

import cn.qbs.wa.train.logistics.entity.Lecturer;
import cn.qbs.wa.train.logistics.pojo.lecturer.*;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 讲师表(Lecturer)表数据库访问层
 *
 * @author makejava
 * @since 2021-11-17 11:25:32
 */
public interface LecturerMapper extends BaseMapper<Lecturer> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<Lecturer> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<Lecturer> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<Lecturer> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<Lecturer> entities);

    IPage<LecturerPageResponse> page(@Param("page") IPage<?> page, @Param("params") LecturerPageRequest params);

    LecturerDetailResponse selectDetailById(Serializable id);

    List<LecturerListResponse> listLecture(LecturerListRequest request);

    @InterceptorIgnore(tenantLine = "true")
    List<LecturerListResponse> listLectureWithOutOrgId(LecturerListRequest lecturerListRequest);

    @InterceptorIgnore(tenantLine = "true")
    IPage<LecturerPageResponse> pageAdmin(Page<Object> mpPage, LecturerPageRequest params);
}

