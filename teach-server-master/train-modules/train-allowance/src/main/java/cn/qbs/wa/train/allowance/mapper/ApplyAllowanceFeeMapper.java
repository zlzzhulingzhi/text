package cn.qbs.wa.train.allowance.mapper;

import cn.qbs.wa.train.allowance.entity.ApplyAllowanceFee;
import cn.qbs.wa.train.allowance.pojo.applyallowancefee.ApplyAllowanceFeeDetailResponse;
import cn.qbs.wa.train.allowance.pojo.applyallowancefee.ApplyAllowanceFeePageRequest;
import cn.qbs.wa.train.allowance.pojo.applyallowancefee.ApplyAllowanceFeePageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 资助资金申请明细-网络安全培训费用(ApplyAllowanceFee)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-03 19:43:28
 */
public interface ApplyAllowanceFeeMapper extends BaseMapper<ApplyAllowanceFee> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<ApplyAllowanceFee> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<ApplyAllowanceFee> entities);
    
    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<ApplyAllowanceFee> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<ApplyAllowanceFee> entities);

    IPage<ApplyAllowanceFeePageResponse> page(@Param("page") IPage<?> page, @Param("params") ApplyAllowanceFeePageRequest params);

    ApplyAllowanceFeeDetailResponse selectDetailById(Serializable id);

    void copyByApplyId(@Param("sourceAllowanceId") Long sourceAllowanceId, @Param("targetAllowanceId") Long targetAllowanceId);
}

