package cn.qbs.wa.train.basic.service;

import cn.qbs.wa.train.basic.entity.NavigationItem;
import cn.qbs.wa.train.basic.pojo.navigationitem.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 【导航栏项】(NavigationItem)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2021-12-08 13:55:36
 */
public interface NavigationItemService extends IService<NavigationItem> {

    /**
     * 新增【导航栏项】
     * @param params
     * @return
     */
    boolean add(NavigationItemAddRequest params);

    /**
     * 分页查询【导航栏项】
     * @param params
     * @return
     */
    IPage<NavigationItemPageResponse> page(NavigationItemPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    NavigationItemDetailResponse detail(Serializable id);

    /**
     * 更新【导航栏项】
     * @param params
     * @return
     */
    boolean update(NavigationItemUpdateRequest params);

    /**
     * 删除【导航栏项】
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);
    
}

