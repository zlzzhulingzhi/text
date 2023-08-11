package cn.qbs.wa.train.screen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.qbs.wa.train.screen.entity.ScreenPlanOrg;
import cn.qbs.wa.train.screen.pojo.screenplanorg.ScreenPlanOrgPageRequest;
import cn.qbs.wa.train.screen.pojo.screenplanorg.ScreenPlanOrgPageResponse;

/**
 * 大屏-万人计划机构(ScreenPlanOrg)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-14 15:22:15
 */
public interface ScreenPlanOrgMapper extends BaseMapper<ScreenPlanOrg> {

    IPage<ScreenPlanOrgPageResponse> page(@Param("page") IPage<?> page, @Param("params") ScreenPlanOrgPageRequest params);

}

