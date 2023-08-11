package cn.qbs.wa.teach.course.standard.mapper;

import cn.qbs.wa.teach.course.standard.entity.TBanner;
import cn.qbs.wa.teach.course.standard.pojo.tbanner.TBannerDetailResponse;
import cn.qbs.wa.teach.course.standard.pojo.tbanner.TBannerPageRequest;
import cn.qbs.wa.teach.course.standard.pojo.tbanner.TBannerResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * Banner图片(TBanner)表数据库访问层
 *
 * @author makejava
 * @version 1.0
 * @date 2022-12-22 14:01:03
 */
public interface TBannerMapper extends BaseMapper<TBanner> {

    IPage<TBannerResponse> page(IPage<?> page, @Param("params") TBannerPageRequest params);

    List<TBannerResponse> list(@Param("params") TBannerPageRequest params);

    TBannerDetailResponse selectDetailById(Serializable id);

}

