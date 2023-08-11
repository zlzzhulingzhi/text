package cn.qbs.wa.train.logistics.service.platform;

import cn.qbs.wa.train.logistics.entity.ClassroomDevice;
import cn.qbs.wa.train.logistics.pojo.classroomdevice.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 教室设施(ClassroomDevice)表服务接口
 *
 * @author makejava
 * @since 2022-10-12 18:57:19
 */
public interface ClassroomDeviceService extends IService<ClassroomDevice> {

    /**
     * 新增教室设施
     *
     * @param params
     * @return
     */
    boolean add(ClassroomDeviceAddRequest params);

    /**
     * 分页查询教室设施
     *
     * @param params
     * @return
     */
    IPage<ClassroomDevicePageResponse> page(ClassroomDevicePageRequest params);

    /**
     * 获取详细信息
     *
     * @param id
     * @return
     */
    ClassroomDeviceDetailResponse detail(Serializable id);

    /**
     * 更新教室设施
     *
     * @param params
     * @return
     */
    boolean update(ClassroomDeviceUpdateRequest params);

    /**
     * 删除教室设施
     *
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

}

