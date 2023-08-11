package cn.qbs.wa.union.auth.mapper;

import cn.qbs.wa.union.auth.entity.SystemSubUser;
import cn.qbs.wa.union.auth.pojo.systemsubuser.SystemSubUserDetailResponse;
import cn.qbs.wa.union.auth.pojo.systemsubuser.SystemSubUserPageRequest;
import cn.qbs.wa.union.auth.pojo.systemsubuser.SystemSubUserPageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 子用户表(SystemSubUser)表数据库访问层
 *
 * @author makejava
 * @since 2022-07-08 09:03:06
 */
public interface SystemSubUserMapper extends BaseMapper<SystemSubUser> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<SystemSubUser> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<SystemSubUser> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<SystemSubUser> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<SystemSubUser> entities);

    IPage<SystemSubUserPageResponse> page(@Param("page") IPage<?> page, @Param("params") SystemSubUserPageRequest params);

    SystemSubUserDetailResponse selectDetailById(Serializable id);

}

