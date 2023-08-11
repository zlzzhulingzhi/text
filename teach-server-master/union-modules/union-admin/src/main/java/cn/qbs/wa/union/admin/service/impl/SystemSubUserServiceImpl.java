package cn.qbs.wa.union.admin.service.impl;

import cn.qbs.wa.union.admin.entity.SystemSubUser;
import cn.qbs.wa.union.admin.mapper.SystemSubUserMapper;
import cn.qbs.wa.union.admin.pojo.systemsubuser.*;
import cn.qbs.wa.union.admin.service.SystemSubUserService;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 子用户表(SystemSubUser)表服务实现类
 *
 * @author makejava
 * @since 2022-07-08 09:03:06
 */
@Slf4j
@Service("systemSubUserService")
public class SystemSubUserServiceImpl extends ServiceImpl<SystemSubUserMapper, SystemSubUser> implements SystemSubUserService {

    @Override
    public boolean add(SystemSubUserAddRequest params) {
        SystemSubUser systemSubUser = new SystemSubUser();
        BeanUtils.copyProperties(params, systemSubUser);
        return this.save(systemSubUser);
    }

    @Override
    public IPage<SystemSubUserPageResponse> page(SystemSubUserPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public SystemSubUserDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(SystemSubUserUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        SystemSubUser systemSubUser = new SystemSubUser();
        BeanUtils.copyProperties(params, systemSubUser);
        return this.updateById(systemSubUser);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

}

