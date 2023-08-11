package cn.qbs.wa.union.auth.mapper;

import cn.qbs.wa.union.auth.entity.UniApplication;
import cn.qbs.wa.union.auth.pojo.uniapplication.UniApplicationDetailResponse;
import cn.qbs.wa.union.auth.pojo.uniapplication.UniApplicationPageRequest;
import cn.qbs.wa.union.auth.pojo.uniapplication.UniApplicationPageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 统一应用展示(UniApplication)表数据库访问层
 *
 * @author makejava
 * @since 2022-07-08 09:03:09
 */
public interface UniApplicationMapper extends BaseMapper<UniApplication> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<UniApplication> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<UniApplication> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<UniApplication> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<UniApplication> entities);

    IPage<UniApplicationPageResponse> page(@Param("page") IPage<?> page, @Param("params") UniApplicationPageRequest params);

    UniApplicationDetailResponse selectDetailById(Serializable id);

}

