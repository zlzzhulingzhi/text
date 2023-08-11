package cn.qbs.wa.train.basic.mapper;

import cn.qbs.wa.train.basic.entity.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (UserInfo)表数据库访问层
 *
 * @author makejava
 * @since 2021-11-02 14:55:30
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<UserInfo> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<UserInfo> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<UserInfo> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<UserInfo> entities);

}

