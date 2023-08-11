package cn.qbs.wa.teach.cert.mapper;

import cn.qbs.wa.teach.cert.entity.Cert;
import cn.qbs.wa.teach.cert.pojo.cert.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 证书(Cert)表数据库访问层
 *
 * @author makejava
 * @since 2022-01-19 19:19:20
 */
public interface CertMapper extends BaseMapper<Cert> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<Cert> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<Cert> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<Cert> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<Cert> entities);

    IPage<CertPageResponse> page(@Param("page") IPage<?> page, @Param("params") CertPageRequest params);

    IPage<CertPageResponse> myPage(Page<Object> mpPage, CertPageRequest params);

    List<CertListResponse> mySelectList(CertListRequest params);

    IPage<CertPageResponse> myPageV2(Page<Object> mpPage, CertPageRequest params);

    List<PersonCertDetailResponse> batchSelect(BatchGetCertRequest batchGetCertRequest);
}

