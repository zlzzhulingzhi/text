package cn.qbs.wa.train.screen.service;

import cn.qbs.wa.train.screen.entity.ScreenBanner;
import cn.qbs.wa.train.screen.pojo.screenbanner.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * 大屏-宣传栏(ScreenBanner)表服务接口
 *
 * @author makejava
 * @since 2022-10-18 14:20:06
 */
public interface ScreenBannerService extends IService<ScreenBanner> {

    /**
     * 新增大屏-宣传栏
     *
     * @param params
     * @return
     */
    boolean add(ScreenBannerAddRequest params);

    /**
     * 分页查询大屏-宣传栏
     *
     * @param params
     * @return
     */
    IPage<ScreenBannerPageResponse> page(ScreenBannerPageRequest params);

    /**
     * 更新大屏-宣传栏
     *
     * @param params
     * @return
     */
    boolean update(ScreenBannerUpdateRequest params);

    /**
     * 删除大屏-宣传栏
     *
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);

}

