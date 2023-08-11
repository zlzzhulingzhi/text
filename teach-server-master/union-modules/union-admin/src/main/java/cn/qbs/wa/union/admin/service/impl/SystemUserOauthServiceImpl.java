package cn.qbs.wa.union.admin.service.impl;

import cn.qbs.wa.union.admin.entity.SystemUserOauth;
import cn.qbs.wa.union.admin.mapper.SystemUserOauthMapper;
import cn.qbs.wa.union.admin.pojo.systemuseroauth.*;
import cn.qbs.wa.union.admin.service.SystemUserOauthService;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 用户第三方应用登录绑定表(SystemUserOauth)表服务实现类
 *
 * @author makejava
 * @since 2022-07-08 09:03:08
 */
@Slf4j
@Service("systemUserOauthService")
public class SystemUserOauthServiceImpl extends ServiceImpl<SystemUserOauthMapper, SystemUserOauth> implements SystemUserOauthService {

    @Override
    public boolean add(SystemUserOauthAddRequest params) {
        SystemUserOauth systemUserOauth = new SystemUserOauth();
        BeanUtils.copyProperties(params, systemUserOauth);
        return this.save(systemUserOauth);
    }

    @Override
    public IPage<SystemUserOauthPageResponse> page(SystemUserOauthPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public SystemUserOauthDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(SystemUserOauthUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        SystemUserOauth systemUserOauth = new SystemUserOauth();
        BeanUtils.copyProperties(params, systemUserOauth);
        return this.updateById(systemUserOauth);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

}

