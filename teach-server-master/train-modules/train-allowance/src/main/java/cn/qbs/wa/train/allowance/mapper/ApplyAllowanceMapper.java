package cn.qbs.wa.train.allowance.mapper;

import cn.qbs.wa.train.allowance.entity.ApplyAllowance;
import cn.qbs.wa.train.allowance.entity.StatCourseRecord;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyPageResponse;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyRequest;
import cn.qbs.wa.train.allowance.pojo.applyallowance.ApplyAllowanceDetailResponse;
import cn.qbs.wa.train.allowance.pojo.approve.ApprovePageRequest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 资助资金申请-网络安全培训课程(ApplyAllowance)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-03 19:28:56
 */
public interface ApplyAllowanceMapper extends BaseMapper<ApplyAllowance> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ApplyAllowance> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ApplyAllowance> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ApplyAllowance> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<ApplyAllowance> entities);

    IPage<ApplyPageResponse> page(@Param("page") IPage<?> page, @Param("params") ApplyRequest applyRequest);

    ApplyAllowanceDetailResponse selectDetailById(Serializable id);

    List<StatCourseRecord> listPassAllowanceFee(Long org);

    List<StatCourseRecord> listPassAllowanceLesson(Long org);

    List<ApplyPageResponse> pageList(@Param("pageIdx") long pageIdx, @Param("pageSize") long pageSize, @Param("params") ApprovePageRequest applyRequest);

    IPage<ApplyPageResponse> pageV2(@Param("page") IPage<?> page, @Param("params") ApprovePageRequest applyRequest);
}

