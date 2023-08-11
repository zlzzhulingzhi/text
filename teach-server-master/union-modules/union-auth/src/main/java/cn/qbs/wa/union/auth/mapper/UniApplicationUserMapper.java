package cn.qbs.wa.union.auth.mapper;

import cn.qbs.wa.union.auth.entity.UniApplicationUser;
import cn.qbs.wa.union.auth.pojo.uniapplicationuser.UniApplicationUserDetailResponse;
import cn.qbs.wa.union.auth.pojo.uniapplicationuser.UniApplicationUserPageRequest;
import cn.qbs.wa.union.auth.pojo.uniapplicationuser.UniApplicationUserPageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 【系统应用-用户】(UniApplicationUser)表数据库访问层
 *
 * @author makejava
 * @since 2022-07-08 09:03:11
 */
public interface UniApplicationUserMapper extends BaseMapper<UniApplicationUser> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<UniApplicationUser> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<UniApplicationUser> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<UniApplicationUser> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<UniApplicationUser> entities);

    IPage<UniApplicationUserPageResponse> page(@Param("page") IPage<?> page, @Param("params") UniApplicationUserPageRequest params);

    UniApplicationUserDetailResponse selectDetailById(Serializable id);

}

