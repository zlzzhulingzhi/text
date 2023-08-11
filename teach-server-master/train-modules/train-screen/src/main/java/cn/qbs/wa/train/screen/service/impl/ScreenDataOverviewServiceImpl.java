package cn.qbs.wa.train.screen.service.impl;

import cn.qbs.wa.train.screen.mapper.ScreenDataOverviewMapper;
import cn.qbs.wa.train.screen.entity.ScreenDataOverview;
import cn.qbs.wa.train.screen.service.ScreenDataOverviewService;
import cn.qbs.wa.train.screen.pojo.screendataoverview.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 大屏-数据总览(ScreenDataOverview)表服务实现类
 *
 * @author makejava
 * @since 2022-10-14 15:37:39
 */
@Slf4j
@Service("screenDataOverviewService")
public class ScreenDataOverviewServiceImpl extends ServiceImpl<ScreenDataOverviewMapper, ScreenDataOverview> implements ScreenDataOverviewService {

    @Override
    public IPage<ScreenDataOverviewPageResponse> page(ScreenDataOverviewPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }
    
}

