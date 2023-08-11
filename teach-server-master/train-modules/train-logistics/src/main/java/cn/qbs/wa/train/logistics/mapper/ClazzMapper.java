package cn.qbs.wa.train.logistics.mapper;

import cn.qbs.wa.train.logistics.entity.Clazz;
import cn.qbs.wa.train.logistics.pojo.clazz.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 班级表(Clazz)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-08 16:41:48
 */
public interface ClazzMapper extends BaseMapper<Clazz> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Clazz> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Clazz> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Clazz> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Clazz> entities);

    IPage<ClazzPageResponse> page(@Param("page") IPage<?> page, @Param("params") ClazzPageRequest params);

    IPage<ClazzPageResponse> pageV2(@Param("page") IPage<?> page, @Param("params") ClazzPageRequest params);

    ClazzDetailResponse selectDetailById(Long id);

    List<ClazzListResponse> list(@Param("params") ClazzListRequest params);

    ClazzInfoResponse getInfoById(@Param("clazzId") Long clazzId, @Param("orgId") Long orgId);

    List<LiteClazzResponse> listMyClazz(@Param("params") MyClazzListRequest myClazzListRequest);

    void updateLessonNumOrStudentNum(@Param("params") ClazzUpdateRequest params);

    IPage<ClazzSummaryResponse> getClazzSummary(@Param("page") IPage<?> page, @Param("params") ClazzSummaryRequest params);

    List<ClazzSummaryResponse> getClazzSummaryV2(@Param("params") ClazzSummaryRequest params);

    List<IntegrateClazzResponse> listClazzByMemberId(Long memberId);
}

