package cn.qbs.wa.train.logistics.mapper;

import java.util.List;

import java.io.Serializable;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.train.logistics.entity.MemberVisit;
import cn.qbs.wa.train.logistics.pojo.membervisit.MemberVisitDetailResponse;
import cn.qbs.wa.train.logistics.pojo.membervisit.MemberVisitPageRequest;
import cn.qbs.wa.train.logistics.pojo.membervisit.MemberVisitPageResponse;

/**
 * 学员访问管理(MemberVisit)表数据库访问层
 *
 * @author makejava
 * @since 2022-12-28 16:24:19
 */
public interface MemberVisitMapper extends BaseMapper<MemberVisit> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<MemberVisit> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<MemberVisit> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<MemberVisit> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<MemberVisit> entities);

    IPage<MemberVisitPageResponse> page(@Param("page") IPage<?> page, @Param("params") MemberVisitPageRequest params);

    MemberVisitDetailResponse selectDetailById(Serializable id);

}

