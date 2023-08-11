package cn.qbs.wa.union.admin.service.impl;

import cn.qbs.wa.union.admin.entity.UniUser;
import cn.qbs.wa.union.admin.mapper.UniUserMapper;
import cn.qbs.wa.union.admin.pojo.uniuser.*;
import cn.qbs.wa.union.admin.service.UniUserService;
import cn.qbs.wa.teach.common.core.exception.IllegalParamsException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * 统一用户表(UniUser)表服务实现类
 *
 * @author makejava
 * @since 2022-07-08 09:03:12
 */
@Slf4j
@Service("uniUserService")
public class UniUserServiceImpl extends ServiceImpl<UniUserMapper, UniUser> implements UniUserService {

    @Override
    public boolean add(UniUserAddRequest params) {
        UniUser uniUser = new UniUser();
        BeanUtils.copyProperties(params, uniUser);
        return this.save(uniUser);
    }

    @Override
    public IPage<UniUserPageResponse> page(UniUserPageRequest params) {
        return baseMapper.page(params.createMpPage(), params);
    }

    @Override
    public UniUserDetailResponse detail(Serializable id) {
        return baseMapper.selectDetailById(id);
    }

    @Override
    public boolean update(UniUserUpdateRequest params) {
        if (params.getId() == null) {
            throw new IllegalParamsException("ID不能为空！");
        }
        UniUser uniUser = new UniUser();
        BeanUtils.copyProperties(params, uniUser);
        return this.updateById(uniUser);
    }

    @Override
    public boolean deleteByIds(List<Long> idList) {
        return this.removeByIds(idList);
    }

    @Override
    @Transactional
    public UniUser addUser(UniUser uniUser) {
        UniUser existUser = getOne(new LambdaQueryWrapper<UniUser>().eq(UniUser::getAccount, uniUser.getAccount()));
        if (existUser == null) {
            save(uniUser);
            return uniUser;
        }
        return existUser;
    }

}

