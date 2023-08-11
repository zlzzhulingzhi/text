package cn.qbs.wa.train.screen.mapper;

import java.util.List;

import java.io.Serializable;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.train.screen.entity.ScreenStatDataDynamic;
import cn.qbs.wa.train.screen.pojo.screenstatdatadynamic.ScreenStatDataDynamicPageRequest;
import cn.qbs.wa.train.screen.pojo.screenstatdatadynamic.ScreenStatDataDynamicPageResponse;

/**
 * 大屏-动态数据统计(ScreenStatDataDynamic)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-14 15:15:29
 */
public interface ScreenStatDataDynamicMapper extends BaseMapper<ScreenStatDataDynamic> {

    IPage<ScreenStatDataDynamicPageResponse> page(@Param("page") IPage<?> page, @Param("params") ScreenStatDataDynamicPageRequest params);

}

