package cn.qbs.wa.train.screen.service.impl;

import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.train.screen.mapper.ScreenPlanOrgMapper;
import cn.qbs.wa.train.screen.entity.ScreenPlanOrg;
import cn.qbs.wa.train.screen.service.ScreenPlanOrgService;
import cn.qbs.wa.train.screen.pojo.screenplanorg.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 大屏-万人计划机构(ScreenPlanOrg)表服务实现类
 *
 * @author makejava
 * @since 2022-10-14 15:22:15
 */
@Slf4j
@Service("screenPlanOrgService")
public class ScreenPlanOrgServiceImpl extends ServiceImpl<ScreenPlanOrgMapper, ScreenPlanOrg> implements ScreenPlanOrgService {

    @Override
    public IPage<ScreenPlanOrgPageResponse> page(ScreenPlanOrgPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

}

