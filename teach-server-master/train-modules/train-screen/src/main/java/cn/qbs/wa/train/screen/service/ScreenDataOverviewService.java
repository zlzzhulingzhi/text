package cn.qbs.wa.train.screen.service;

import cn.qbs.wa.train.screen.entity.ScreenDataOverview;
import cn.qbs.wa.train.screen.pojo.screendataoverview.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 大屏-数据总览(ScreenDataOverview)表服务接口
 *
 * @author makejava
 * @since 2022-10-14 15:37:35
 */
public interface ScreenDataOverviewService extends IService<ScreenDataOverview> {

    /**
     * 分页查询大屏-数据总览
     * @param params
     * @return
     */
    IPage<ScreenDataOverviewPageResponse> page(ScreenDataOverviewPageRequest params);
    
}

