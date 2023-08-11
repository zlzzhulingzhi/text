package cn.qbs.wa.train.screen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.train.screen.entity.ScreenBanner;
import cn.qbs.wa.train.screen.pojo.screenbanner.ScreenBannerPageRequest;
import cn.qbs.wa.train.screen.pojo.screenbanner.ScreenBannerPageResponse;

/**
 * 大屏-宣传栏(ScreenBanner)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-14 15:45:06
 */
public interface ScreenBannerMapper extends BaseMapper<ScreenBanner> {

    IPage<ScreenBannerPageResponse> page(@Param("page") IPage<?> page, @Param("params") ScreenBannerPageRequest params);

}

