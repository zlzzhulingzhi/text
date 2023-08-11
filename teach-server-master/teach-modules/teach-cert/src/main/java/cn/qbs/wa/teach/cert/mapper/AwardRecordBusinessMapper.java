package cn.qbs.wa.teach.cert.mapper;

import java.util.List;

import java.io.Serializable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.teach.cert.entity.AwardRecordBusiness;
import cn.qbs.wa.teach.cert.pojo.awardrecordbusiness.AwardRecordBusinessDetailResponse;
import cn.qbs.wa.teach.cert.pojo.awardrecordbusiness.AwardRecordBusinessPageRequest;
import cn.qbs.wa.teach.cert.pojo.awardrecordbusiness.AwardRecordBusinessPageResponse;

/**
 * 基础直播业务关联表(AwardRecordBusiness)表数据库访问层
 *
 * @author makejava
 * @since 2022-01-19 11:38:18
 */
public interface AwardRecordBusinessMapper extends BaseMapper<AwardRecordBusiness> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<AwardRecordBusiness> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<AwardRecordBusiness> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<AwardRecordBusiness> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<AwardRecordBusiness> entities);

    IPage<AwardRecordBusinessPageResponse> page(@Param("page") IPage<?> page, @Param("params") AwardRecordBusinessPageRequest params);

    AwardRecordBusinessDetailResponse selectDetailById(Serializable id);

}

