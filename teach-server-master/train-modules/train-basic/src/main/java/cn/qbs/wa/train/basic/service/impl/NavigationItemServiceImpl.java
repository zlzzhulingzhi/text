package cn.qbs.wa.train.basic.service.impl;

import cn.qbs.wa.train.basic.entity.NavigationItem;
import cn.qbs.wa.train.basic.mapper.NavigationItemMapper;
import cn.qbs.wa.train.basic.pojo.navigationitem.*;
import cn.qbs.wa.train.basic.service.NavigationItemService;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 【导航栏项】(NavigationItem)表服务实现类
 *
 * @author makejava
 * @version 1.0
 * @date 2021-12-08 13:55:36
 */
@Slf4j
@Service("navigationItemService")
public class NavigationItemServiceImpl extends ServiceImpl<NavigationItemMapper, NavigationItem> implements NavigationItemService {

    @Override
    public boolean add(NavigationItemAddRequest params) {
        NavigationItem navigationItem = new NavigationItem();
        BeanUtils.copyProperties(params, navigationItem);
        return this.save(navigationItem);
    }

    @Override
    public IPage<NavigationItemPageResponse> page(NavigationItemPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public NavigationItemDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(NavigationItemUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        NavigationItem navigationItem = new NavigationItem();
        BeanUtils.copyProperties(params, navigationItem);
        return this.updateById(navigationItem);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }
    
}

