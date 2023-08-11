package cn.qbs.wa.train.logistics.service.platform;

import cn.qbs.wa.train.logistics.entity.DormitoryDevice;
import cn.qbs.wa.train.logistics.pojo.dormitorydevice.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 宿舍设施(DormitoryDevice)表服务接口
 *
 * @author makejava
 * @since 2022-10-13 09:48:04
 */
public interface DormitoryDeviceService extends IService<DormitoryDevice> {

    /**
     * 新增宿舍设施
     *
     * @param params
     * @return
     */
    boolean add(DormitoryDeviceAddRequest params);

    /**
     * 分页查询宿舍设施
     *
     * @param params
     * @return
     */
    IPage<DormitoryDevicePageResponse> page(DormitoryDevicePageRequest params);

    /**
     * 获取详细信息
     *
     * @param id
     * @return
     */
    DormitoryDeviceDetailResponse detail(Serializable id);

    /**
     * 更新宿舍设施
     *
     * @param params
     * @return
     */
    boolean update(DormitoryDeviceUpdateRequest params);

    /**
     * 删除宿舍设施
     *
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

}

