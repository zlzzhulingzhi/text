package cn.qbs.wa.train.basic.mapper;

import cn.qbs.wa.train.basic.entity.User;
import cn.qbs.wa.train.basic.pojo.user.*;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (User)表数据库访问层
 *
 * @author makejava
 * @since 2021-11-02 14:55:30
 */
public interface UserMapper extends BaseMapper<User> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<User> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<User> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<User> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<User> entities);

    IPage<UserPageResponse> pageUser(Page<User> page, @Param("params")UserPageRequest request);

    IPage<UserPageResponse> pageListUser(Page<User> page, @Param("params")UserPageRequest request);

    List<UserListResponse> listUser(@Param("params")UserListRequest request);

    List<UserListResponse> listUserByField(@Param("params") UserListFieldRequest request);

    @InterceptorIgnore(tenantLine = "true")
    List<User> checkExistUser(String account);
}

