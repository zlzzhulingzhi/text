package cn.qbs.wa.train.screen.service;

import cn.qbs.wa.train.screen.entity.ScreenPlanOrg;
import cn.qbs.wa.train.screen.pojo.screenplanorg.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 大屏-万人计划机构(ScreenPlanOrg)表服务接口
 *
 * @author makejava
 * @since 2022-10-14 15:22:15
 */
public interface ScreenPlanOrgService extends IService<ScreenPlanOrg> {

    /**
     * 分页查询大屏-万人计划机构
     *
     * @param params
     * @return
     */
    IPage<ScreenPlanOrgPageResponse> page(ScreenPlanOrgPageRequest params);

}

