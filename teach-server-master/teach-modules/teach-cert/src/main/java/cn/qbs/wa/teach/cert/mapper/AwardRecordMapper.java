package cn.qbs.wa.teach.cert.mapper;

import cn.qbs.wa.teach.cert.entity.AwardRecord;
import cn.qbs.wa.teach.cert.pojo.awardrecord.*;
import cn.qbs.wa.teach.cert.pojo.cert.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 颁发记录(AwardRecord)表数据库访问层
 *
 * @author makejava
 * @since 2022-01-19 11:38:16
 */
public interface AwardRecordMapper extends BaseMapper<AwardRecord> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<AwardRecord> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<AwardRecord> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<AwardRecord> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<AwardRecord> entities);

    IPage<AwardRecordPageResponse> page(@Param("page") IPage<?> page, @Param("params") AwardRecordPageRequest params);

    AwardRecordDetailResponse selectDetailById(Serializable id);

    List<AwardRecordCountResponse> listAwardCount(List<Long> certIdList);

    IPage<AwardRecordPageResponse> selectConditionPage(@Param("page") IPage<?> page,AwardRecordPageRequest params);

    List<AwardRecord> selectConditionList(AwardRecordListRequest params);
}

