package cn.qbs.wa.train.screen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.train.screen.entity.ScreenDataOverview;
import cn.qbs.wa.train.screen.pojo.screendataoverview.ScreenDataOverviewPageRequest;
import cn.qbs.wa.train.screen.pojo.screendataoverview.ScreenDataOverviewPageResponse;

/**
 * 大屏-数据总览(ScreenDataOverview)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-14 15:38:52
 */
public interface ScreenDataOverviewMapper extends BaseMapper<ScreenDataOverview> {

    IPage<ScreenDataOverviewPageResponse> page(@Param("page") IPage<?> page, @Param("params") ScreenDataOverviewPageRequest params);

}

