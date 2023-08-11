package cn.qbs.wa.train.screen.service.impl;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.train.screen.mapper.ScreenBannerMapper;
import cn.qbs.wa.train.screen.entity.ScreenBanner;
import cn.qbs.wa.train.screen.service.ScreenBannerService;
import cn.qbs.wa.train.screen.pojo.screenbanner.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 大屏-宣传栏(ScreenBanner)表服务实现类
 *
 * @author makejava
 * @since 2022-10-18 14:20:06
 */
@Slf4j
@Service("screenBannerService")
public class ScreenBannerServiceImpl extends ServiceImpl<ScreenBannerMapper, ScreenBanner> implements ScreenBannerService {

    @Override
    public boolean add(ScreenBannerAddRequest params) {
        ScreenBanner screenBanner = new ScreenBanner();
        BeanUtils.copyProperties(params, screenBanner);
        return this.save(screenBanner);
    }

    @Override
    public IPage<ScreenBannerPageResponse> page(ScreenBannerPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }


    @Override
    public boolean update(ScreenBannerUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        ScreenBanner screenBanner = new ScreenBanner();
        BeanUtils.copyProperties(params, screenBanner);
        return this.updateById(screenBanner);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

}

