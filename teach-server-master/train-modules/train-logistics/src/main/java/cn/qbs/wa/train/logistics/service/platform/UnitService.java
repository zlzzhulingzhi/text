package cn.qbs.wa.train.logistics.service.platform;

import cn.qbs.wa.train.logistics.entity.Unit;
import cn.qbs.wa.train.logistics.pojo.unit.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 单位表(Unit)表服务接口
 *
 * @author makejava
 * @since 2022-09-29 08:31:21
 */
public interface UnitService extends IService<Unit> {

    /**
     * 新增单位表
     *
     * @param params
     * @return
     */
    boolean add(UnitAddRequest params);

    /**
     * 分页查询单位表
     *
     * @param params
     * @return
     */
    IPage<UnitPageResponse> page(UnitPageRequest params);

    List<Unit> listUnit();

    /**
     * 获取详细信息
     *
     * @param id
     * @return
     */
    UnitDetailResponse detail(Serializable id);

    /**
     * 更新单位表
     *
     * @param params
     * @return
     */
    boolean update(UnitUpdateRequest params);

    boolean updateBatch(List<UnitUpdateRequest> params);

    /**
     * 删除单位表
     *
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

}

