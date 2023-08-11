package cn.qbs.wa.train.allowance.mapper;

import cn.qbs.wa.train.allowance.entity.ApplyActivity;
import cn.qbs.wa.train.allowance.entity.StatActivityRecord;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyPageResponse;
import cn.qbs.wa.train.allowance.pojo.apply.ApplyRequest;
import cn.qbs.wa.train.allowance.pojo.applyactivity.ApplyActivityDetailResponse;
import cn.qbs.wa.train.allowance.pojo.approve.ApprovePageRequest;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 资助资金申请-学术会议和竞赛活动(ApplyActivity)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-03 19:27:14
 */
public interface ApplyActivityMapper extends BaseMapper<ApplyActivity> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ApplyActivity> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ApplyActivity> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ApplyActivity> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<ApplyActivity> entities);

    IPage<ApplyPageResponse> page(@Param("page") IPage<?> page, @Param("params") ApplyRequest applyRequest);

    ApplyActivityDetailResponse selectDetailById(Serializable id);

    List<StatActivityRecord> listPassActivity(Long org);

    long pageCount(@Param("params") ApprovePageRequest request);

    List <ApplyPageResponse> pageList(@Param("pageIdx") long pageIdx, @Param("pageSize") long pageSize, @Param("params") ApprovePageRequest applyRequest);

    IPage<ApplyPageResponse> pageV2(@Param("page") IPage<?> page, @Param("params") ApprovePageRequest applyRequest);

}

