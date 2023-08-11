package cn.qbs.wa.union.auth.mapper;

import cn.qbs.wa.union.auth.entity.UniUser;
import cn.qbs.wa.union.auth.pojo.uniuser.UniUserDetailResponse;
import cn.qbs.wa.union.auth.pojo.uniuser.UniUserPageRequest;
import cn.qbs.wa.union.auth.pojo.uniuser.UniUserPageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 统一用户表(UniUser)表数据库访问层
 *
 * @author makejava
 * @since 2022-07-08 09:03:12
 */
public interface UniUserMapper extends BaseMapper<UniUser> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<UniUser> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<UniUser> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<UniUser> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<UniUser> entities);

    IPage<UniUserPageResponse> page(@Param("page") IPage<?> page, @Param("params") UniUserPageRequest params);

    UniUserDetailResponse selectDetailById(Serializable id);

}

