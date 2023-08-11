package cn.qbs.wa.union.auth.mapper;

import cn.qbs.wa.union.auth.entity.UniUserOauth;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户第三方应用登录绑定表(UniUserOauth)表数据库访问层
 *
 * @author makejava
 * @since 2022-07-12 13:53:43
 */
public interface UniUserOauthMapper extends BaseMapper<UniUserOauth> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<UniUserOauth> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<UniUserOauth> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<UniUserOauth> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<UniUserOauth> entities);



}

