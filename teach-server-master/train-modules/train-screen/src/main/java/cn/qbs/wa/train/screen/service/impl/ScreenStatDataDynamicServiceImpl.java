package cn.qbs.wa.train.screen.service.impl;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.train.screen.mapper.ScreenStatDataDynamicMapper;
import cn.qbs.wa.train.screen.entity.ScreenStatDataDynamic;
import cn.qbs.wa.train.screen.service.ScreenStatDataDynamicService;
import cn.qbs.wa.train.screen.pojo.screenstatdatadynamic.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 大屏-动态数据统计(ScreenStatDataDynamic)表服务实现类
 *
 * @author makejava
 * @since 2022-10-14 15:15:30
 */
@Slf4j
@Service("screenStatDataDynamicService")
public class ScreenStatDataDynamicServiceImpl extends ServiceImpl<ScreenStatDataDynamicMapper, ScreenStatDataDynamic> implements ScreenStatDataDynamicService {

    @Override
    public IPage<ScreenStatDataDynamicPageResponse> page(ScreenStatDataDynamicPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

}

