package cn.qbs.wa.train.allowance.service;

import cn.qbs.wa.train.allowance.entity.Expert;
import cn.qbs.wa.train.allowance.pojo.expert.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 专家信息(Expert)表服务接口
 *
 * @author makejava
 * @since 2022-10-31 18:47:29
 */
public interface ExpertService extends IService<Expert> {

    /**
     * 新增专家信息
     *
     * @param params
     * @return
     */
    boolean add(ExpertAddRequest params);

    /**
     * 分页查询专家信息
     *
     * @param params
     * @return
     */
    IPage<ExpertPageResponse> page(ExpertPageRequest params);

    /**
     * 获取详细信息
     *
     * @param id
     * @return
     */
    ExpertDetailResponse detail(Serializable id);

    /**
     * 更新专家信息
     *
     * @param params
     * @return
     */
    boolean update(ExpertUpdateRequest params);

    /**
     * 删除专家信息
     *
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

}

