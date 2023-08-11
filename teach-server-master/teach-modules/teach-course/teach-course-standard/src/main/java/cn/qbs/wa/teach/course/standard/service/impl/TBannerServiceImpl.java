package cn.qbs.wa.teach.course.standard.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.teach.course.standard.entity.TBanner;
import cn.qbs.wa.teach.course.standard.mapper.TBannerMapper;
import cn.qbs.wa.teach.course.standard.pojo.tbanner.*;
import cn.qbs.wa.teach.course.standard.service.TBannerService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * Banner图片(TBanner)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2022-12-22 14:01:03
 */
@Slf4j
@Service("tBannerService")
public class TBannerServiceImpl extends ServiceImpl<TBannerMapper, TBanner> implements TBannerService {

    @Override
    public boolean add(TBannerAddRequest params) {
        TBanner tBanner = new TBanner();
        BeanUtils.copyProperties(params, tBanner);
        return this.save(tBanner);
    }

    @Override
    public IPage<TBannerResponse> page(TBannerPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public List<TBannerResponse> list(TBannerPageRequest params) {
        return baseMapper.list(params);
    }

    @Override
    public TBannerDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(TBannerUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        TBanner tBanner = new TBanner();
        BeanUtil.copyProperties(params, tBanner);
        return this.updateById(tBanner);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }
    
}

