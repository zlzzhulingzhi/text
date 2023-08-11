package cn.qbs.wa.teach.organization.mapper;

import cn.qbs.wa.teach.organization.entity.WOrg;
import cn.qbs.wa.teach.organization.pojo.worg.WOrgDetailResponse;
import cn.qbs.wa.teach.organization.pojo.worg.WOrgPageRequest;
import cn.qbs.wa.teach.organization.pojo.worg.WOrgPageResponse;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 机构插件表(WOrg)表数据库访问层
 *
 * @author makejava
 * @since 2022-03-01 13:46:16
 */
public interface WOrgMapper extends BaseMapper<WOrg> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<WOrg> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<WOrg> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<WOrg> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<WOrg> entities);

    @InterceptorIgnore(tenantLine = "true")
    IPage<WOrgPageResponse> page(@Param("page") IPage<?> page, @Param("params") WOrgPageRequest params);


    WOrgDetailResponse selectDetailById(Serializable id);

}

