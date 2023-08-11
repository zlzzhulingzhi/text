package cn.qbs.wa.train.logistics.service.platform;

import cn.qbs.wa.train.logistics.entity.DormitoryAttach;
import cn.qbs.wa.train.logistics.pojo.dormitoryattach.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 宿舍附件(DormitoryAttach)表服务接口
 *
 * @author makejava
 * @since 2022-10-13 09:36:14
 */
public interface DormitoryAttachService extends IService<DormitoryAttach> {

    /**
     * 新增宿舍附件
     *
     * @param params
     * @return
     */
    boolean add(DormitoryAttachAddRequest params);

    /**
     * 分页查询宿舍附件
     *
     * @param params
     * @return
     */
    IPage<DormitoryAttachPageResponse> page(DormitoryAttachPageRequest params);

    /**
     * 获取详细信息
     *
     * @param id
     * @return
     */
    DormitoryAttachDetailResponse detail(Serializable id);

    /**
     * 更新宿舍附件
     *
     * @param params
     * @return
     */
    boolean update(DormitoryAttachUpdateRequest params);

    /**
     * 删除宿舍附件
     *
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

}

