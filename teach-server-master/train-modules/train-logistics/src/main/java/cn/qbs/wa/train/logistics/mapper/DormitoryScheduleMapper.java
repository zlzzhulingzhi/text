package cn.qbs.wa.train.logistics.mapper;

import java.util.List;

import java.io.Serializable;

import cn.qbs.wa.train.logistics.entity.DormitorySchedule;
import cn.qbs.wa.train.logistics.pojo.dormitory.DormitoryPageRequest;
import cn.qbs.wa.train.logistics.pojo.dormitoryschedule.UseDateStateCount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.train.logistics.pojo.dormitoryschedule.DormitoryScheduleDetailResponse;
import cn.qbs.wa.train.logistics.pojo.dormitoryschedule.DormitorySchedulePageRequest;
import cn.qbs.wa.train.logistics.pojo.dormitoryschedule.DormitorySchedulePageResponse;

/**
 * 宿舍排期表(DormitorySchedule)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-18 10:03:56
 */
public interface DormitoryScheduleMapper extends BaseMapper<DormitorySchedule> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<DormitorySchedule> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DormitorySchedule> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<DormitorySchedule> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<DormitorySchedule> entities);

    IPage<DormitorySchedulePageResponse> page(@Param("page") IPage<?> page, @Param("params") DormitorySchedulePageRequest params);

    DormitoryScheduleDetailResponse selectDetailById(Serializable id);

    List<UseDateStateCount> pageCount(@Param("params") DormitoryPageRequest params);

}

