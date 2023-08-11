package cn.qbs.wa.train.screen.service;

import cn.qbs.wa.train.screen.entity.ScreenStatDataDynamic;
import cn.qbs.wa.train.screen.pojo.screenstatdatadynamic.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 大屏-动态数据统计(ScreenStatDataDynamic)表服务接口
 *
 * @author makejava
 * @since 2022-10-14 15:15:29
 */
public interface ScreenStatDataDynamicService extends IService<ScreenStatDataDynamic> {

    /**
     * 分页查询大屏-动态数据统计
     *
     * @param params
     * @return
     */
    IPage<ScreenStatDataDynamicPageResponse> page(ScreenStatDataDynamicPageRequest params);

}

