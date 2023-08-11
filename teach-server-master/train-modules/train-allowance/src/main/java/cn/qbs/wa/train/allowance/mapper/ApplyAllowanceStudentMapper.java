package cn.qbs.wa.train.allowance.mapper;

import cn.qbs.wa.train.allowance.entity.ApplyAllowanceStudent;
import cn.qbs.wa.train.allowance.pojo.applyallowancestudent.ApplyAllowanceStudentDetailResponse;
import cn.qbs.wa.train.allowance.pojo.applyallowancestudent.ApplyAllowanceStudentPageRequest;
import cn.qbs.wa.train.allowance.pojo.applyallowancestudent.ApplyAllowanceStudentPageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 资助资金申请明细-网络安全培训学员(ApplyAllowanceStudent)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-03 19:30:24
 */
public interface ApplyAllowanceStudentMapper extends BaseMapper<ApplyAllowanceStudent> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ApplyAllowanceStudent> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ApplyAllowanceStudent> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ApplyAllowanceStudent> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<ApplyAllowanceStudent> entities);

    IPage<ApplyAllowanceStudentPageResponse> page(@Param("page") IPage<?> page,
                                                  @Param("params") ApplyAllowanceStudentPageRequest params);

    ApplyAllowanceStudentDetailResponse selectDetailById(Serializable id);

    void copyByApplyId(@Param("sourceAllowanceId") Long sourceAllowanceId, @Param("targetAllowanceId") Long targetAllowanceId);
}

