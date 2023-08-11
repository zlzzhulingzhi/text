package cn.qbs.wa.train.logistics.mapper;

import cn.qbs.wa.train.logistics.entity.Groups;
import cn.qbs.wa.train.logistics.pojo.groups.GroupsDetailResponse;
import cn.qbs.wa.train.logistics.pojo.groups.GroupsPageRequest;
import cn.qbs.wa.train.logistics.pojo.groups.GroupsPageResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * 通用分组表(Groups)表数据库访问层
 *
 * @author makejava
 * @since 2022-03-28 09:27:33
 */
public interface GroupsMapper extends BaseMapper<Groups> {
    /**
    * 批量新增数据（MyBatis原生foreach方法）
    *
    * @param entities List<Groups> 实例对象列表
    * @return 影响行数
    */
    int insertBatch(@Param("entities") List<Groups> entities);
    
    /**
    * 批量新增或按主键更新数据（MyBatis原生foreach方法）
    *
    * @param entities List<Groups> 实例对象列表
    * @return 影响行数
    * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
    */
    int insertOrUpdateBatch(@Param("entities") List<Groups> entities);

    IPage<GroupsPageResponse> page(@Param("page") IPage<?> page, @Param("params") GroupsPageRequest params);

    GroupsDetailResponse selectDetailById(Serializable id);

    //根据学生id查询标签集合
    List<GroupsDetailResponse> selectDetailByIds(Serializable id);

    //查询所有可用标签
    List<GroupsPageResponse> selectAll();

    GroupsDetailResponse detailList(Long id);

    List<GroupsDetailResponse> selectListAll();
}

