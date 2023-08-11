package cn.qbs.wa.teach.cert.mapper;

import java.util.List;

import java.io.Serializable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.teach.cert.entity.AwardRecordDetail;
import cn.qbs.wa.teach.cert.pojo.awardrecorddetail.AwardRecordDetailDetailResponse;
import cn.qbs.wa.teach.cert.pojo.awardrecorddetail.AwardRecordDetailPageRequest;
import cn.qbs.wa.teach.cert.pojo.awardrecorddetail.AwardRecordDetailPageResponse;

/**
 * 证书配置(AwardRecordDetail)表数据库访问层
 *
 * @author makejava
 * @since 2022-01-19 11:38:19
 */
public interface AwardRecordDetailMapper extends BaseMapper<AwardRecordDetail> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<AwardRecordDetail> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<AwardRecordDetail> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<AwardRecordDetail> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<AwardRecordDetail> entities);

    IPage<AwardRecordDetailPageResponse> page(@Param("page") IPage<?> page, @Param("params") AwardRecordDetailPageRequest params);

    AwardRecordDetailDetailResponse selectDetailById(Serializable id);

}

