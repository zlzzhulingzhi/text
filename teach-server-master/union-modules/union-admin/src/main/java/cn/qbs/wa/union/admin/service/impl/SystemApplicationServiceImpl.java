package cn.qbs.wa.union.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.qbs.wa.union.admin.entity.SystemApplication;
import cn.qbs.wa.union.admin.mapper.SystemApplicationMapper;
import cn.qbs.wa.union.admin.pojo.systemapplication.*;
import cn.qbs.wa.union.admin.service.SystemApplicationService;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 【系统应用】(SystemApplication)表服务实现类
 *
 * @author makejava
 * @since 2022-07-08 09:03:00
 */
@Slf4j
@Service("systemApplicationService")
public class SystemApplicationServiceImpl extends ServiceImpl<SystemApplicationMapper, SystemApplication> implements SystemApplicationService {

    @Override
    public boolean add(SystemApplicationAddRequest params) {
        SystemApplication systemApplication = new SystemApplication();
        BeanUtils.copyProperties(params, systemApplication);
        return this.save(systemApplication);
    }

    @Override
    public IPage<SystemApplicationPageResponse> page(SystemApplicationPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public SystemApplicationDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(SystemApplicationUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        SystemApplication systemApplication = new SystemApplication();
        BeanUtils.copyProperties(params, systemApplication);
        return this.updateById(systemApplication);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    public List<SystemApplicationDetailResponse> appList(SystemApplicationListRequest request) {
        return BeanUtil.copyToList(list(),SystemApplicationDetailResponse.class);
    }

}

