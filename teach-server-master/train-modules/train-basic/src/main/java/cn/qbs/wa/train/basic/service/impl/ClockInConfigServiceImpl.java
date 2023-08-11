package cn.qbs.wa.train.basic.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import cn.qbs.wa.train.basic.mapper.ClockInConfigMapper;
import cn.qbs.wa.train.basic.entity.ClockInConfig;
import cn.qbs.wa.train.basic.service.ClockInConfigService;
import cn.qbs.wa.train.basic.pojo.clockinconfig.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 打卡配置(ClockInConfig)表服务实现类
 *
 * @author makejava
 * @since 2022-12-26 15:38:20
 */
@Slf4j
@Service("clockInConfigService")
public class ClockInConfigServiceImpl extends ServiceImpl<ClockInConfigMapper, ClockInConfig> implements ClockInConfigService {

    @Override
    public boolean add(ClockInConfigAddRequest params) {
        ClockInConfig clockInConfig = new ClockInConfig();
        BeanUtils.copyProperties(params, clockInConfig);
        return this.save(clockInConfig);
    }

    @Override
    public IPage<ClockInConfigPageResponse> page(ClockInConfigPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public ClockInConfigDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(ClockInConfigUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        ClockInConfig clockInConfig = new ClockInConfig();
        BeanUtils.copyProperties(params, clockInConfig);
        return this.updateById(clockInConfig);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public List<ClockInConfigDetailResponse> configList(ClockInConfigListRequest request) {
        List<ClockInConfig> clockInConfigList = this.lambdaQuery().eq(StrUtil.isNotBlank(request.getSiteCode()), ClockInConfig::getSiteCode, request.getSiteCode()).list();
        return BeanUtil.copyToList(clockInConfigList,ClockInConfigDetailResponse.class);
    }

}

