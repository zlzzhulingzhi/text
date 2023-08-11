package cn.qbs.wa.teach.exam.admin.mapper;

import java.util.List;

import java.io.Serializable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.teach.exam.common.entity.TCertRule;
import cn.qbs.wa.teach.exam.admin.pojo.tcertrule.TCertRuleDetailResponse;
import cn.qbs.wa.teach.exam.admin.pojo.tcertrule.TCertRulePageRequest;
import cn.qbs.wa.teach.exam.admin.pojo.tcertrule.TCertRulePageResponse;

/**
 * 证书规则表(TCertRule)表数据库访问层
 *
 * @author makejava
 * @since 2022-05-16 13:49:20
 */
public interface TCertRuleMapper extends BaseMapper<TCertRule> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<TCertRule> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<TCertRule> entities);
    
    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<TCertRule> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<TCertRule> entities);

    IPage<TCertRulePageResponse> page(@Param("page") IPage<?> page, @Param("params") TCertRulePageRequest params);

    TCertRuleDetailResponse selectDetailById(Serializable id);
    
}

