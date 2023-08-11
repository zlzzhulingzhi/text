package cn.qbs.wa.train.logistics.mapper;

import cn.qbs.wa.train.logistics.entity.MatterReportAttach;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 报事报修-附件(MatterReportAttach)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2022-11-24 15:31:34
 */
public interface MatterReportAttachMapper extends BaseMapper<MatterReportAttach> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<MatterReportAttach> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<MatterReportAttach> entities);
    
    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<MatterReportAttach> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<MatterReportAttach> entities);
    
}

