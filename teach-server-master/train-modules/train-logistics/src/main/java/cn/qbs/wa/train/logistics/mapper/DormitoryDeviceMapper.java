package cn.qbs.wa.train.logistics.mapper;

import java.util.List;

import java.io.Serializable;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.train.logistics.entity.DormitoryDevice;
import cn.qbs.wa.train.logistics.pojo.dormitorydevice.DormitoryDeviceDetailResponse;
import cn.qbs.wa.train.logistics.pojo.dormitorydevice.DormitoryDevicePageRequest;
import cn.qbs.wa.train.logistics.pojo.dormitorydevice.DormitoryDevicePageResponse;

/**
 * 宿舍设施(DormitoryDevice)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-13 09:48:04
 */
public interface DormitoryDeviceMapper extends BaseMapper<DormitoryDevice> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<DormitoryDevice> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<DormitoryDevice> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<DormitoryDevice> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<DormitoryDevice> entities);

    IPage<DormitoryDevicePageResponse> page(@Param("page") IPage<?> page, @Param("params") DormitoryDevicePageRequest params);

    DormitoryDeviceDetailResponse selectDetailById(Serializable id);

}

