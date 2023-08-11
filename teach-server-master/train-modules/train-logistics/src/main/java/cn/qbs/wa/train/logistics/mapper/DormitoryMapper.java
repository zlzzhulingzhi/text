package cn.qbs.wa.train.logistics.mapper;

import java.util.List;

import java.io.Serializable;

import cn.qbs.wa.train.logistics.pojo.dormitoryschedule.UseDateStateCount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.train.logistics.entity.Dormitory;
import cn.qbs.wa.train.logistics.pojo.dormitory.DormitoryDetailResponse;
import cn.qbs.wa.train.logistics.pojo.dormitory.DormitoryPageRequest;
import cn.qbs.wa.train.logistics.pojo.dormitory.DormitoryPageResponse;

/**
 * 宿舍表(Dormitory)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-08 17:39:59
 */
public interface DormitoryMapper extends BaseMapper<Dormitory> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Dormitory> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Dormitory> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Dormitory> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Dormitory> entities);

    IPage<DormitoryPageResponse> page(@Param("page") IPage<?> page, @Param("params") DormitoryPageRequest params);

    List<DormitoryPageResponse> pages(@Param("params") DormitoryPageRequest params);

    DormitoryDetailResponse selectDetailById(Long id);

    List<UseDateStateCount> pageCount(@Param("params") DormitoryPageRequest params);

}

