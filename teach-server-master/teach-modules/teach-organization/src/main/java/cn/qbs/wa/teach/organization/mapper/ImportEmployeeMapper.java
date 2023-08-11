package cn.qbs.wa.teach.organization.mapper;

import java.util.List;

import java.io.Serializable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.teach.organization.entity.ImportEmployee;
import cn.qbs.wa.teach.organization.pojo.importemployee.ImportEmployeeDetailResponse;
import cn.qbs.wa.teach.organization.pojo.importemployee.ImportEmployeePageRequest;
import cn.qbs.wa.teach.organization.pojo.importemployee.ImportEmployeePageResponse;

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

