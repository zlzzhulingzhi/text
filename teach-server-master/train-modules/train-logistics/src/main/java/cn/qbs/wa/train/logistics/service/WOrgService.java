package cn.qbs.wa.train.logistics.service;

import cn.qbs.wa.train.logistics.entity.WOrg;
import cn.qbs.wa.train.logistics.pojo.worg.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 机构插件表(WOrg)表服务接口
 *
 * @author makejava
 * @since 2022-03-01 13:46:16
 */
public interface WOrgService extends IService<WOrg> {

    /**
     * 新增机构插件表
     * @param params
     * @return
     */
    boolean add(WOrgAddRequest params);

    /**
     * 分页查询机构插件表
     * @param params
     * @return
     */
    IPage<WOrgPageResponse> page(WOrgPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    WOrgDetailResponse detail(Serializable id);

    /**
     * 更新机构插件表
     * @param params
     * @return
     */
    boolean update(WOrgUpdateRequest params);

    /**
     * 删除机构插件表
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

}

