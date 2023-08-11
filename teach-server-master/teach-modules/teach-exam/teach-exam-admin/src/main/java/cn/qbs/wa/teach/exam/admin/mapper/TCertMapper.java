package cn.qbs.wa.teach.exam.admin.mapper;

import java.util.List;

import java.io.Serializable;

import cn.qbs.wa.teach.common.core.domain.IdRequest;
import cn.qbs.wa.teach.exam.admin.pojo.tcert.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.teach.exam.common.entity.TCert;

/**
 * 任务证书表(TCert)表数据库访问层
 *
 * @author makejava
 * @since 2022-05-16 13:47:59
 */
public interface TCertMapper extends BaseMapper<TCert> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<TCert> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<TCert> entities);
    
    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<TCert> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<TCert> entities);

    IPage<TCertPageResponse> page(@Param("page") IPage<?> page, @Param("params") TCertPageRequest params);

    TCertDetailResponse selectDetailById(Serializable id);

    List<TCertDetailResponse> certDetail(TCertAddRequest params);

    List<CertDetailResponse> certList(TCertDetailRequest request);

    CertDetailResponse awardRecordPage(AwardRecordPageRequest idRequest);
}

