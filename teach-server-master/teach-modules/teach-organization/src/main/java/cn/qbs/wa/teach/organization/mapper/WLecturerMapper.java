package cn.qbs.wa.teach.organization.mapper;

import cn.qbs.wa.teach.organization.entity.WLecturer;
import cn.qbs.wa.teach.organization.pojo.wlecturer.WLecturerDetailResponse;
import cn.qbs.wa.teach.organization.pojo.wlecturer.WLecturerPageRequest;
import cn.qbs.wa.teach.organization.pojo.wlecturer.WLecturerPageResponse;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 插件-讲师表(WLecturer)表数据库访问层
 *
 * @author makejava
 * @since 2022-03-01 13:44:48
 */
public interface WLecturerMapper extends BaseMapper<WLecturer> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<WLecturer> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<WLecturer> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<WLecturer> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<WLecturer> entities);

    @InterceptorIgnore(tenantLine = "true")
    IPage<WLecturerPageResponse> page(@Param("page") IPage<?> page, @Param("params") WLecturerPageRequest params);

    WLecturerDetailResponse selectDetailById(Serializable id);

}

