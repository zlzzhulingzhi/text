package cn.qbs.wa.union.admin.mapper;

import cn.qbs.wa.union.admin.entity.SystemUserOauth;
import cn.qbs.wa.union.admin.pojo.systemuseroauth.SystemUserOauthDetailResponse;
import cn.qbs.wa.union.admin.pojo.systemuseroauth.SystemUserOauthPageRequest;
import cn.qbs.wa.union.admin.pojo.systemuseroauth.SystemUserOauthPageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 用户第三方应用登录绑定表(SystemUserOauth)表数据库访问层
 *
 * @author makejava
 * @since 2022-07-08 09:03:08
 */
public interface SystemUserOauthMapper extends BaseMapper<SystemUserOauth> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<SystemUserOauth> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<SystemUserOauth> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<SystemUserOauth> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<SystemUserOauth> entities);

    IPage<SystemUserOauthPageResponse> page(@Param("page") IPage<?> page, @Param("params") SystemUserOauthPageRequest params);

    SystemUserOauthDetailResponse selectDetailById(Serializable id);

}

