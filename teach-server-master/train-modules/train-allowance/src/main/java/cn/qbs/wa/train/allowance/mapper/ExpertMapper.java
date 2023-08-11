package cn.qbs.wa.train.allowance.mapper;

import cn.qbs.wa.train.allowance.entity.Expert;
import cn.qbs.wa.train.allowance.pojo.expert.ExpertDetailResponse;
import cn.qbs.wa.train.allowance.pojo.expert.ExpertPageRequest;
import cn.qbs.wa.train.allowance.pojo.expert.ExpertPageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 专家信息(Expert)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-31 18:47:28
 */
public interface ExpertMapper extends BaseMapper<Expert> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Expert> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Expert> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Expert> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Expert> entities);

    IPage<ExpertPageResponse> page(@Param("page") IPage<?> page, @Param("params") ExpertPageRequest params);

    ExpertDetailResponse selectDetailById(Serializable id);

}

