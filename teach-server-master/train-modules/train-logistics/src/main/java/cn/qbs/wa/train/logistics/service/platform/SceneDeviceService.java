package cn.qbs.wa.train.logistics.service.platform;

import cn.qbs.wa.train.logistics.entity.Clazz;
import cn.qbs.wa.train.logistics.entity.SceneDevice;
import cn.qbs.wa.train.logistics.pojo.scenedevice.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 场景设备(SceneDevice)表服务接口
 *
 * @author makejava
 * @since 2022-10-12 19:05:00
 */
public interface SceneDeviceService extends IService<SceneDevice> {

    /**
     * 新增场景设备
     * @param params
     * @return
     */
    Boolean add(SceneDeviceAddRequest params);

    /**
     * 分页查询场景设备
     * @param params
     * @return
     */
    IPage<SceneDevicePageResponse> page(SceneDevicePageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    SceneDeviceDetailResponse detail(Serializable id);

    /**
     * 更新场景设备
     * @param params
     * @return
     */
    boolean update(SceneDeviceUpdateRequest params);

    boolean updateBatch(SceneDeviceUpdateBatchRequest params);

    /**
     * 删除场景设备
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

    List<SceneDevice> listSceneDevice(SceneDevicePageRequest request);
    
}

