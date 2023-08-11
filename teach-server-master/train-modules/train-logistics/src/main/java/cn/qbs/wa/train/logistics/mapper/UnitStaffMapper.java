package cn.qbs.wa.train.logistics.mapper;

import java.util.List;

import java.io.Serializable;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.train.logistics.entity.UnitStaff;
import cn.qbs.wa.train.logistics.pojo.unitstaff.UnitStaffDetailResponse;
import cn.qbs.wa.train.logistics.pojo.unitstaff.UnitStaffPageRequest;
import cn.qbs.wa.train.logistics.pojo.unitstaff.UnitStaffPageResponse;

/**
 * 单位职员表(UnitStaff)表数据库访问层
 *
 * @author makejava
 * @since 2022-09-29 09:04:01
 */
public interface UnitStaffMapper extends BaseMapper<UnitStaff> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<UnitStaff> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<UnitStaff> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<UnitStaff> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<UnitStaff> entities);

    IPage<UnitStaffPageResponse> page(@Param("page") IPage<?> page, @Param("params") UnitStaffPageRequest params);

    UnitStaffDetailResponse selectDetailById(Serializable id);

}

