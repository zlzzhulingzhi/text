package cn.qbs.wa.train.logistics.mapper;

import cn.qbs.wa.train.logistics.entity.ImportEmployee;
import cn.qbs.wa.train.logistics.pojo.importemployee.ImportEmployeeDetailResponse;
import cn.qbs.wa.train.logistics.pojo.importemployee.ImportEmployeePageRequest;
import cn.qbs.wa.train.logistics.pojo.importemployee.ImportEmployeePageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 导入职工表(ImportEmployee)表数据库访问层
 *
 * @author makejava
 * @since 2021-11-16 11:29:41
 */
public interface ImportEmployeeMapper extends BaseMapper<ImportEmployee> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<ImportEmployee> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<ImportEmployee> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<ImportEmployee> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<ImportEmployee> entities);

    IPage<ImportEmployeePageResponse> page(@Param("page") IPage<?> page, @Param("params") ImportEmployeePageRequest params);

    ImportEmployeeDetailResponse selectDetailById(Serializable id);

}

