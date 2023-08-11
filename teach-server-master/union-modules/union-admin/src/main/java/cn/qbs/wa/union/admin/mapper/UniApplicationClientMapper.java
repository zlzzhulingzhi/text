package cn.qbs.wa.union.admin.mapper;

import cn.qbs.wa.union.admin.entity.UniApplicationClient;
import cn.qbs.wa.union.admin.pojo.uniapplicationclient.UniApplicationClientDetailResponse;
import cn.qbs.wa.union.admin.pojo.uniapplicationclient.UniApplicationClientPageRequest;
import cn.qbs.wa.union.admin.pojo.uniapplicationclient.UniApplicationClientPageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 统一应用客户端(UniApplicationClient)表数据库访问层
 *
 * @author makejava
 * @since 2022-07-08 09:03:10
 */
public interface UniApplicationClientMapper extends BaseMapper<UniApplicationClient> {

    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<UniApplicationClient> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<UniApplicationClient> entities);

    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<UniApplicationClient> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<UniApplicationClient> entities);

    IPage<UniApplicationClientPageResponse> page(@Param("page") IPage<?> page, @Param("params") UniApplicationClientPageRequest params);

    UniApplicationClientDetailResponse selectDetailById(Serializable id);

}

