package cn.qbs.wa.teach.course.standard.service;

import cn.qbs.wa.teach.course.standard.entity.TBanner;
import cn.qbs.wa.teach.course.standard.pojo.tbanner.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.List;

/**
 * Banner图片(TBanner)表服务接口
 *
 * @author makejava
 * @version 1.0
 * @date 2022-12-22 14:01:03
 */
public interface TBannerService extends IService<TBanner> {

    /**
     * 新增Banner图片
     * @param params
     * @return
     */
    boolean add(TBannerAddRequest params);

    /**
     * 分页查询Banner图片
     * @param params
     * @return
     */
    IPage<TBannerResponse> page(TBannerPageRequest params);

    List<TBannerResponse> list(TBannerPageRequest params);

    /**
     * 获取详细信息
     * @param id
     * @return
     */
    TBannerDetailResponse detail(Serializable id);

    /**
     * 更新Banner图片
     * @param params
     * @return
     */
    boolean update(TBannerUpdateRequest params);

    /**
     * 删除Banner图片
     * @param idList
     * @return
     */
    boolean deleteByIds(List<Long> idList);
    
}

